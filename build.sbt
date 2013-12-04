name := "handsondb"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.google.inject" % "guice" % "4.0-beta",
  "com.google.inject.extensions" % "guice-multibindings" % "4.0-beta",
  "org.mybatis" % "mybatis" % "3.2.3",
  "org.mybatis" % "mybatis-guice" % "3.5",
  "mysql" % "mysql-connector-java" % "5.1.26",
  "org.atteo" % "evo-classindex" % "2.0",
  "org.apache.poi" % "poi" % "3.9" % "test",
  "org.apache.poi" % "poi-ooxml" % "3.9" % "test",
  "org.seleniumhq.selenium" % "selenium-java" % "2.37.1" % "test",
   "org.apache.httpcomponents" % "httpcore" % "4.2.1",
  "org.apache.httpcomponents" % "httpclient" % "4.2.1",
  "commons-codec" % "commons-codec" % "1.6",  
  "commons-dbutils" % "commons-dbutils" % "1.5"
)     

play.Project.playJavaSettings
