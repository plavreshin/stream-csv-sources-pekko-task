package com.github.plavreshin.domain

sealed trait StatsGatheringError

object StatsGatheringError {

  case object MissingValidHeader extends StatsGatheringError
  case object InvalidCsv extends StatsGatheringError

}
