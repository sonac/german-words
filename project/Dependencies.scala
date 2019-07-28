import sbt._

object Dependencies {

  lazy val Http4sVersion = "0.20.0-RC1"
  lazy val CirceVersion = "0.10.0"
  lazy val Specs2Version = "4.2.0"
  lazy val LogbackVersion = "1.2.3"
  lazy val PureConfigVersion = "0.10.2"
  lazy val DoobieVersion = "0.5.4"
  lazy val FlywayVersion = "5.2.4"
  lazy val ScalaTestVersion = "3.0.5"
  lazy val ScalaMockVersion = "4.1.0"
  lazy val mockitoV = "1.5.0"

  lazy val blazeServer
      : ModuleID = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
  lazy val blazeClient
      : ModuleID = "org.http4s" %% "http4s-blaze-client" % Http4sVersion
  lazy val http4sCirce
      : ModuleID = "org.http4s" %% "http4s-circe" % Http4sVersion
  lazy val http4sDsl: ModuleID = "org.http4s" %% "http4s-dsl" % Http4sVersion
  lazy val circeGeneric: ModuleID = "io.circe" %% "circe-generic" % CirceVersion
  lazy val circeGenericExtras = "io.circe" %% "circe-generic-extras" % CirceVersion
  lazy val circeParser: ModuleID = "io.circe" %% "circe-parser" % CirceVersion
  lazy val circeLiteral: ModuleID = "io.circe" %% "circe-literal" % CirceVersion
  lazy val specsCore
      : ModuleID = "org.specs2" %% "specs2-core" % Specs2Version % "test"
  lazy val logback
      : ModuleID = "ch.qos.logback" % "logback-classic" % LogbackVersion
  lazy val pureConfig
      : ModuleID = "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
  lazy val scalaTest
      : ModuleID = "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
  lazy val scalaMock
      : ModuleID = "org.scalamock" %% "scalamock" % ScalaMockVersion % "test"
  lazy val http4sTesting
      : ModuleID = "org.http4s" %% "http4s-testing" % Http4sVersion % "test"
  lazy val mockito: ModuleID = "org.mockito" %% "mockito-scala" % mockitoV

  lazy val dependencies: Seq[ModuleID] = Seq(
    blazeServer,
    blazeClient,
    http4sCirce,
    http4sDsl,
    circeGeneric,
    circeGenericExtras,
    circeParser,
    circeLiteral,
    specsCore,
    logback,
    pureConfig,
    scalaTest,
    scalaMock,
    http4sTesting,
    mockito
  )

}
