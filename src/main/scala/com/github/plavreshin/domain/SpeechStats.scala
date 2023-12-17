package com.github.plavreshin.domain

case class SpeechStats(mostSpeechesByAuthor: Map[String, Int],
                       mostSecurityTopicsByAuthor: Map[String, Int],
                       leastWordyByAuthor: Map[String, Int]) {

  def mostSpeeches: Option[String] = mostSpeechesByAuthor.maxByOption(_._2).map(_._1)

  def mostSecurity: Option[String] = mostSecurityTopicsByAuthor.maxByOption(_._2).map(_._1)

  def leastWordy: Option[String] = leastWordyByAuthor.minByOption(_._2).map(_._1)

}
