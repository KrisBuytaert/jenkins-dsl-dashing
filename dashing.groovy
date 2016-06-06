
job('dashing/dashing-dashboaord-build') {
    /** require tag docker */
    scm {

        git{ remote { 
            url('https://github.com/KrisBuytaert/build-dashing')
        }
        }
    }
    triggers {
        scm('*/15 * * * *')
    }
    steps {
        shell(readFileFromWorkspace('build-dashboard.sh'))

    }
}

folder('dashing')
