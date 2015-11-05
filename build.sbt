import wav.devtools.sbt.karaf.{packaging, SbtKaraf}, SbtKaraf.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi, SbtOsgi.autoImport._
import KarafKeys._
import KarafPackagingKeys._

enablePlugins(SbtOsgi, SbtKaraf)

name := "base_osgi"

version := "0.1.0.SNAPSHOT"

scalaVersion := "2.11.7"

featuresRequired := Set(
  feature("wrap", dependency = true), // enable provisioning of wrapped bundles, that is those JARs which are non-OSGi
  feature("log"), // implements slf4j
  feature("camel-blueprint"), // Allows Apache Camel to work with Blueprint
  feature("aries-blueprint") // The Blueprint implementation
)

osgiSettings

OsgiKeys.exportPackage := Seq("com.baseosgi")

OsgiKeys.privatePackage := Seq("com.baseosgi.akkaosgi")

OsgiKeys.importPackage := Seq("scala", "scala.*", "org.json", "org.slf4j", "org.osgi.framework",
  "org.apache.camel", "org.apache.camel.impl", "akka.osgi", "com.typesafe.config",
  "akka.actor")

OsgiKeys.bundleActivator := Option("com.baseosgi.akkaosgi.Activator")

libraryDependencies ++= Seq(
  "org.json" % "json" % "20140107" toWrappedBundle(Map(
    "Bundle-SymbolicName" -> "json",
    "Bundle-Version" -> "20140107"
  )),

/*
Do I need these?
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.slf4j" % "slf4j-simple" % "1.7.12",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.12",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.12",
 */
  "org.slf4j" % "slf4j-api" % "1.7.12" % "provided",
  "org.osgi" % "org.osgi.core" % "6.0.0" % "provided",
  FeatureID("org.apache.camel.karaf", "apache-camel", "2.16.0"),
  FeatureID("org.apache.karaf.features", "standard", "4.0.2"),

  // See http://www.scala-sbt.org/0.13/docs/Library-Management.html
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-osgi" % "2.4.0",
  "com.typesafe.akka" %% "akka-camel" % "2.4.0",
  "com.typesafe" % "config" % "1.3.0" % "provided"
)

logLevel := Level.Warn

