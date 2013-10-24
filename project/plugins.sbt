resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

resolvers += "Sonatype Repository" at "https://oss.sonatype.org/content/groups/public"

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.3.0")

addSbtPlugin("com.ebiznext.sbt.plugins" % "sbt-cxf-wsdl2java" % "0.1.1")

addSbtPlugin("com.ebiznext.sbt.plugins" % "sbt-soapui-mockservice" % "0.1.1")

addSbtPlugin("com.ebiznext.sbt.plugins" % "sbt-groovy" % "0.1")

addSbtPlugin("com.ebiznext.sbt.plugins" % "sbt-andromda" % "0.1.2")

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "0.4.2")

addSbtPlugin("de.johoop" % "findbugs4sbt" % "1.2.1")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.3.2")

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.1")

addSbtPlugin("com.ebiznext.sbt.plugins" % "sbt-sonar" % "0.1.1")

lazy val root = project.in( file(".") ) dependsOn (junitXmlListener, xsbtFilter)

lazy val junitXmlListener = uri("git://github.com/fupelaqu/junit_xml_listener.git#patch-1")

lazy val xsbtFilter = uri("git://github.com/sdb/xsbt-filter.git")
