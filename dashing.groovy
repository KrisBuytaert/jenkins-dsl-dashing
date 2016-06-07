folder('dashing')

job('dashing/dashing-dashboard-build') {
    scm {

        git{ remote {
            url('https://github.com/KrisBuytaert/build-dashing')
        }
        /** require tag docker */

        }
    }
    triggers {
        scm('*/15 * * * *')
    }
    steps {
        shell(readFileFromWorkspace('dashing/build-dashboard.sh'))

    }
    publishers {
        downstreamParameterized {
            trigger("dashing/dashing-dashboard-test") {
                condition('SUCCESS')
                    parameters {
                        predefinedProp('ITERATION','${BUILD_NUMBER}')
                            predefinedProp('GIT_COMMIT','${GIT_COMMIT}')
                    }


            }
        }
    }

}



job('dashing/dashing-dashboard-test') {
    steps {
        shell(readFileFromWorkspace('dashing/test-dashboard.sh'))
    }
    publishers {
        downstreamParameterized {
          trigger("dashing/dashing-dashboard-deploy") {
                condition('SUCCESS')
                    parameters {
                        predefinedProp('ITERATION','${BUILD_NUMBER}')
                        predefinedProp('GIT_COMMIT','${GIT_COMMIT}')
                    }


            }
        }
    }
}
job('dashing/dashing-dashboard-deploy') {
    steps {
        shell(readFileFromWorkspace('dashing/deploy-dashboard.sh'))
            shell(readFileFromWorkspace('dashing/restart-dashboard.sh'))
    }
}



job('dashing/dashing-build') {

}


buildPipelineView('dashing/dashboard-pipeline') {
    title('Dashing Dashboard Pipeline')
        displayedBuilds(5)
        selectedJob('dashing/dashing-dashboard-build')
        alwaysAllowManualTrigger()
}
