package com.baseosgi.akkaosgi

import java.util.{Dictionary, Properties}
import akka.actor.{ExtendedActorSystem, ActorRef, Props, ActorSystem}
import akka.camel.ContextProvider
import akka.osgi.OsgiActorSystemFactory
import com.typesafe.config.{ConfigFactory, Config}
import org.apache.camel.blueprint.BlueprintCamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.osgi.framework.{ServiceRegistration, BundleContext}
//remove if not needed
import scala.collection.JavaConversions._

object AkkaCamelContextProvider {
  @volatile var contextProvider: DefaultCamelContext = null
}

final class AkkaCamelContextProvider extends ContextProvider {
  override def getContext(system: ExtendedActorSystem): DefaultCamelContext = {
    AkkaCamelContextProvider.contextProvider
  }
}

/* An OSGi BluePrint bean which interfaces with Akka */
class AkkaBean {
  def getSystem = system

  var bundleContext: BundleContext = _
  var camelContext: BlueprintCamelContext = _

  def setBundleContext(bcontext: BundleContext) {
    println("set bundle contetx")
    this.bundleContext = bcontext
  }

  def setCamelContext(camelContext: BlueprintCamelContext) {
    println("set camel context")
    this.camelContext = camelContext
  }

  private var system: Option[ActorSystem] = None
  private var registration: Option[ServiceRegistration[_]] = None

  private var akkaCamelBean: ActorRef = _

  def start(): Unit = {
    println("start in akka bean")
    AkkaCamelContextProvider.contextProvider = camelContext.asInstanceOf[DefaultCamelContext]

    val sysConfig = getActorSystemConfiguration(bundleContext)
    val actorFactory = OsgiActorSystemFactory(bundleContext, sysConfig)
    system = Some(actorFactory.createActorSystem(Option(getActorSystemName(bundleContext))))
    //system foreach (addLogServiceListener(context, _))
    system foreach (configure(bundleContext, _))
  }

  def stop(): Unit = {
    stopWithContext(bundleContext)
  }

  def configure(context: BundleContext, system: ActorSystem): Unit = {
    // Registers this ActorSystem as a service so other blueprints can use this ActorSystem
    registerService(context, system)
  }

  def getActorSystemConfiguration(context: BundleContext): Config = ConfigFactory.empty
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
  }
}
