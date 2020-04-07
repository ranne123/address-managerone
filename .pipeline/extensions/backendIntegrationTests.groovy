void call(Map params) {
  //access stage name
  echo "Start - Extension for stage: ${params.stageName}"

  //access config
  echo "Current stage config: ${params.config}"
   
   //execute mvn clean install 
   sh 'mvn clean install'
   
  //execute original stage as defined in the template
  params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this
