name := "base_osgi"

version := "1.0"

scalaVersion := "2.11.7"

import java.io.PrintWriter

import wav.devtools.sbt.karaf.packaging.SbtKarafPackaging
import SbtKarafPackaging.autoImport._
import KarafPackagingKeys._

enablePlugins(SbtKarafPackaging, SbtOsgi)

featuresRequired := Map(
	"scr" -> "*",
	"wrap" -> "*",
	"log" -> "*",
  "aries-blueprint" -> "*",
	"camel-weather" -> "*",
	"camel-blueprint" -> "*")

featuresAddDependencies := true

libraryDependencies ++= Seq(
  FeatureID("org.apache.karaf.features", "standard", "4.0.1"),
  FeatureID("org.apache.camel.karaf", "apache-camel", "2.16.0"),

  "biz.aQute.bnd" % "annotation" % "2.4.0" % "provided",
  "org.json" % "json" % "20140107", // or "20141113"
  "org.osgi" % "org.osgi.core" % "6.0.0" % "provided",
  
  "com.typesafe.akka" %% "akka-actor" % "2.3.10",
  "com.typesafe.akka" %% "akka-remote" % "2.3.10",

  // slf4j-api and osgi.core shouldn't be in the project feature
  "com.typesafe.akka" %% "akka-osgi" % "2.3.10" intransitive(),
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.10" intransitive(),
  "com.typesafe.akka" %% "akka-camel" % "2.3.10" intransitive())

osgiSettings

OsgiKeys.exportPackage := Seq("com.baseosgi")

OsgiKeys.privatePackage := Seq("com.baseosgi.akkaosgi", "com.baseosgi.weather")

OsgiKeys.importPackage := Seq(
  "sun.misc;resolution:=optional",
  "org.osgi.framework.*",
  "scala.*",
  "org.osgi.service.blueprint;version=\"[1.0.0,2.0.0)\""
)

OsgiKeys.additionalHeaders := Map(
  "Service-Component" -> "*"
  // Note: Conditional package brings in the classes from this package and packs them into the
  // OSGi bundle.
  // demo.internal.WeiboConsumer
  // THIS WOULD INCLUDE THESE CLASSES "Conditional-Package" -> "com.universe.service.rest.*"
)

OsgiKeys.bundleActivator := Option("com.baseosgi.akkaosgi.Activator")

logLevel := Level.Warn






// Post-processing of the features.xml file

val triggeredTask = taskKey[Unit]("Triggered by featuresFile")

triggeredTask <<= Def.task {
  val featuresFile = "./target/scala-2.11/features.xml"

  val fileLines = io.Source.fromFile(featuresFile).getLines.toList
  val outLines = fileLines.map( (line: String) => { if (line.indexOf("mvn:org.apache.karaf.features/standard/4.0.1/xml/features") >= 0) "" else line })
  new PrintWriter(featuresFile) { write(outLines.mkString("\r\n")); close }
  def unit: Unit = null
  unit
}.triggeredBy(featuresFile in Compile)
