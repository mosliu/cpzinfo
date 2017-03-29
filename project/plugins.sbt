// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"


//addSbtPlugin("be.objectify" %% "deadbolt-java" % "2.2-RC3")

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.1")

//这里添加了play2war 打包以使用tomcat
//https://github.com/dlecan/play2-war-plugin
addSbtPlugin("com.github.play2war" % "play2-war-plugin" % "1.2-beta2")