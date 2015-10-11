logLevel := Level.Warn

def sbtOsgi = uri("git://github.com/sbt/sbt-osgi.git")

// Refer to https://github.com/wav/osgi-tooling/issues/10
// If there are dependency issues when loading, the sbt plugin cache may be confused. Try deleting the plugin related cache and artifacts
def sbtKarafPackaging = ProjectRef(
  uri("git://github.com/wav/osgi-tooling.git#ebb235651f5a162c801d165d8dc55b1c09aaedab"),
  "sbt-karaf-packaging")

lazy val plugins = (project in file("."))
  .dependsOn(sbtOsgi, sbtKarafPackaging)