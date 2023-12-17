package com.github.plavreshin.web

import com.github.plavreshin.model.SpeechStatsJson
import io.circe.{Encoder, Json}

object JsonCodecs {

  implicit val speechStatsJsonEncoded: Encoder[SpeechStatsJson] = (value: SpeechStatsJson) => Json.obj(
    "mostSpeeches" -> value.mostSpeeches.fold(Json.Null)(Json.fromString),
    "mostSecurity" -> value.mostSecurity.fold(Json.Null)(Json.fromString),
    "leastWordy" -> value.leastWordy.fold(Json.Null)(Json.fromString)
  )

}
