logLevel := Level.Warn

def sbtOsgi = uri("git://github.com/sbt/sbt-osgi.git")

// Refer to https://github.com/wav/osgi-tooling/issues/10
// If there are dependency issues when loading, the sbt plugin cache may be confused. Try deleting the plugin related cache and artifacts
def sbtKarafPackaging = ProjectRef(
  uri("git://github.com/wav/osgi-tooling.git#e430ed654365d4af3792004c05b4ec63818f5635"),
  "sbt-karaf-packaging")

lazy val plugins = (project in file("."))
  .dependsOn(sbtOsgi, sbtKarafPackaging)
