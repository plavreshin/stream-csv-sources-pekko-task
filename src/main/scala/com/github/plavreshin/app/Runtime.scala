package com.github.plavreshin.app

import org.apache.pekko.actor.ActorSystem

object Runtime {

  implicit val actorSystem: ActorSystem = ActorSystem()

}
