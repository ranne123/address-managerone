import com.cloudbees.groovy.cps.NonCPS
import com.sap.cloud.sdk.s4hana.pipeline.BashUtils
import com.sap.cloud.sdk.s4hana.pipeline.BuildToolEnvironment
import com.sap.cloud.sdk.s4hana.pipeline.PathUtils
import com.sap.piper.ConfigurationLoader
import com.sap.piper.ConfigurationMerger
import com.sap.piper.Utils
import com.sap.cloud.sdk.s4hana.pipeline.MavenUtils
void call(Map params) {
 


  //print envrs variables
 
  def ret = sh (script: "$PWD", returnStatus:true)
  echo "Priniting the env var ret ..."
 println ret
 echo "get the current directory..."
// def DIRVar = binding.getVariables().get('PWD')
 
 
 echo "${PWD}"
 
 
  //access stage name
  echo "Start rama - Extension for stage: ${params.stageName}"

  //access config
  echo "Current stage config: ${params.config}"
   
  //install assemblies in jenkins local repo
  echo "generate the  assemblies - integration-tests *******"
  mavenExecute script: this, goals: 'assembly:single'
 
 

 String pathToTargetDirectory = PathUtils.normalize(${PWD}, '/target')
 echo "the app path is $pathToApplication "
  
println "Artifact version is $version"
  //install the assemblies into local maven repository (Docker based )
  echo "installing the assemblies ..into local repo"
   mavenExecute script: this, goals: 'org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile= ${project.build.directory}/address-manager-1.0-SNAPSHOT-integrationtest.jar  -Dversion=1.0-SNAPSHOT  -DgroupId=com.sap.cloud.s4hana.examples         -DartifactId=address-manager-integration-tests -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dclassifier=integrationtest'
 
  //install the aaplication jar into local repo
 mavenExecute script: this, goals: 'org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile= ${project.build.directory}/../application/target/address-manager-application-applicationclasses.jar -Dversion=1.0-SNAPSHOT  -DgroupId=com.sap.cloud.s4hana.examples -DartifactId=address-manager-application -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dclassifier=applicationclasses'
  
  
  //install the unit-test jar into local repo
 mavenExecute script: this, goals: 'org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile= ${project.build.directory}/address-manager-1.0-SNAPSHOT-unittest.jar -Dversion=1.0-SNAPSHOT  -DgroupId=com.sap.cloud.s4hana.examples   -DartifactId=unit-tests -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dclassifier=unittest'
  
  
  
  //execute original stage as defined in the template
 params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this
