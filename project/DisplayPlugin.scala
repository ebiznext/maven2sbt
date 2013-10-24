package com.ebiznext.sbt.sample.plugins

import sbt._

import Keys._

/**
 * @author stephane.manciot@ebiznext.com
 *
 */
object DisplayPlugin extends Plugin {
  trait Keys {
    lazy val timeFormat = settingKey[String]("Time format to use")
    lazy val time = taskKey[String]("echos the current time")
  }

  object display extends Keys {
    lazy val settings = Seq(
  	  timeFormat := "dd/MM/yyyy HH:mm:ss",
  	  time := {
        val format = new java.text.SimpleDateFormat(timeFormat.value)
        val t : String = format.format(new java.util.Date())
        streams.value.log.info(t)
        t
  	  }
    )
  }
}

