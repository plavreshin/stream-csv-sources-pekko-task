package com.github.plavreshin.web

import com.github.plavreshin.service.SpeechServiceImpl
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Span}
import org.scalatest.wordspec.AnyWordSpec

import java.net.URL

class ResourceSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with TimeLimitedTests {
  val timeLimit: Span = Span(5000, Millis)

  "Resource" should {
    "return OK when evaluating valid URL" in {
      Get(s"/evaluation?url=${TestScope.validCsvUrl}") ~> TestScope.resource.route ~> check {
        responseAs[String] shouldEqual
          """{"mostSpeeches":"Caesare Collins","mostSecurity":"Alexander Abel","leastWordy":"Bernhard Belling"}"""
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
          """{"mostSpeeches":"Alexander Abel","mostSecurity":"Caesare Collins","leastWordy":"Alexander Abel"}"""
      }
    }

    "return OK with empty state in case of empty but valid CSV" in {
      Get(s"/evaluation?url=${TestScope.headerOnlyCsv}") ~> TestScope.resource.route ~> check {
        responseAs[String] shouldEqual
          """{"mostSpeeches":null,"mostSecurity":null,"leastWordy":null}"""
      }
    }

  }

  private object TestScope {
    val headerOnlyCsv = "https://gist.githubusercontent.com/plavreshin/5762344cb6ddf5840427a095d00b28e8/raw/236ecacd01b0eb1e66660a8b3093fb54e26126e5/headerOnly.csv"
    val validCsvUrl = "https://gist.githubusercontent.com/plavreshin/5379f789f290a6355eecb47688498f12/raw/987f791b2ebc4ce5bcfbc2e915f63eebf0a42724/speakersValid.csv"
    val secondValidCsvUrl = "https://gist.githubusercontent.com/plavreshin/ab4d55bf0d16d89408d3c8673d96fddd/raw/d3d5765f49f7e19d31bc7197aadf45218ada9dd1/speakersValid2csv"
    val thirdValidCsvUrl = "https://gist.githubusercontent.com/plavreshin/17ffa5a5e68d02772e1bfd722a0613a5/raw/644495258dbb6bf1c7933e7e879905a4f78f6e28/speakersValid3.csv"

    val invalidUrl = new URL("ftp://bucket")

    val speechService = new SpeechServiceImpl()
    val resource = new Resource(speechService)

  }
}
