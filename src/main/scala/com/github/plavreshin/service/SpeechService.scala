package com.github.plavreshin.service

import com.github.plavreshin.domain.{SpeechItem, SpeechStats}
import com.github.plavreshin.service.csv.CsvParser
import org.apache.pekko.NotUsed
import org.apache.pekko.stream.scaladsl.{Framing, Merge, Source, StreamConverters}
import org.apache.pekko.util.ByteString

import java.net.URL

trait SpeechService {

  def evaluate(urls: Seq[String]): Source[SpeechStats, NotUsed]

}

class SpeechServiceImpl extends SpeechService {

  override def evaluate(urls: Seq[String]): Source[SpeechStats, NotUsed] = {
    val sources: Seq[Source[Either[String, SpeechItem], NotUsed]] = urls.map { url =>
      StreamConverters.fromInputStream(() => new URL(url).openStream())
        .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 1024, allowTruncation = true))
        .map(_.utf8String.split(","))
        .mapMaterializedValue[NotUsed](_ => NotUsed)
        .via(CsvParser.decodeCsvFlow)
    }
    mergeSources(sources)
      .wireTap(x => println(x.map(_.speaker).getOrElse("")))
    Source.empty
  }

  private def mergeSources[T](sources: Seq[Source[T, NotUsed]]): Source[T, NotUsed] =
    sources.size match {
      case length if length < 1 => Source.empty[T]
      case 1 => sources.head
      case 2 => sources.head.merge(sources(1))
      case _ => Source.combine(sources.head, sources(1), sources.drop(2) *)(Merge(_))
    }

}

object SpeechService {

}
