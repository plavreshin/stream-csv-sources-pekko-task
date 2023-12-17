package com.github.plavreshin.app.wiring

import com.github.plavreshin.service.{SpeechService, SpeechServiceImpl}

trait ServiceWiring {

  val speechService: SpeechService

}

object ServiceWiring {

  def apply(): ServiceWiring =
    new ServiceWiringImpl

}

private class ServiceWiringImpl extends ServiceWiring {

  val speechService: SpeechService = new SpeechServiceImpl

}
