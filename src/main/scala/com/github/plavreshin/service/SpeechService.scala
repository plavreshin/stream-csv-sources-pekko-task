package com.github.plavreshin.service

import com.github.plavreshin.domain.{CsvFileError, SpeechStats}
import org.apache.pekko.stream.scaladsl.Source

trait SpeechService {

  def evaluate(urls: Seq[String]): Source[Either[CsvFileError, SpeechStats], Any]

}

class SpeechServiceImpl extends SpeechService {

  override def evaluate(urls: Seq[String]): Source[Either[CsvFileError, SpeechStats], Any] = ???
}

object SpeechService {

}
