#!/usr/bin/env groovy 

node {
    deleteDir()
    sh "git clone --depth 1 https://github.com/SAP/cloud-s4-sdk-pipeline.git pipelines"
    load './pipelines/s4sdk-pipeline.groovy'
    archiveArtifacts artifacts: 'build/*.war', onlyIfSuccessful: true
    stage('Copy Archive') {
        steps {
            script {
                step ([$class: 'CopyArtifact',
                       projectName: 'address-manager/master',
                       filter:'build/*.war',
                       selector: [$class: 'WorkspaceSelector'],
                       fingerprintArtifacts: true, flatten: true,

                       target: './address-manager/integration-tests/target']);
            }
        }

    }
}