@Library('Jenkins_common_library') _
def COLOR_MAP = [
    'FAILURE' : 'danger',
    'SUCCESS' : 'good'
]

pipeline{
    agent any
    parameters {
        choice(name: 'ACTION', choices: 'deploy\ndelete', description: 'Select deploy or rollback.')
        
        string(name: 'GIT_URL', defaultValue: 'https://github.com/Saksuriyas/poc-app-youtube.git', description: 'Git Repository URL.')
        string(name: 'BRANCE_NAME', defaultValue: 'main', description: 'Branch Name')
        string(name: 'USER_NAME', defaultValue: 'saksuriyas', description: 'Docker Hub Username')
        string(name: 'REPO_NAME', defaultValue: 'youtube', description: 'Docker Project/Image Name')
        string(name: 'EMAILS', defaultValue: 'saksuriya@dtac.co.th', description: 'Emails for approve & Pipeline result')
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
                checkoutGit()
            }
        }
        stage('sonarqube Analysis'){
        when { expression { params.ACTION == 'deploy'}}    
            steps{                
                script{
                    sonarqubeAnalysis()
                }
            }
        }
        stage('sonarqube QualitGate'){
        when { expression { params.ACTION == 'deploy'}}    
            steps{
                script{
                    def credentialsId = 'sonar-token'
                    qualityGate(credentialsId)
                }
            }
        }
        stage('Npm Install'){
        when { expression { params.ACTION == 'deploy'}}    
            steps{
                npmInstall()
            }
        }
        stage('Trivy file scan'){
        when { expression { params.ACTION == 'deploy'}}    
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
        when { expression { params.ACTION == 'deploy'}}    
            steps{
                script{
                   dockerBuild()
                }
            }
        }
        stage('Trivy iamge'){
        when { expression { params.ACTION == 'deploy'}}    
            steps{
                trivyImage()
            }
        }
        stage('Run container'){
        when { expression { params.ACTION == 'deploy'}}    
            steps{
				script{
				   runContainer()
                }
                
            }
        }
        stage('Remove container'){
        when { expression { params.ACTION == 'rollback'}}    
            steps{
				script{         
				   removeContainer()
                }                
            }
        }
        stage('Kube deploy'){
        when { expression { params.ACTION == 'deploy'}}    
            steps{
                kubeDeploy()
            }
        }
        stage('Kube deleter'){
        when { expression { params.ACTION == 'rollback'}}    
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
