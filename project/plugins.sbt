logLevel := Level.Warn

def sbtOsgi = uri("git://github.com/sbt/sbt-osgi.git")

// Refer to https://github.com/wav/osgi-tooling/issues/10
// If there are dependency issues when loading, the sbt plugin cache may be confused. Try deleting the plugin related cache and artifacts

val osgiTooling = "git://github.com/wav/osgi-tooling.git#cb5365ce4215ebd4511dc3e7643106f0af4639cb"

def sbtKaraf = ProjectRef(
  uri(osgiTooling),
  "sbt-karaf")

def sbtKarafPackaging = ProjectRef(
  uri(osgiTooling),
  "sbt-karaf-packaging")

lazy val plugins = (project in file("."))
  .dependsOn(sbtOsgi, sbtKaraf, sbtKarafPackaging)
