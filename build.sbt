lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.bigeek",
      scalaVersion := "2.11.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "skeleton"
  )

mainClass in assembly := Some("com.bigeek.twitter.Main")

// Spark configuration using sbt-spark-package
spName := "bigeek/com.bigeek.twitter"
sparkVersion := "2.2.0"
sparkComponents ++= Seq("core", "sql", "streaming")

libraryDependencies ++= Seq(
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.2.0",
  "com.holdenkarau" %% "spark-testing-base" % "2.2.0_0.8.0" % "test",
  "org.twitter4j" % "twitter4j" % "4.0.6" pomOnly(),
  "org.apache.spark" %% "spark-hive" % sparkVersion.value
)


// Assembly task will create a fat jar.
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

// To run using SBT task with provided dependencies
fullClasspath in Runtime := (fullClasspath in Compile).value

// Compile will fail if code has a negative scalastyle result
(compile in Compile) := {
  scalastyle.in(Compile).toTask("").value
  (compile in Compile).value
}
// Shade rules for google dependencies
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.**" -> "shadeio.@1").inAll
)