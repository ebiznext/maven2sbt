//logLevel := Level.Debug

lazy val timeFormat = settingKey[String]("Time format to use")

lazy val time = taskKey[String]("echos the current time")

timeFormat := "dd/MM/yyyy HH:mm:ss"

time := {
    val format = new java.text.SimpleDateFormat(timeFormat.value)
    val t : String = format.format(new java.util.Date())
    streams.value.log.info(t)
    t
}

compile in Compile <<= (compile in Compile) dependsOn time

