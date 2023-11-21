def call(){
    sh "docker run -d --name $REPO_NAME -p 3000:3000 $USER_NAME/$REPO_NAME:latest"
}