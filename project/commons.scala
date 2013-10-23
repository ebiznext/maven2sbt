import sbt._
import Keys._

/**
 * @author hayssam
 *
 */
object commons {

  lazy val defaultSettings = Seq(
      organization := "com.ebiznext.sbt.sample",
      version := "1.0-SNAPSHOT",
      scalaVersion := "2.10.2",
      scalacOptions ++= Seq("-deprecation", "-feature"),
      resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      resolvers += "SOAPUI Repository" at "http://www.soapui.org/repository/maven2",
      resolvers += "Sonatype Repository" at "https://oss.sonatype.org/content/groups/public",
      resolvers += "AndroMDA Repository" at "http://team.andromda.org/maven2"
  )

}