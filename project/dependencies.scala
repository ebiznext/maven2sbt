import sbt._
import Keys._

/**
 * @author stephane.manciot@ebiznext.com
 *
 */
object dependencies {

  //lazy val commonsLang = "commons-lang" % "commons-lang" % "2.6"

  lazy val commonsDependencies = Seq(
    "commons-lang" % "commons-lang" % "2.6",
    "commons-beanutils" % "commons-beanutils" % "1.8.0",
    "commons-collections" % "commons-collections" % "3.2.1"
  )

  lazy val cxfDependencies = Seq(
    "org.apache.cxf" % "cxf-rt-frontend-jaxrs" % "2.7.3"
  )

  lazy val hibernateDependencies = Seq(
    "org.hibernate" % "hibernate-core" % "3.5.6-Final",
    "org.hibernate" % "hibernate-annotations" % "3.5.6-Final",
    "org.hibernate" % "hibernate-ehcache" % "3.5.6-Final"
  )

  lazy val springDependencies = Seq(
    "org.springframework" % "spring-orm" % "3.0.6.RELEASE",
    "org.springframework" % "spring-context-support" % "3.0.6.RELEASE"
  )

  lazy val jacksonDependencies = Seq(
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.1.0",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.1.0",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.0",
    "org.codehaus.jackson" %  "jackson-core-asl" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-mapper-asl" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-core-lgpl" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-mapper-lgpl" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-jaxrs" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-xc" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-mrbean" % "1.9.0",
    "org.codehaus.jackson" %  "jackson-smile" % "1.9.0"
  )

  def httpDependencies(configs : Seq[Configuration]) = applyConfigurations(Seq(
    "org.apache.httpcomponents" % "httpclient" % "4.2.1",
    "org.apache.httpcomponents" % "httpmime" % "4.2.1",
    "org.apache.httpcomponents" % "httpclient-cache" % "4.2.1",
    "org.apache.httpcomponents" % "fluent-hc" % "4.2.1",
    "xml-apis" % "xml-apis" % "1.3.03",
    "net.sourceforge.nekohtml" % "nekohtml" % "1.9.15",
    "org.codehaus.groovy.modules.http-builder" % "http-builder" % "0.5.2"
  ))(configs)

  lazy val groovy = "org.codehaus.groovy" % "groovy-all" % "2.1.7"

  lazy val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided"

  def jettyDependencies(configs : Seq[Configuration]) = applyConfigurations(Seq(
    "org.mortbay.jetty" % "jetty" % "6.1.22",
    "org.mortbay.jetty" % "jetty-naming" % "6.1.22",
    "org.mortbay.jetty" % "jetty-plus" % "6.1.22"))(configs)

  lazy val junit = "com.novocode" % "junit-interface" % "0.10" % "test->default" 

  def applyConfigurations(dependencies:Seq[ModuleID])(configs:Seq[Configuration]):Seq[ModuleID] = dependencies map (_ % (configs map (_.name) mkString(",")))

  def testDependencies(configs : Seq[Configuration]) = applyConfigurations(Seq(
	  "org.scalatest" %% "scalatest" % "1.9.2", 
	  //"junit" % "junit" % "4.10", 
    "com.novocode" % "junit-interface" % "0.10",
	  "org.mockito" % "mockito-core" % "1.9.5")
  )(configs)
}