pipeline {
    agent {
        docker {
            image 'maven:3.10.0-eclipse-temurin-21-alpine'
        }
    }

    stages {
        stage('Test') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Sonar Report') {
            steps {
                sh 'mvn verify sonar:sonar -Dmaven.test.skip -Dsonar.projectKey=cicd -Dsonar.projectName=\'cicd\' -Dsonar.host.url=${SONAR_PATH} -Dsonar.token=${SONAR_TOKEN}'
            }
        }
    }

    post {
        always {
            script {
                node {
                    // Busca por surefire-reports em qualquer subnível do projeto e evita crash se não achar
                    junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

                    recordCoverage(
                        tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                        sourceCodeRetention: 'LAST_BUILD'
                    )
                }
            }
        }
    }
}