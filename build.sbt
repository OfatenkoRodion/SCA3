
version := "0.0.1"

scalaVersion := "2.12.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

name := "analysis"

mainClass in Compile := Some("Main")

resolvers += Resolver.jcenterRepo
resolvers += Resolver.url("bintray", url("https://dl.bintray.com/1on1development/maven"))

libraryDependencies ++= {
  val akkaV = "2.4.17"
  val akkaHttpV = "10.0.9"
  val slickV = "3.2.1"
  val slick_pg = "0.15.7"
  Seq(
    "com.1on1development" %% "slick-migration-api-flyway" % "0.4.1",
    "de.knutwalker" %% "akka-stream-circe" % "3.4.0",
    "de.knutwalker" %% "akka-http-circe" % "3.4.0",
    "com.github.tminglei" %% "slick-pg" % slick_pg,
    "com.github.tminglei" %% "slick-pg_spray-json" % slick_pg,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.byteslounge" %% "slick-repo" % "1.4.3",
    "com.typesafe.slick" %% "slick-hikaricp" % slickV,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.14.0",
    //"com.github.dakatsuka" %% "akka-http-oauth2-client" % "0.1.0",
    "ch.megard" %% "akka-http-cors" % "0.2.1",
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.specs2" %% "specs2-core" % "4.0.3" % "test",
    "org.specs2" %% "specs2-mock" % "4.0.3",
    "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test",
    "junit" % "junit" % "4.11" % "test",
    //    "com.typesafe.slick" %% "slick" % slickV,
    //    "com.typesafe.slick" %% "slick-hikaricp" % slickV,
    "com.typesafe" % "config" % "1.2.1",
    //    "com.h2database" % "h2" % "1.3.175",
    "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    //"io.jsonwebtoken" % "jjwt" % "0.9.0",
    "com.auth0" % "java-jwt" % "3.3.0",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "commons-io" % "commons-io" % "2.6",
    "commons-lang" % "commons-lang" % "2.6",
    "org.scalaz" %% "scalaz-core" % "7.2.20"
  )
}