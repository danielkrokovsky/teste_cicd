pipeline {
    agent {
        docker {
            image 'maven:3.10.0-eclipse-temurin-21-alpine'
        }
    }

    stages {
        stage('Test') {
            steps {
                // Removido o -Dmaven.test.skip para que os testes rodem e gerem os relatórios
                sh 'mvn clean package'
            }
        }

        stage('Sonar Report') {
            steps {
                // Aqui o maven.test.skip faz sentido para não rodar os testes de novo apenas para o Sonar,
                // já que eles rodaram no stage anterior. Os relatórios já estarão salvos.
                sh 'mvn verify sonar:sonar -Dmaven.test.skip -Dsonar.projectKey=cicd -Dsonar.projectName=\'cicd\' -Dsonar.host.url=${SONAR_PATH} -Dsonar.token=${SONAR_TOKEN}'
            }
        }
    }

    post {
        always {
            script {
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