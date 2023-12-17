package com.github.plavreshin.web

import com.github.plavreshin.model.SpeechStatsJson
import com.github.plavreshin.service.SpeechService
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.stream.scaladsl.Sink
import org.mdedetrich.pekko.http.support.CirceHttpSupport

class Resource(speechService: SpeechService)
              (implicit val actorSystem: ActorSystem) extends CirceHttpSupport {

  import JsonCodecs.*
  import Resource.*
  import Resource.Validator.*

  val route: Route =
    get {
      path("evaluation") {
        evaluateSpeeches
      }
    }

  private def evaluateSpeeches =
    parameters(Params.Url.repeated) { urls =>
      (for {
        _ <- validateNonEmpty(urls)
        _ <- validateValidUrls(urls)
      } yield speechService.evaluate(urls.toSeq))
        .fold(
          validationErr => complete(StatusCodes.UnprocessableEntity, s"Provided URLs must be either http or https, actual err: $validationErr"),
          stream => {
            onComplete(stream.runWith(Sink.head)) { result =>
              complete(SpeechStatsJson(None, None, None))
            }
          })
    }

}

object Resource {

  private object Validator {

    def validateNonEmpty(urls: Iterable[String]): Either[String, Unit] =
      Either.cond(urls.nonEmpty, (), "URLs are empty")

    def validateValidUrls(urls: Iterable[String]): Either[String, Unit] =
      Either.cond(urls.forall(url => url.startsWith("http") || url.startsWith("https") || url.startsWith("file")), (), "URL is not valid")

  }

  private object Params {

    val Url: String = "url"

  }

}
