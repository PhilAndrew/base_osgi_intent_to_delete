package com.baseosgi.akkaosgi

/*
import akka.actor.{PoisonPill, Props, ActorRef, Actor}
import akka.actor.Actor.Receive
import akka.camel._

import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._


object SupervisorActor {
  case class Startup()
}

class SupervisorActor extends Actor {

  // @todo This should supervise the actors


  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 1, withinTimeRange = 1 minute) {
      case ex: AkkaCamelException       => {
        // @todo We should send back the error message here
        println(ex.getMessage)
        println(ex.toString)
        Resume
      }
    }


  override def preStart() = {
    //val a = context.actorOf(Props[FetchVolumesConsumerActor], name = "volumeConsumer")
  }

  override def receive: Receive = {

    case start: SupervisorActor.Startup => {
    }
  }

}
*/