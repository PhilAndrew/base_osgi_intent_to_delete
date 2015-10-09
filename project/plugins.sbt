logLevel := Level.Warn

// For Karaf deployment

//val ivyLocal = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
//externalResolvers := Seq(ivyLocal)

// https://github.com/wav/osgi-tooling

//addSbtPlugin("wav.devtools" % "sbt-karaf-packaging" % "0.1.0.SNAPSHOT")

def sbtOsgi = uri("git://github.com/sbt/sbt-osgi.git")

// Refer to https://github.com/wav/osgi-tooling/issues/10
// This looks like a plugin cache issue with sbt. Try deleting the plugin related cache and artifacts. For example
def sbtKarafPackaging = ProjectRef(
  uri("git://github.com/wav/osgi-tooling.git#e390f0b948586e2ea3e6d18b2b44dd5be3669508"),
  "sbt-karaf-packaging")

lazy val plugins = (project in file("."))
  .dependsOn(sbtOsgi)
  .dependsOn(sbtKarafPackaging)

