pipeline {
    agent any

    tools {
        maven 'Maven 3.9.x'
        jdk 'JDK 17'
    }

    environment {
        JAVA_HOME = tool 'JDK 17'
        PATH = "$JAVA_HOME/bin:$PATH"
    }

    stages {
        stage('Checkout') {
            steps {
                git clone --single-branch --branch main https://github.com/danielkrokovsky/teste_cicd.git
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Archive artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker build') {
            when {
                expression { fileExists('Dockerfile') }
            }
            steps {
                script {
                    def imageName = "${env.JOB_NAME}:${env.BUILD_NUMBER}"
                    sh "docker build -t ${imageName} ."
                    echo "Imagem Docker gerada: ${imageName}"
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizada.'
        }
        success {
            echo 'Build e testes concluídos com sucesso.'
        }
        failure {
            echo 'Pipeline falhou. Verifique os logs.'
        }
    }
}
