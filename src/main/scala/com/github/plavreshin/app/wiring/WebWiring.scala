package com.github.plavreshin.app.wiring

import com.github.plavreshin.service.SpeechService
import com.github.plavreshin.web.Resource
import org.apache.pekko.http.scaladsl.server.Route

trait WebWiring {

  def route: Route

}

object WebWiring {

  def apply(speechService: SpeechService): WebWiring =
    new WebWiringImpl(speechService)

}

private class WebWiringImpl(speechService: SpeechService) extends WebWiring {

  override def route: Route =
    resource.route

  private lazy val resource = new Resource(speechService)

}
