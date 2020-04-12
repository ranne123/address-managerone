void call(Map params) {
  //access stage name
  echo "Start - Extension for stage: ${params.stageName}"

  //access config
  echo "Current stage config: ${params.config}"
   
 // echo "Resolve dependencies ..."
 // mavenExecute script: this, goals: 'dependency:resolve -Dclassifer=classes'
  //echo "Execute maven verify ..."
  // mavenExecute script: this, goals: 'verify'
   //execute mvn clean install 
    //sh '../../jenkins/scripts/deliver.sh'
  //echo "Executing the mvn clean install " 
 //  mavenExecute script: this, goals: 'clean install -U -X'
  
  echo "Executing the mvn clean verify  ..."
  mavenExecute script: this, goals: 'clean install'
  //execute original stage as defined in the template
 // params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this
