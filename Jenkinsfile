pipeline {
    agent any

    tools {
        maven 'Maven 3.9.x'
        jdk 'JDK 21'
    }

    environment {
        JAVA_HOME = tool 'JDK 17'
        PATH = "$JAVA_HOME/bin:$PATH"
    }

    agent {
            docker {
                image 'maven:3.9.6-eclipse-temurin-17'
                // Monta o socket do Docker do host para permitir o Testcontainers
                args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
            }
        }

    stages {

        stage('Test with Testcontainers') {
                steps {
                    // Testcontainers se comunica diretamente com o Docker do host via socket
                    sh 'mvn clean test'
                }
                post {
                    always {
                        junit '**/target/surefire-reports/*.xml'
                    }
                }
            }

        stage('Checkout') {
            steps {
                git clone --single-branch --branch main https://github.com/danielkrokovsky/teste_cicd.git
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

        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Archive artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
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
