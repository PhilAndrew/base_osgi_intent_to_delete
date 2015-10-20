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

OsgiKeys.importPackage := Seq("scala", "scala.*", "org.json", "org.slf4j", "org.osgi.framework")

OsgiKeys.bundleActivator := Option("com.baseosgi.akkaosgi.Activator")

libraryDependencies ++= Seq(
  "org.json" % "json" % "20140107" toWrappedBundle(Map(
    "Bundle-SymbolicName" -> "json",
    "Bundle-Version" -> "20140107"
  )),
  "org.slf4j" % "slf4j-api" % "1.7.12" % "provided",
  "org.osgi" % "org.osgi.core" % "6.0.0" % "provided",
  FeatureID("org.apache.camel.karaf", "apache-camel", "2.16.0"),
  FeatureID("org.apache.karaf.features", "standard", "4.0.2"))

logLevel := Level.Warn


import java.io.PrintWriter



// Post-processing of the features.xml file
val triggeredTask = taskKey[Unit]("Triggered by featuresFile")

triggeredTask <<= Def.task {
  val featuresFile = "./target/scala-2.11/features.xml"

  val fileLines = io.Source.fromFile(featuresFile).getLines.toList
  val outLines = fileLines.map( (line: String) => { if (line.indexOf("mvn:org.apache.karaf.features/standard") >= 0) "" else line })
  new PrintWriter(featuresFile) { write(outLines.mkString("\r\n")); close }
  def unit: Unit = null
  unit
}.triggeredBy(featuresFile in Compile)


