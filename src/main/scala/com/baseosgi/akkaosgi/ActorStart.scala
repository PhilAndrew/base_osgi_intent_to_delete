package com.baseosgi.akkaosgi

import akka.actor.{ActorRef, PoisonPill, Props}

/*
Responsible for starting and stopping all actors
 */

class ActorStart {

  var akka: AkkaBean = _

  def setAkka(akka: AkkaBean) = {
    println("set Akka")
    this.akka = akka
  }

  def start(): Unit = {
    println("Start in ActorStart")
    // @todo Create actors here on startup

    val supervisor = akka.getSystem.get.actorOf(Props[SupervisorActor], name="someActor")
    supervisor ! SupervisorActor.Startup
  }

  def stop(): Unit = {
    // @todo Should stop some akka actors volumeConsumer ! PoisonPill
  }
}
