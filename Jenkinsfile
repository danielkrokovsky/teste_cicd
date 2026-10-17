pipeline {
    agent {
        docker {
            image 'maven:3.10.0-eclipse-temurin-21-alpine'
        }
    }

    stages {
        stage('Test') {
            steps {
                sh 'mvn clean -Dmaven.test.skip package'
            }
        }

        stage('Sonar Report') {
            steps {
                sh 'mvn clean verify sonar:sonar -Dmaven.test.skip -Dsonar.projectKey=cicd -Dsonar.projectName=\'cicd\' -Dsonar.host.url=${SONAR_PATH} -Dsonar.token=${SONAR_TOKEN}'
            }
        }
    }

    post {
        always {
            script {
                node {
                    // Publica os testes unitários JUnit convencionais
                    junit '**/target/surefire-reports/*.xml'

                    // Publica a cobertura do JaCoCo
                    recordCoverage(
                        tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                        sourceCodeRetention: 'LAST_BUILD'
                    )
                }
            }
        }
    }
}