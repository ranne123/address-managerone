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
 echo "print the executing directory "
 println env.PWD
 
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
  echo "generate the  assemblies - integration-tests *******"
  mavenExecute script: this, goals: 'assembly:single'
 
 

 //String pathToTargetDirectory = PathUtils.normalize(${PWD}, '/target')
// echo "the app path is $pathToApplication "
  
 //println "Artifact version is ${version}"
  //install the assemblies into local maven repository (Docker based )
  echo "installing the assemblies ..into local repo"
 MavenUtils.installMavenArtifacts(script, '$PWD/address-manager')
 //mavenutils.installArtifact('address-manager/application')
 //mavenutils.installArtifact('address-manager/integration-tests')
  
  
  //execute original stage as defined in the template
 params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this
