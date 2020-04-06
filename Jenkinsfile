#!/usr/bin/env groovy 

node {
    deleteDir()
    sh "git clone --depth 1 https://github.com/SAP/cloud-s4-sdk-pipeline.git pipelines"
    load './pipelines/s4sdk-pipeline.groovy'
    stage('Build') {

    }
    post {
        always {
            prereq_build = selectRun filter: parameters("TARGET_OS=${TARGET_OS},GIT_BRANCH_NAME=${GIT_BRANCH_NAME}"), job: 'address-manager/master', selector: status('STABLE'), verbose: true
            copyArtifacts(
                    projectName: 'address-manager/master',
                    filter: '**/*.war',
                    fingerprintArtifacts: true,
                    target: '/integration-tests/target',
                    flatten: true,
                    selector: specific(prereq_build.getId())
            )
        }
    }
}


