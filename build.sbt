import wav.devtools.sbt.karaf.{packaging, SbtKaraf}, SbtKaraf.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi, SbtOsgi.autoImport._
import KarafKeys._
import KarafPackagingKeys._

enablePlugins(SbtOsgi, SbtKaraf)

name := "base_osgi"

version := "0.1.0.SNAPSHOT"

scalaVersion := "2.11.7"

featuresRequired := Set(
  feature("wrap", dependency = true) /* enable provisioning of wrapped bundles */,
  feature("log") /* implements slf4j */,
  feature("camel-blueprint"),
  feature("aries-blueprint"))

osgiSettings

OsgiKeys.exportPackage := Seq("com.baseosgi")

OsgiKeys.privatePackage := Seq("com.baseosgi.akkaosgi")

OsgiKeys.importPackage := Seq("scala", "scala.*", "org.json", "org.slf4j", "org.osgi.framework",
  "org.apache.camel", "org.apache.camel.impl", "akka.osgi", "com.typesafe.config")

OsgiKeys.bundleActivator := Option("com.baseosgi.akkaosgi.Activator")

libraryDependencies ++= Seq(
  "org.json" % "json" % "20140107" toWrappedBundle(Map(
    "Bundle-SymbolicName" -> "json",
    "Bundle-Version" -> "20140107"
  )),
  "org.slf4j" % "slf4j-api" % "1.7.12" % "provided",
  "org.osgi" % "org.osgi.core" % "6.0.0" % "provided",
  FeatureID("org.apache.camel.karaf", "apache-camel", "2.16.0"),
  FeatureID("org.apache.karaf.features", "standard", "4.0.2"),

  //"com.typesafe.akka" % "akka-osgi_2.11" % "2.4.0"
/*
<bundle>mvn:com.typesafe.akka/akka-actor_2.11/2.3.10</bundle>
<bundle>mvn:com.typesafe.akka/akka-camel_2.11/2.3.10</bundle>
<bundle>mvn:com.typesafe.akka/akka-osgi_2.11/2.3.10</bundle>
<bundle>mvn:com.typesafe/config/1.2.1</bundle>
*/

  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-osgi" % "2.4.0",
  "com.typesafe.akka" %% "akka-camel" % "2.4.0",
  "com.typesafe" % "config" % "1.3.0" % "provided"
)

logLevel := Level.Warn


import java.io.PrintWriter






val removeFromFeatures = Seq("mvn:org.apache.karaf.features/standard", "mvn:org.osgi/org.osgi.compendium/4.3.1", "mvn:org.osgi/org.osgi.core/4.3.1",
"mvn:org.slf4j/slf4j-api/1.7.12", "wrap:mvn:com.sun.xml.bind/jaxb-impl/2.2.6", "mvn:com.typesafe.akka/akka-slf4j_2.11/2.4.0")

// Post-processing of the features.xml file
val triggeredTask = taskKey[Unit]("Triggered by featuresFile")

triggeredTask <<= Def.task {
  val featuresFile = "./target/scala-2.11/features.xml"

  val fileLines = io.Source.fromFile(featuresFile).getLines.toList
  val outLines = fileLines.map( (line: String) => {
    // if (line.indexOf("mvn:org.apache.karaf.features/standard") >= 0) "" else line
    if (removeFromFeatures.find( (l) => { line.indexOf(l) >= 0 } ).isDefined) "" else line
  })
  new PrintWriter(featuresFile) { write(outLines.mkString("\r\n")); close }
  def unit: Unit = null
  unit
}.triggeredBy(featuresFile in Compile)


