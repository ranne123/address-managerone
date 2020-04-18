import com.cloudbees.groovy.cps.NonCPS
import com.sap.cloud.sdk.s4hana.pipeline.BashUtils
import com.sap.cloud.sdk.s4hana.pipeline.BuildToolEnvironment
import com.sap.cloud.sdk.s4hana.pipeline.PathUtils
import com.sap.piper.ConfigurationLoader
import com.sap.piper.ConfigurationMerger
import com.sap.piper.Utils
import com.sap.cloud.sdk.s4hana.pipeline.MavenUtils
void call(Map params) {
 
 sh  "printenv"

  //print envrs variables
 //echo "print the executing directory "
 println env.WORKSPACE
 
 echo "printing job bame  "
 println env.JOB_NAME
// def env = System.getenv()
//env.each{
//println it
//} 


  //access stage name
  echo "Start rama - Extension for stage: ${params.stageName}"

  //access config
  echo "Current stage config: ${params.config}"
   
 
  //install assemblies in jenkins local repo
  //echo "generate the  assemblies - integration-tests *******"
 // mavenExecute script: this, goals: 'install -pl !integration-tests '
 
 

 //String pathToTargetDirectory = PathUtils.normalize(${PWD}, '/target')
// echo "the app path is $pathToApplication "
  
 //println "Artifact version is ${version}"
  //install the assemblies into local maven repository (Docker based )
 // echo "installing the assemblies ..into local repo from unit-tests"
// echo "installing the assemblies ..into local repo"
// def localWorkspace = env.WORKSPACE
// def integrationFile = PathUtils.normalize(env.WORKSPACE,"target/address-manager-rama123-integrationtest.jar")
// println integrationFile
// echo "file is ${integrationFile}"
 //File artifactIntFile = new File("${localworkspace}/target/address-manager-rama123-integrationtest.jar")
//  mavenExecute script: this, goals: 'org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile=' + "${integrationFile}" +  ' -Dversion=rama123  -DgroupId=com.sap.cloud.s4hana.examples         -DartifactId=address-manager-integration-tests -Dversion=rama123 -Dpackaging=jar -Dclassifier=integrationtest'
 
// def applicationFile = PathUtils.normalize(env.WORKSPACE,"/application/target/address-manager-application-applicationclasses.jar")
// println applicationFile
  //install the aaplication jar into local repo
// mavenExecute script: this, goals: 'org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile='+ "${applicationFile}" + ' -Dversion=rama123  -DgroupId=com.sap.cloud.s4hana.examples -DartifactId=address-manager-application -Dversion=rama123 -Dpackaging=jar -Dclassifier=applicationclasses'
  
 //  def unittestFile = PathUtils.normalize(env.WORKSPACE,"target/address-manager-rama123-unittest.jar")
 //println unittestFile
  //install the unit-test jar into local repo
//mavenExecute script: this, goals: 'org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile=' + "${unittestFile}" + ' -Dversion=rama123  -DgroupId=com.sap.cloud.s4hana.examples   -DartifactId=unit-tests -Dversion=rama123 -Dpackaging=jar -Dclassifier=unittest'
  
   mavenExecute script: this, goals: '-pl !testing -Dskip.unit-tests=true -Dskip-integration-tests=true'
  
  //execute original stage as defined in the template
 params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this


  
  
  
 

  
  

