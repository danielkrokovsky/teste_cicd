pipeline {
    agent {
        dockerfile true
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
                // Publica os relatórios de testes unitários com segurança
                junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

                // Registra a cobertura do JaCoCo tratando eventuais falhas anteriores
                try {
                    recordCoverage(
                        tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                        sourceCodeRetention: 'LAST_BUILD',
                        failedThresholds: [unstableTotalPercentage: [minimum: 0]]
                    )
                } catch (Exception e) {
                    println("Aviso: Não foi possível registrar a cobertura do JaCoCo: ${e.getMessage()}")
                }
            }
        }
    }
}