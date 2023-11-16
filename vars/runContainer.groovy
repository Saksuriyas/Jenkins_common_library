def call(){
    sh "docker run -d --name $IMAGE_NAME -p 3000:3000 $DOCKER_HUB_USERNAME/$IMAGE_NAME:latest"
}