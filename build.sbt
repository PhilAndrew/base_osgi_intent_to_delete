name := "base_osgi"

version := "1.0"

scalaVersion := "2.11.7"

import org.apache.commons.io.FileUtils
import wav.devtools.sbt.karaf.packaging.SbtKarafPackaging
import SbtKarafPackaging.autoImport._
import KarafPackagingKeys._

import scala.xml.{Elem, NodeSeq}

enablePlugins(SbtKarafPackaging)

enablePlugins(SbtOsgi)

libraryDependencies ++= Seq(
//  "com.sun.xml.bind" % "jaxb-impl" % "2.1.2",
//  "javax.xml.bind" % "jaxb-api" % "2.1",

  // Logging
  // @todo Seems slf4j cannot be in the features file
  //"org.slf4j" % "slf4j-api" % "1.7.10",

  //"org.slf4j" % "osgi-over-slf4j" % "1.7.10",
  //"ch.qos.logback" % "logback-core" % "1.1.2",
  //"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  //"ch.qos.logback" % "logback-classic" % "1.1.3",

  "biz.aQute.bnd" % "annotation" % "2.4.0",

  "org.osgi" % "org.osgi.core" % "4.3.0",
  "org.apache.felix" % "org.apache.felix.framework" % "4.6.0" % "runtime",

  // Add other library dependencies from here onwards

  "org.json" % "json" % "20140107",
  //FeatureID("org.json", "json", "20140107"),

  //"org.json" % "json" % "20141113",
  "com.typesafe.akka" %% "akka-osgi" % "2.3.10",

  //"com.typesafe.akka" %% "akka-actor" % "2.3.10",
  //"com.typesafe.akka" %% "akka-slf4j" % "2.3.10",
  "com.typesafe.akka" %% "akka-camel" % "2.3.10",
  "com.typesafe.akka" %% "akka-remote" % "2.3.10",

  "org.apache.camel" % "camel-blueprint" % "2.15.2",
  "org.apache.camel" % "camel-core-osgi" % "2.15.2"
  //"org.apache.camel" % "camel-weather" % "2.15.2"

)

//featuresRequired := Map("camel-blueprint" -> "2.15.2")

//featuresRequired := Map("scr" -> "*")

//def bundleActivator = "com.universe.internal.Activator"

OsgiKeys.exportPackage := Seq("com.baseosgi")

OsgiKeys.privatePackage := Seq("com.baseosgi.akkaosgi", "com.baseosgi.weather")

OsgiKeys.importPackage := Seq(
  "sun.misc;resolution:=optional",
  "org.osgi.service.blueprint;version=\"[1.0.0,2.0.0)\"",
  "!aQute.bnd.annotation.*",
  "*"
)

//OsgiKeys.bundleActivator := Option(bundleActivator)

OsgiKeys.additionalHeaders := Map(
  "Service-Component" -> "*"
  // Note: Conditional package brings in the classes from this package and packs them into the
  // OSGi bundle.
  // demo.internal.WeiboConsumer
  // THIS WOULD INCLUDE THESE CLASSES "Conditional-Package" -> "com.universe.service.rest.*"
)

//OsgiKeys.bundleActivator := Option("com.baseosgi.akkaosgi.Activator")

osgiSettings

val excludedFromKaraf = Set("mvn:org.slf4j/slf4j-api/1.7.5", "mvn:com.sun.xml.bind/jaxb-impl/2.2.11", "mvn:com.sun.xml.bind/jaxb-core/2.2.11")


















// Post-processing of the features.xml file

val triggeredTask = taskKey[Unit]("Triggered by featuresFile")

triggeredTask <<= Def.task {

  def removeTrailingSlashes(s: String) = {
    s.reverse.dropWhile( _ == '/' ).reverse
  }

  def addChild(n: scala.xml.Node, newChild: scala.xml.Node) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, child ++ newChild : _*)
    case _ => error("Can only add children to elements!")
  }

  def eraseChildren(n: scala.xml.Node) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, Seq() : _*)
    case _ => error("Can only erase children of elements!")
  }

  def setChildren(n: scala.xml.Node, newChildren: Seq[scala.xml.Node]) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
    {
      //println(newChildren)
      var newNode = eraseChildren(n)
      /*newChildren.map( (c) => {
        addChild(newNode, c)
      })*/
      var loop:Int = 0

      while (loop < newChildren.size) {
        val c = newChildren(loop)
        newNode = addChild(newNode, c)
        loop = loop + 1
      }
      newNode
    }
    case _ => error("Can set children of elements!")
  }

  val featuresFile = "./target/scala-2.11/features.xml"

  import scala.xml.XML
  val xml = XML.loadFile(featuresFile)
  //println(xml)

  //val prettyPrinter = new scala.xml.PrettyPrinter(80, 2)
  //val prettyXml = prettyPrinter.format(xml)
  // @todo Save prettyXml

  val features: NodeSeq = (xml \ "feature")

  val featuresFixed = features.map( (feature) => {
    //println("Feature is: " + feature.toString)
    val bundles: NodeSeq = (feature \ "bundle")

    val bundlesFixed = bundles.flatMap((bundle) => {
      val slashlessBundle = removeTrailingSlashes(bundle.text)

      if (excludedFromKaraf.contains(slashlessBundle)) None
      else Some({

        val dep = bundle.attribute("dependency").get.head.text
        val start = bundle.attribute("start").get.head.text

        // dependency true and start false is bad, remove them
        if ((dep == "true") && (start == "false")) {
          val res = <bundle>{slashlessBundle}</bundle>
          res
        } else {
          // @todo Should use slashlessBundle here
          bundle
        }
      })
    })
    val newFeature = setChildren(feature, bundlesFixed)
    newFeature
  })

  val mainFixed = setChildren(xml, featuresFixed)

  XML.save(featuresFile, mainFixed, "UTF-8", true, null)
}.triggeredBy(featuresFile in Compile)
