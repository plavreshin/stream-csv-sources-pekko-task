package com.github.plavreshin.web

import com.github.plavreshin.service.SpeechServiceImpl
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ResourceSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  //  import JsonCodecs.*

  "Resource" should {
    "return OK when evaluating valid URL" in {
      Get("/evaluation") ~> TestScope.resource.route ~> check {
        responseAs[String] shouldEqual "OK"
      }
    }
  }

  private object TestScope {

    val speechService = new SpeechServiceImpl()
    val resource = new Resource(speechService)

  }
}
