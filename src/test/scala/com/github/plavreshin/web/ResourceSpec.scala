package com.github.plavreshin.web

import com.github.plavreshin.service.SpeechServiceImpl
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.net.URL

class ResourceSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  "Resource" should {
    "return OK when evaluating valid URL" in {
      Get(s"/evaluation?url=${TestScope.validCsvUrl}") ~> TestScope.resource.route ~> check {
        responseAs[String] shouldEqual
          """{"mostSpeeches":null,"mostSecurity":null,"leastWordy":null}"""
      }
    }

    "fail when invalid URL (ftp) is passed" in {
      Get(s"/evaluation?url=${TestScope.invalidUrl}") ~> TestScope.resource.route ~> check {
        responseAs[String] should include("http or https")
      }
    }

    "fail no urls are passed" in {
      Get(s"/evaluation") ~> TestScope.resource.route ~> check {
        responseAs[String] should include("http or https")
      }
    }

    "return OK when evaluating multiple CSVs" in {
      Get(s"/evaluation?url=${TestScope.validCsvUrl}&url=${TestScope.secondValidCsvUrl}&url=${TestScope.thirdValidCsvUrl}") ~> TestScope.resource.route ~> check {
        responseAs[String] shouldEqual
          """{"mostSpeeches":null,"mostSecurity":null,"leastWordy":null}"""
      }
    }
  }

  private object TestScope {
    val validCsvUrl = getClass.getResource("/valid.csv")
    val secondValidCsvUrl = getClass.getResource("/valid2.csv")
    val thirdValidCsvUrl = getClass.getResource("/valid3.csv")
    val invalidUrl = new URL("ftp://bucket")

    val speechService = new SpeechServiceImpl()
    val resource = new Resource(speechService)

  }
}
