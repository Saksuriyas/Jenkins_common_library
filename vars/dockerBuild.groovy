def call() {
    sh "docker build -t $REPO_NAME ."
    sh "docker tag $REPO_NAME $USER_NAME/$REPO_NAME:latest"
    withDockerRegistry([url: 'https://index.docker.io/v1/', credentialsId: 'docker']) {
        sh "docker push $USER_NAME/$REPO_NAME:latest"
    }
}