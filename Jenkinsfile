@Library('Jenkins_common_library') _
def COLOR_MAP = [
    'FAILURE' : 'danger',
    'SUCCESS' : 'good'
]

pipeline{
    agent any
    parameters {
        choice(name: 'action', choices: 'create\ndelete', description: 'Select create or destroy.')
        
        string(name: 'DOCKER_HUB_USERNAME', defaultValue: 'saksuriyas', description: 'Docker Hub Username')
        string(name: 'IMAGE_NAME', defaultValue: 'youtube', description: 'Docker Image Name')
    }
    tools{
        jdk 'jdk17'
        nodejs 'node18'
    }
    environment {
        SCANNER_HOME=tool 'sonar-scanner'
    }
    stages{
        stage('clean workspace'){
            steps{
                cleanWorkspace()
            }
        }
        stage('checkout from Git'){
            steps{
                checkoutGit('https://github.com/Saksuriyas/poc-app-youtube.git', 'main')
            }
        }
        stage('sonarqube Analysis'){
        when { expression { params.action == 'create'}}    
            steps{                
                script{
		    println(env.DOCKER_HUB_USERNAME)
                    println(env.IMAGE_NAME)
                    sonarqubeAnalysis()
                }
            }
        }
        stage('sonarqube QualitGate'){
        when { expression { params.action == 'create'}}    
            steps{
                script{
                    def credentialsId = 'sonar-token'
                    qualityGate(credentialsId)
                }
            }
        }
        stage('Npm Install'){
        when { expression { params.action == 'create'}}    
            steps{
                npmInstall()
            }
        }
        stage('Trivy file scan'){
        when { expression { params.action == 'create'}}    
            steps{
                trivyFs()
            }
        }
        stage('OWASP FS SCAN') {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-Check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage('Docker Build'){
        when { expression { params.action == 'create'}}    
            steps{
                script{
                   dockerBuild()
                }
            }
        }
        stage('Trivy iamge'){
        when { expression { params.action == 'create'}}    
            steps{
                trivyImage()
            }
        }
        stage('Run container'){
        when { expression { params.action == 'create'}}    
            steps{
				script{
				   runContainer()
                }
                
            }
        }
        stage('Remove container'){
        when { expression { params.action == 'delete'}}    
            steps{
				script{         
				   removeContainer()
                }                
            }
        }
        stage('Kube deploy'){
        when { expression { params.action == '*create'}}    
            steps{
                kubeDeploy()
            }
        }
        stage('Kube deleter'){
        when { expression { params.action == '*delete'}}    
            steps{
                kubeDelete()
            }
        }
    }
    post {
        always {
            echo 'Slack Notifications'
            slackSend (
                channel: '#jenkins',
                color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} \n build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
            )
        }
    }
}
