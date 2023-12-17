package com.github.plavreshin.web

import com.github.plavreshin.service.SpeechService
import com.github.plavreshin.web.Resource.Params
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import org.mdedetrich.pekko.http.support.CirceHttpSupport

class Resource(speechService: SpeechService) extends CirceHttpSupport {

  val route: Route =
    get {
      path("evaluation") {
        evaluateSpeeches
      }
    }

  private def evaluateSpeeches =
    parameters(Params.Url.repeated) { urls =>
      complete(StatusCodes.OK)
    }

}

object Resource {

  private object Params {

    val Url: String = "url"

  }

}
