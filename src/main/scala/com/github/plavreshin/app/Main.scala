package com.github.plavreshin.app

import com.github.plavreshin.app.wiring.{ServiceWiring, WebWiring}
import com.typesafe.config.ConfigFactory
import org.apache.pekko.http.scaladsl.Http

object Main extends App {

  import Runtime.*

  val config = ConfigFactory.load()
  val serviceWiring = ServiceWiring()
  val webWiring = WebWiring(serviceWiring.speechService)

  Http().newServerAt(config.getString("http.interface"), config.getInt("http.port")).bindFlow(webWiring.route)

}
