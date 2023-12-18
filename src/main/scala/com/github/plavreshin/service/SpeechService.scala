package com.github.plavreshin.service

import cats.syntax.all.*
import com.github.plavreshin.domain.CsvFileError.InvalidCsv
import com.github.plavreshin.domain.{CsvFileError, SpeechItem, SpeechStats}
import com.github.plavreshin.service.csv.CsvParser
import com.typesafe.scalalogging.LazyLogging
import org.apache.pekko.NotUsed
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.{Framing, Merge, Sink, Source, StreamConverters}
import org.apache.pekko.util.ByteString

import java.net.URL
import scala.concurrent.Future
import scala.util.control.NonFatal

trait SpeechService {

  def evaluate(urls: Seq[String]): Future[Either[CsvFileError, SpeechStats]]

}

class SpeechServiceImpl(implicit actorSystem: ActorSystem) extends SpeechService with LazyLogging {

  override def evaluate(urls: Seq[String]): Future[Either[CsvFileError, SpeechStats]] =
    mergeSources(urls.distinct.map(parseAndCollectSpeeches))
      .fold(SpeechStats.Empty)(evaluateSpeechItems)
      .flatMapConcat(Source.single)
      .map(_.asRight[CsvFileError])
      .recover { case NonFatal(ex) =>
        logger.error("Error while evaluating speeches", ex)
        InvalidCsv.asLeft[SpeechStats]
      }
      .runWith(Sink.head)

  private def parseAndCollectSpeeches(url: String): Source[Either[String, SpeechItem], NotUsed] =
    StreamConverters.fromInputStream(() => new URL(url).openStream())
      .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 1024, allowTruncation = true))
      .map(_.utf8String.split(","))
      .mapMaterializedValue[NotUsed](_ => NotUsed)
      .prefixAndTail(1).flatMapConcat { case (header, rows) =>
        header.headOption.fold(Source.empty[Array[String]]) { _ => rows }
      }
      .via(CsvParser.decodeCsvFlow)

  private def evaluateSpeechItems(state: SpeechStats, row: Either[String, SpeechItem]): SpeechStats =
    row.fold(_ => state, state.update)

  private def mergeSources[T](sources: Seq[Source[T, NotUsed]]): Source[T, NotUsed] =
    sources match {
      case Nil => Source.empty[T]
      case Seq(a) => a
      case Seq(a, b) => a.merge(b)
      case _ => Source.combine(sources.head, sources(1), sources.drop(2) *)(Merge(_))
    }

}
