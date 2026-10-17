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
                // Como o agent global é Docker, abrimos um node e garantimos
                // que o contexto de execução e o workspace estejam ativos no pós-build
                node {
                    docker.image('maven:3.10.0-eclipse-temurin-21-alpine').inside {
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
}