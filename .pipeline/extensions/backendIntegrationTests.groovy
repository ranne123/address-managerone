void call(Map params) {
  //access stage name
  echo "Start - Extension for stage: ${params.stageName}"

  //access config
  echo "Current stage config: ${params.config}"
   
   //execute mvn clean install 
    //sh '../../jenkins/scripts/deliver.sh'
  echo "Executing the mvn clean install " 
   mavenExecute script: this, goals: 'clean install test'
  
  echo "Executing the maven failsafe plugin ..."
  mavenExecute script: this, goals: 'mvn failsafe:integration-test failsafe:verify'
  //execute original stage as defined in the template
  params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this
