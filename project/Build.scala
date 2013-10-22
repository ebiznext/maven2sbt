import sbt._
import Keys._
import com.earldouglas.xsbtwebplugin.{Container, WebPlugin, PluginKeys => WebKeys}

import com.ebiznext.sbt.plugins._
import CxfWsdl2JavaPlugin._
import GroovyPlugin._
import SoapUIMockServicePlugin._
import AndroMDAPlugin._
import SonarPlugin._

import sbtfilter.{Plugin => FilterPlugin}
import FilterPlugin._

import de.johoop.jacoco4sbt._
import JacocoPlugin._

import org.scalastyle.sbt.ScalastylePlugin.{Settings => ScalastyleSettings}

import de.johoop.findbugs4sbt.FindBugs._

import eu.henkelmann.sbt._

/**
 * @author stephane.manciot@ebiznext.com
 *
 */
object BuildProjects extends Build {

  lazy val wsclientPackage = "com.ebiznext.sbt.sample.reception.wsclient"

  lazy val itContainer = Container("it")

  private lazy val androMDATrigger = taskKey[Unit]("Trigger AndroMDA generation when necessary")

  def subProject(name:String) : Project = {
    Project(name, file(name))
	  .configs( IntegrationTest )
    .settings( Defaults.itSettings : _*)

    .settings(commons.defaultSettings : _*)

    .settings(groovy.settings : _*)
    .settings(testGroovy.settings : _*)
    .settings(
      libraryDependencies += dependencies.groovy
    )

    .settings(FilterPlugin.filterSettings: _*)

    .settings(jacoco.settings: _*)
    .settings(
      parallelExecution in jacoco.Config := false
    )

    .settings(ScalastyleSettings: _*)

    .settings(findbugsSettings: _*)

    .settings(sonar.settings: _*)

    .settings(
      sonar.sonarProperties ++= Seq(
        "sonar.profile" -> "Sonar way",//"Sonar way with Findbugs",
        "sonar.java.coveragePlugin" -> "jacoco",
        "sonar.jacoco.reportPath" -> ((jacoco.outputDirectory in jacoco.Config).value / "jacoco.exec").getAbsolutePath,
        "sonar.findbugs.reportPath" -> ((findbugsReportPath.value)getOrElse(crossTarget.value / "findbugs" / "findbugs.xml")).getAbsolutePath, // FIXME
        "sonar.junit.reportsPath" -> (crossTarget.value / "test-reports").getAbsolutePath,
        "sonar.surefire.reportsPath" -> (crossTarget.value / "test-reports").getAbsolutePath
      )
    )

    .settings(
      libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test->default",
      testOptions += Tests.Argument("-Dsample.properties=" + ((resourceDirectory in Test).value / "sample.properties")),
      //junit xml listener
      testListeners <<= crossTarget.map(t => Seq(new JUnitXmlTestsListener(t.getAbsolutePath)))
    )

    .settings(androMDA.settings :_*)
    // to demonstrate the use of tasks dependencies
    .settings(
      androMDATrigger in ("mda", Compile) := {
        val f : File = baseDirectory.value / ".." / "andromda.log"
        if(!f.exists()){
          val taskKey = androMDA.androMDAGenerate in ("mda", Compile)
          val result: Option[Result[Seq[File]]] = Project.evaluateTask(taskKey, state.value)
          result match
          {
            case None => // Key wasn't defined.
            case Some(Inc(inc)) => // error detail, inc is of type Incomplete, use Incomplete.show(inc.tpe) to get an error message
            case Some(Value(v)) => // do something with v: inc.Analysis
          }
        }
      },
      androMDA.androMDAGeneratedSources in Compile <<= (androMDA.androMDAGeneratedSources in Compile) dependsOn (androMDATrigger in ("mda", Compile))
    )

  }

  lazy val mda = (
    subProject("mda")
      .settings(
        androMDA.lastModifiedCheck := true,
        androMDA.androMDAProperties ++= Seq(
          "validation" -> ("" + true),
          "model.uri" -> ((sourceDirectory in ("mda", Compile)).value / "uml" / "sample.uml2").getAbsolutePath,
          "conf.dir" -> ((sourceDirectory in ("mda", Compile)).value / "config").getAbsolutePath,
          "application.package" -> "com.ebiznext.sbt.sample",
          "application.id" -> "sample",
          "common.generated.dir" -> (sourceManaged in ("common", Compile)).value.getAbsolutePath,
          "core.generated.dir" -> (sourceManaged in ("core", Compile)).value.getAbsolutePath,
          "core.manual.dir" -> (javaSource in ("core", Compile)).value.getAbsolutePath,
          "core.resources.dir" -> (resourceDirectory in ("core", Compile)).value.getAbsolutePath,
          /*
          "webservice.generated.dir" -> (sourceManaged in ("web", Compile)).value.getAbsolutePath,
          "sessionTimeout" -> "15",
          "webservice.host" -> "localhost",
          "webservice.port" -> "8080",
          */
          "filter" -> ("java,spring")
        )
      )
      .settings(
      )

  )

