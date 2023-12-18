package com.github.plavreshin.app.wiring

import com.github.plavreshin.service.{SpeechService, SpeechServiceImpl}
import org.apache.pekko.actor.ActorSystem

trait ServiceWiring {

  val speechService: SpeechService

}

object ServiceWiring {

  def apply()(implicit actorSystem: ActorSystem): ServiceWiring =
    new ServiceWiringImpl

}

private class ServiceWiringImpl(implicit actorSystem: ActorSystem) extends ServiceWiring {

  val speechService: SpeechService = new SpeechServiceImpl

}
