#!/usr/bin/env groovy 

node {
    deleteDir()
    sh "git clone --depth 1 https://github.com/SAP/cloud-s4-sdk-pipeline.git pipelines"
    load './pipelines/s4sdk-pipeline.groovy'
    environment {
buildEnvVar1= 'In the build stage of the project'
backendIntegEnvVar= 'In the backend integration test stage'
thirdEnvVar= 'THIRD_VAR'
}
    stages {
        stage('build') { 
            artifactPrepareVersion script: this, buildTool: 'maven', versioningType: library
            steps {
                echo env.buildEnvVar1 
            }
        }
        stage('backendIntegrationTests'){
             steps {
                echo env.backendIntegEnvVar 
               
            }
        }
    }
}