  lazy val common = (
    subProject("common")
    .dependsOn(mda)

    // begin cxf settings
    .settings(cxf.settings : _*)
    .settings(cxf.wsdls := Seq(
      cxf.Wsdl((resourceDirectory in Compile).value / "ListerReceptions.wsdl", Seq("-p",  wsclientPackage), "E2DListerReceptions"),
      cxf.Wsdl((resourceDirectory in Compile).value / "LoginReception.wsdl", Seq("-p",  wsclientPackage), "E2DLoginReception"),
      cxf.Wsdl((resourceDirectory in Compile).value / "ReceptionnerCommande.wsdl", Seq("-p",  wsclientPackage), "E2DReceptionnerCommande"),
      cxf.Wsdl((resourceDirectory in Compile).value / "validerCodeOIA.wsdl", Seq("-p",  wsclientPackage), "E2DvaliderCodeOIA"),
      cxf.Wsdl((resourceDirectory in Compile).value / "ListerFournisseurs.wsdl", Seq("-p",  wsclientPackage), "E2DListerFournisseurs"),
      cxf.Wsdl((resourceDirectory in Compile).value / "ListerReceptionsFournisseur.wsdl", Seq("-p",  wsclientPackage), "E2DListerReceptionsFournisseur")))
    // end cxf settings

    .settings(libraryDependencies ++= dependencies.commonsDependencies ++ dependencies.cxfDependencies ++ dependencies.jacksonDependencies)

    .settings(
      jacoco.excludes in jacoco.Config ++= Seq("*vo*", "*wsclient*"),
      sonar.sonarProperties ++= Seq(
        "sonar.jacoco.excludes" -> "*wsclient*,*Exception.java",
        "sonar.exclusions" -> "**/wsclient/*.java,**/vo/*.java,**/*Exception.java"
      )
    )

  )

  lazy val core = (
    subProject("core")
    .dependsOn(common)

    .settings(libraryDependencies ++= dependencies.springDependencies ++ dependencies.hibernateDependencies)

    .settings(
      sonar.sonarProperties ++= Seq(
        "sonar.jacoco.excludes" -> "*Base.java,*Exception.java,*Service.java",
        "sonar.exclusions" -> "**/*Base.java,**/*Exception.java,**/*Service.java, **/sample/*.java"
      )
    )

  )

  lazy val web = (
    subProject("web")
    .dependsOn(core)

    .settings(WebPlugin.webSettings :_*)

    .settings(itContainer.deploy(IntegrationTest)("/sample" -> "web"):_* )

    .settings(
      libraryDependencies ++= 
        Seq(dependencies.servletApi) ++ 
        dependencies.httpDependencies(Seq(Test)) ++ 
        dependencies.jettyDependencies(Seq(IntegrationTest,WebPlugin.container.Configuration)))

    .settings(
      WebKeys.port in itContainer.Configuration := 8888,
      WebKeys.env in itContainer.Configuration := Some(file("web/src/test/resources/conf") / "jetty" / "jetty-env.xml" asFile),
      testOptions += Tests.Argument("-Dsample.port=" + (WebKeys.port in itContainer.Configuration).value),
      testOptions += Tests.Argument("-Dsample.properties=" + ((classDirectory in Test).value / "sample.properties")),
      // start application before tests execution
      //testOptions in Test += Tests.Setup( () => (WebKeys.start in itContainer.Configuration).value ),
      // stop application after tests completion
      //testOptions in Test += Tests.Cleanup( () => (WebKeys.stop in itContainer.Configuration).value ),
      parallelExecution in Test := false
    )

    .settings(
      FilterKeys.extraProps in Test += "wsdlDirectory" -> ((classDirectory in Test).value).getAbsolutePath,
      includeFilter in (Test, FilterKeys.filterResources) := "*.properties" | "*.wsdl"
    )

    // begin SOAP UI settings 
    .settings(soapui.settings : _*)
    .settings(
        soapui.mockServices := Seq(
        soapui.MockService( (resourceDirectory in Test).value / "EDListeFournisseurs-soapui-project.xml", "9001"),
        soapui.MockService( (resourceDirectory in Test).value / "EDListeReceptions-soapui-project.xml", "9002"),
        soapui.MockService( (resourceDirectory in Test).value / "EDListeReceptionsFournisseur-soapui-project.xml", "9003"),
        soapui.MockService( (resourceDirectory in Test).value / "EDLoginReception-soapui-project.xml", "9005"),
        soapui.MockService( (resourceDirectory in Test).value / "EDReceptionCommande-soapui-project.xml", "9007"),
        soapui.MockService( (resourceDirectory in Test).value / "EDValidationCodeOIA-soapui-project.xml", "9009")
      )
    )
    // end SOAP UI settings 

    .settings(
      test in jacoco.Config <<= (test in jacoco.Config) dependsOn (soapui.mock in Test)//,
    )

  )

}