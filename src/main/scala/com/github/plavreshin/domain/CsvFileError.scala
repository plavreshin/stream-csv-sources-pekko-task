package com.github.plavreshin.domain

sealed trait CsvFileError

object CsvFileError {

  case object MissingValidHeader extends CsvFileError
  case object InvalidCsv extends CsvFileError

}
