def call() {
    withSonarQubeEnv('sonar-server') {
        sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=$REPO_NAME -Dsonar.projectKey=$REPO_NAME '''
    }
}