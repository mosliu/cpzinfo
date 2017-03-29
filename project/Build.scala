import sbt._
import Keys._
//import PlayProject._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

  val appName = "CPZInfos"
  val appVersion = "5.0.0-RC1-20140109"

  val appDependencies = Seq(
    // Add your project dependencies here,
    //"org.hibernate" % "hibernate-entitymanager" % "4.1.2.Final",
    jdbc,
    javaCore,
    cache,
    javaJdbc,
    //javaEbean,
    javaJpa,
    "mysql" % "mysql-connector-java" % "5.1.25" ,
    "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final",
    "org.hibernate" % "hibernate-core" % "4.1.1.Final",
    "org.hibernate" % "hibernate-validator" % "4.2.0.Final",
//    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
//    "com.typesafe" % "play-plugins-mailer_2.10" % "2.1.0",
    "com.typesafe" %% "play-plugins-mailer" % "2.2.0",
//    "be.objectify" %% "deadbolt-2" % "1.1.3-SNAPSHOT",
//    "be.objectify" %% "deadbolt-2" % "1.1.2",
    "be.objectify" %% "deadbolt-java" % "2.2-RC1",
//    "be.objectify" %% "deadbolt-scala" % "2.1-SNAPSHOT",
    "org.docx4j" % "docx4j" % "2.8.1",
    "org.bouncycastle" % "bcprov-jdk15on" % "1.47",
    "commons-codec" % "commons-codec" % "1.9",
    "com.google.zxing" % "core" % "2.3.0",
    "com.google.zxing" % "javase" % "2.3.0"
)

//  val main = play.Project(appName, appVersion, appDependencies, mainLang = JAVA).settings( 
	val main = play.Project(appName, appVersion, appDependencies).settings( 
      // Add your own project settings here
      ebeanEnabled := false,Play2WarKeys.servletVersion := "3.0",
     resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
  resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
  ).settings(Play2WarPlugin.play2WarSettings: _*)

}
