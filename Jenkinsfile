pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                // Executando via container Docker diretamente no passo sh,
                // garantindo que o workspace e os arquivos fiquem na máquina/diretório correto.
                docker.image('maven:3.10.0-eclipse-temurin-21-alpine').inside {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Sonar Report') {
            steps {
                docker.image('maven:3.10.0-eclipse-temurin-21-alpine').inside {
                    sh 'mvn verify sonar:sonar -Dmaven.test.skip -Dsonar.projectKey=cicd -Dsonar.projectName=\'cicd\' -Dsonar.host.url=${SONAR_PATH} -Dsonar.token=${SONAR_TOKEN}'
                }
            }
        }
    }

    post {
        always {
            // Como estamos usando agent any com .inside nos stages,
            // o post roda nativamente no workspace do nó principal onde os arquivos XML foram salvos.
            script {
                junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

                // Usamos o try/catch ou garantimos que o JaCoCo processe mesmo se houver falhas anteriores
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