@Library('Jenkins_common_library') _
def COLOR_MAP = [
    'FAILURE' : 'danger',
    'SUCCESS' : 'good'
]

pipeline{
    agent any
    parameters {
        choice(name: 'env_action', choices: 'deploy\ndelete', description: 'Select deploy or rollback.')
        
        string(name: 'env_repo_url', defaultValue: 'https://github.com/Saksuriyas/poc-app-youtube.git', description: 'Git Repository URL.')
        string(name: 'env_branch_name', defaultValue: 'main', description: 'Branch Name')

        string(name: 'env_user_name', defaultValue: 'saksuriyas', description: 'Docker Hub Username')
        string(name: 'env_project_name', defaultValue: 'youtube', description: 'Docker Project/Image Name')
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
                checkoutGit('$env_repo_url', '$env_branch_name')
            }
        }
        stage('sonarqube Analysis'){
        when { expression { params.env_action == 'deploy'}}    
            steps{                
                script{
                    sonarqubeAnalysis('$env_project_name')
                }
            }
        }
        stage('sonarqube QualitGate'){
        when { expression { params.env_action == 'deploy'}}    
            steps{
                script{
                    def credentialsId = 'sonar-token'
                    qualityGate(credentialsId)
                }
            }
        }
        stage('Npm Install'){
        when { expression { params.env_action == 'deploy'}}    
            steps{
                npmInstall()
            }
        }
        stage('Trivy file scan'){
        when { expression { params.env_action == 'deploy'}}    
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
        when { expression { params.env_action == 'deploy'}}    
            steps{
                script{
                   dockerBuild()
                }
            }
        }
        stage('Trivy iamge'){
        when { expression { params.env_action == 'deploy'}}    
            steps{
                trivyImage()
            }
        }
        stage('Run container'){
        when { expression { params.env_action == 'deploy'}}    
            steps{
				script{
				   runContainer()
                }
                
            }
        }
        stage('Remove container'){
        when { expression { params.env_action == 'rollback'}}    
            steps{
				script{         
				   removeContainer()
                }                
            }
        }
        stage('Kube deploy'){
        when { expression { params.env_action == 'deploy'}}    
            steps{
                kubeDeploy()
            }
        }
        stage('Kube deleter'){
        when { expression { params.env_action == 'rollback'}}    
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
