def call() {
    sh 'trivy image $DOCKER_HUB_USERNAME/$IMAGE_NAME:latest > trivyimage.txt'
}