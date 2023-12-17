package com.github.plavreshin.app.wiring

import com.github.plavreshin.service.SpeechService
import com.github.plavreshin.web.Resource
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.server.Route

trait WebWiring {

  def route: Route

}

object WebWiring {

  def apply(speechService: SpeechService)(implicit actorSystem: ActorSystem): WebWiring =
    new WebWiringImpl(speechService)

}

private class WebWiringImpl(speechService: SpeechService)
                           (implicit actorSystem: ActorSystem) extends WebWiring {

  override def route: Route =
    resource.route

  private lazy val resource = new Resource(speechService)

}
