def call(String projectName) {
    withSonarQubeEnv('sonar-server') {
        sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=$projectName -Dsonar.projectKey=$projectName '''
    }
}