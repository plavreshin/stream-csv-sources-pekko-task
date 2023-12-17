package com.github.plavreshin.domain

case class SpeechStats(mostSpeechesByAuthorIn2013: Map[String, Int],
                       mostSecurityTopicsByAuthor: Map[String, Int],
                       leastWordyByAuthor: Map[String, Int]) {

  def mostSpeeches: Option[String] =
    findFirstUnambiguousValue(mostSpeechesByAuthorIn2013, reverse = true)

  def mostSecurity: Option[String] =
    findFirstUnambiguousValue(mostSecurityTopicsByAuthor, reverse = true)

  def leastWordy: Option[String] =
    findFirstUnambiguousValue(leastWordyByAuthor, reverse = false)

  def update(speechItem: SpeechItem): SpeechStats =
    withMostSpeeches(speechItem)
      .withMostSecurity(speechItem)
      .withLeastWordy(speechItem)

  private def findFirstUnambiguousValue(map: Map[String, Int], reverse: Boolean): Option[String] = {
    val sorted = if (reverse) map.toList.sortBy(_._2).reverse else map.toList.sortBy(_._2)
    val firstValue = sorted.headOption

    sorted match {
      case Nil => None
      case _ :: Nil => firstValue.map(_._1)
      case _ =>
        firstValue.flatMap { case (author, value) =>
          val secondValue = sorted.dropWhile(_._2 == value).headOption
          secondValue.flatMap { case (_, value2) =>
            if (value == value2) None else Some(author)
          }
        }
    }
  }

  private def withMostSpeeches(speechItem: SpeechItem): SpeechStats = {
    if (speechItem.date.getYear == 2013) {
      val currentMostSpeeches = mostSpeechesByAuthorIn2013.getOrElse(speechItem.speaker, 0)
      val newMostSpeeches = currentMostSpeeches + 1
      copy(mostSpeechesByAuthorIn2013 = mostSpeechesByAuthorIn2013.updated(speechItem.speaker, newMostSpeeches))
    }
    else
      this
  }

  private def withMostSecurity(speechItem: SpeechItem): SpeechStats = {
    val currentMostSecurity = mostSecurityTopicsByAuthor.getOrElse(speechItem.speaker, 0)
    val newMostSecurity = if (speechItem.topic == SpeechStats.SecurityTopicInGerman) currentMostSecurity + 1 else currentMostSecurity
    copy(mostSecurityTopicsByAuthor = mostSecurityTopicsByAuthor.updated(speechItem.speaker, newMostSecurity))
  }

  private def withLeastWordy(speechItem: SpeechItem): SpeechStats = {
    val currentLeastWordy = leastWordyByAuthor.getOrElse(speechItem.speaker, Int.MaxValue)
    val newLeastWordy = Math.min(currentLeastWordy, speechItem.wordy)
    copy(leastWordyByAuthor = leastWordyByAuthor.updated(speechItem.speaker, newLeastWordy))
  }

}

object SpeechStats {

  val Empty: SpeechStats = SpeechStats(Map.empty, Map.empty, Map.empty)

  val SecurityTopicInGerman: String = "Innere Sicherheit"

}
