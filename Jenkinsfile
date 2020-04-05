#!/usr/bin/env groovy 

node {
    deleteDir()
    sh "git clone --depth 1 https://github.com/SAP/cloud-s4-sdk-pipeline.git pipelines"
    load './pipelines/s4sdk-pipeline.groovy'
    stage('Copy Archive') {
        steps {
            script {
                step ([$class: 'CopyArtifact',
                       projectName: 'address-manager/master',
                       filter:'build/address-*.war',
                       selector: [$class: 'SpecificBuildSelector', buildNumber: '${BUILD_NUMBER}'],
                       fingerprintArtifacts: true, flatten: true,

                       target: './address-manager/integration-tests/target']);
            }
        }

    }
}