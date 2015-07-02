name := """activator-akka-spray"""

version := "1.0"

scalaVersion := "2.10.2"


resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "spray nightlies" at "http://nightlies.spray.io"

libraryDependencies ++= {
  val akkaVersion  = "2.3.2"
  val sprayVersion = "1.3.1"
  Seq(
  "com.typesafe.akka"  %% "akka-actor"       % akkaVersion exclude ("org.scala-lang" , "scala-library"),
  "com.typesafe.akka"  %% "akka-slf4j"       % akkaVersion exclude ("org.slf4j", "slf4j-api"),
  "ch.qos.logback"      % "logback-classic"  % "1.0.13",
  "io.spray"            %% "spray-can"        % sprayVersion,
  "io.spray"            %% "spray-routing"    % sprayVersion,
  "io.spray"           %% "spray-json"       % "1.2.5" exclude ("org.scala-lang" , "scala-library"),
  "org.scalaj" %% "scalaj-http" % "1.1.0",
  "org.apache.poi" % "poi" % "3.9",  
  "org.apache.poi" % "poi-ooxml" % "3.11",
  "org.specs2" %% "specs2" % "1.14" % "test",
  "com.unboundid" % "unboundid-ldapsdk" % "2.3.1",
  "io.spray"            %% "spray-testkit"    % sprayVersion % "test",
  "com.typesafe.akka"  %% "akka-testkit"     % akkaVersion        % "test",
  "com.novocode"        % "junit-interface"  % "0.11-RC1" % "test->default" exclude("org.hamcrest", "hamcrest-core"),
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "org.apache.directory.api" % "api-all" % "1.0.0-M30"
  )
}

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
