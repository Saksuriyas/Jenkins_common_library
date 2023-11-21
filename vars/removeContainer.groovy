def call(){
    sh 'docker stop $REPO_NAME'
    sh 'docker rm $REPO_NAME'
}