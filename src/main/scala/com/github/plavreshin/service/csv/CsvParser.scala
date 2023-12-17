package com.github.plavreshin.service.csv

import cats.syntax.all.*
import com.github.plavreshin.domain.SpeechItem
import kantan.csv.ops.*
import kantan.csv.{CsvConfiguration, RowDecoder, rfc}
import org.apache.pekko.NotUsed
import org.apache.pekko.stream.scaladsl.Flow

object CsvParser {

  val decodeCsvFlow: Flow[Array[String], Either[String, SpeechItem], NotUsed] =
    Flow[Array[String]]
      .map { row =>
        rowDecoder.decode(row.mkString(",")).fold(
          error => error.toString.asLeft[SpeechItem],
          row => row.asRight[String]
        )
      }

  private val CsvConfiguration: CsvConfiguration = rfc.withHeader(false)

  private val rowDecoder = RowDecoder.decoder(0, 1, 2, 3)(SpeechItem.apply).contramapEncoded(splitLine)

  private def splitLine(line: String): Seq[String] =
    line
      .readCsv[Seq, Seq[String]](CsvConfiguration)
      .headOption
      .fold(Seq.empty[String])(_.getOrElse(Seq.empty))
      .map(_.trim)

}
