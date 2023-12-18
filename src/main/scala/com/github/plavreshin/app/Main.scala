package com.github.plavreshin.app

import com.github.plavreshin.app.wiring.{ServiceWiring, WebWiring}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.apache.pekko.http.scaladsl.Http

object Main extends App with LazyLogging {

  import Runtime.*

  val config = ConfigFactory.load()
  val serviceWiring = ServiceWiring()
  val webWiring = WebWiring(serviceWiring.speechService)

  logger.info("Starting HTTP server...")

  Http().newServerAt(config.getString("http.interface"), config.getInt("http.port")).bindFlow(webWiring.route)

  logger.info(s"Application started at ${config.getString("http.interface")}:${config.getInt("http.port")}")
}
