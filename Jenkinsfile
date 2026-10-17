pipeline {
    // Removemos o agent global e definiremos por stage para manter o contexto no post
    agent none

    stages {
        stage('Test') {
            agent {
                docker {
                    image 'maven:3.10.0-eclipse-temurin-21-alpine'
                }
            }
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Sonar Report') {
            agent {
                docker {
                    image 'maven:3.10.0-eclipse-temurin-21-alpine'
                }
            }
            steps {
                sh 'mvn verify sonar:sonar -Dmaven.test.skip -Dsonar.projectKey=cicd -Dsonar.projectName=\'cicd\' -Dsonar.host.url=${SONAR_PATH} -Dsonar.token=${SONAR_TOKEN}'
            }
        }
    }

    post {
        always {
            script {
                // Como os stages usaram agent Docker, o post herdará o workspace correto
                // sem perder o contexto do Launcher.
                junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

                recordCoverage(
                    tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                    sourceCodeRetention: 'LAST_BUILD'
                )
            }
        }
    }
}