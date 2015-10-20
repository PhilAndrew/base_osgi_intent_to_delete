package com.baseosgi.akkaosgi

import akka.actor.{ActorRef, ExtendedActorSystem}
import akka.camel.ContextProvider
import akka.osgi.OsgiActorSystemFactory
import com.typesafe.config.{Config, ConfigFactory}

//import org.apache.camel.blueprint.BlueprintCamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.osgi.framework.{BundleContext, ServiceRegistration}

//remove if not needed

object AkkaCamelContextProvider {
  @volatile var contextProvider: DefaultCamelContext = null
}

final class AkkaCamelContextProvider extends ContextProvider {
  override def getContext(system: ExtendedActorSystem): DefaultCamelContext = {
    AkkaCamelContextProvider.contextProvider
  }
}

// An OSGi BluePrint bean which interfaces with Akka
class AkkaBean {
  //def getSystem = system

  var bundleContext: BundleContext = _
  var camelContext: Object = _ // BlueprintCamelContext

  def setBundleContext(bcontext: BundleContext) {
    println("set bundle contetx")
    this.bundleContext = bcontext
  }

  def setCamelContext(camelContext: Object) {
    println("set camel context")
    this.camelContext = camelContext
  }

  //private var system: Option[ActorSystem] = None
  private var registration: Option[ServiceRegistration[_]] = None

  private var akkaCamelBean: ActorRef = _

  def start(): Unit = {
    println("start in akka bean")
    println("1")
    AkkaCamelContextProvider.contextProvider = camelContext.asInstanceOf[DefaultCamelContext]

    println("2")
    def getActorSystemConfiguration(context: BundleContext): Config = ConfigFactory.empty
    println("3")
    val sysConfig = getActorSystemConfiguration(bundleContext)
    println("4")
    val actorFactory = OsgiActorSystemFactory(bundleContext, sysConfig)
    println("5")
    println("actorFactory" + actorFactory)
    println("6")

    //system = Some(actorFactory.createActorSystem(Option(getActorSystemName(bundleContext))))
    //NOT system foreach (addLogServiceListener(context, _))
    //system foreach (configure(bundleContext, _))
  }

  def stop(): Unit = {
    //stopWithContext(bundleContext)
  }

  /*  def configure(context: BundleContext, system: ActorSystem): Unit = {
      // Registers this ActorSystem as a service so other blueprints can use this ActorSystem
      registerService(context, system)
    }


    def getActorSystemName(context: BundleContext): String = "RemoteSystem"

    // @todo Wire up blueprint stop
    def stopWithContext(context: BundleContext): Unit = {
      registration foreach (_.unregister())
      system foreach (_.shutdown())
    }

    def registerService(context: BundleContext, system: ActorSystem): Unit = {
      println("register service")
      registration.foreach(_.unregister()) //Cleanup
      val properties = new Properties()
      properties.put("name", system.name)
      registration = Some(context.registerService(classOf[ActorSystem].getName, system,
        properties.asInstanceOf[Dictionary[String, Any]]))
    }*/
}
