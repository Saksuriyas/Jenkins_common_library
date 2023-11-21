def call() {
    sh 'trivy image $USER_NAME/$REPO_NAME:latest > trivyimage.txt'
}