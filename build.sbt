name := """rent-control"""

version := "2.6.x"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

scalacOptions += "-Ypartial-unification"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

lazy val doobieVersion = "0.5.3"

TwirlKeys.templateImports += "models.search._"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "org.webjars" % "bootstrap" % "4.1.0"
libraryDependencies ++= Seq(
	"org.tpolecat" %% "doobie-core"		% doobieVersion,
	"org.tpolecat" %% "doobie-postgres"	% doobieVersion,
	"org.tpolecat" %% "doobie-specs2"	% doobieVersion
)
