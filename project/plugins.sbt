logLevel := Level.Warn

def sbtOsgi = uri("git://github.com/sbt/sbt-osgi.git")

// Refer to https://github.com/wav/osgi-tooling/issues/10
// If there are dependency issues when loading, the sbt plugin cache may be confused. Try deleting the plugin related cache and artifacts

def sbtKaraf = ProjectRef(
  uri("git://github.com/wav/osgi-tooling.git#461d6fd97949b3f91ab5c3b1710faa6b6752db72"),
  "sbt-karaf")

def sbtKarafPackaging = ProjectRef(
  uri("git://github.com/wav/osgi-tooling.git#461d6fd97949b3f91ab5c3b1710faa6b6752db72"),
  "sbt-karaf-packaging")

lazy val plugins = (project in file("."))
  .dependsOn(sbtOsgi, sbtKaraf, sbtKarafPackaging)
