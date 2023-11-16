def call() {
    sh "docker build -t $IMAGE_NAME ."
    sh "docker tag $IMAGE_NAME $DOCKER_HUB_USERNAME/$IMAGE_NAME:latest"
    withDockerRegistry([url: 'https://index.docker.io/v1/', credentialsId: 'docker']) {
        sh "docker push $DOCKER_HUB_USERNAME/$IMAGE_NAME:latest"
    }
}