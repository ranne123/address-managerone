void call(Map params) {
  //access stage name
  echo "Start rama - Extension for stage: ${params.stageName}"

  //access config
  echo "Current stage config: ${params.config}"
   
  //install assemblies in jenkins local repo
  evho "install assemblies - integration-tests *******"
  mavenExecute script: this, goals: 'clean assembly:single'
  //execute original stage as defined in the template
 params.originalStage()

  

  echo "End - Extension for stage: ${params.stageName}"
}
return this
