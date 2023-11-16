def call(){
    sh 'docker stop $IMAGE_NAME'
    sh 'docker rm $IMAGE_NAME'
}