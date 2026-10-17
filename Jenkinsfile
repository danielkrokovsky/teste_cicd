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
                // Roda em um node padrão do Jenkins (no workspace onde o projeto foi clonado e compilado)
                // Os plugins do Jenkins (junit e recordCoverage) leem os arquivos do disco diretamente,
                // sem precisar que o Maven ou o Docker estejam ativos nesta etapa.
                node {
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