package com.github.plavreshin.web

import com.github.plavreshin.model.SpeechStatsJson
import com.github.plavreshin.service.SpeechService
import io.scalaland.chimney.dsl.*
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import org.mdedetrich.pekko.http.support.CirceHttpSupport

class Resource(speechService: SpeechService) extends CirceHttpSupport {

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
      } yield speechService.gatherSpeechStats(urls.toSeq))
        .fold(
          validationErr => complete(StatusCodes.UnprocessableEntity, s"Provided URLs must be either http or https, actual err: $validationErr"),
          evaluationResult => {
            onComplete(evaluationResult) { result =>
              result.fold(
                err => complete(StatusCodes.InternalServerError, s"Error occurred during evaluation: $err"),
                speechStats =>
                  speechStats.fold(
                    err => complete(StatusCodes.InternalServerError, s"Failed to process csv sources: $err"),
                    stats =>
                      complete(
                        stats
                          .into[SpeechStatsJson]
                          .withFieldComputed(_.mostSpeeches, _.mostSpeeches)
                          .withFieldComputed(_.mostSecurity, _.mostSecurity)
                          .withFieldComputed(_.leastWordy, _.leastWordy)
                          .transform
                      )
                  )
              )
            }
          })
    }

}

object Resource {

  private object Validator {

    def validateNonEmpty(urls: Iterable[String]): Either[String, Unit] =
      Either.cond(urls.nonEmpty, (), "URLs are empty")

    def validateValidUrls(urls: Iterable[String]): Either[String, Unit] =
      Either.cond(urls.forall(url => url.startsWith("http") || url.startsWith("https")), (), "URL is not valid")

  }

  private object Params {

    val Url: String = "url"

  }

}
