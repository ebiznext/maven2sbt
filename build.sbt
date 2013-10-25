
lazy val timeFormat = settingKey[String]("Time format to use")

timeFormat := "dd/MM/yyyy HH:mm:ss"

lazy val time = taskKey[String]("echos the current time")

time := {
    new java.text.SimpleDateFormat(timeFormat.value).format(new java.util.Date())
}

compile in Compile <<= (compile in Compile) dependsOn (Def.task{ streams.value.log.info(time.value)})

