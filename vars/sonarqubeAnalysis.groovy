def call() {
    withSonarQubeEnv('sonar-server') {
        sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=$IMAGE_NAME -Dsonar.projectKey=$IMAGE_NAME '''
    }
}