package com.github.plavreshin.domain

case class SpeechStats(mostSpeechesByAuthor: Map[String, Int],
                       mostSecurityTopicsByAuthor: Map[String, Int],
                       leastWordyByAuthor: Map[String, Int])
