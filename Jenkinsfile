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
                // Removido o "clean" para que a pasta target/ com os relatórios não seja apagada
                sh 'mvn verify sonar:sonar -Dmaven.test.skip -Dsonar.projectKey=cicd -Dsonar.projectName=\'cicd\' -Dsonar.host.url=${SONAR_PATH} -Dsonar.token=${SONAR_TOKEN}'
            }
        }
    }

    post {
        always {
            script {
                node {
                    // O node fornece o contexto de Launcher necessário para o JUnit e JaCoCo
                    // E agora os arquivos estarão lá, pois o "mvn clean" foi removido do stage anterior
                    junit '**/target/surefire-reports/*.xml'

                    recordCoverage(
                        tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                        sourceCodeRetention: 'LAST_BUILD'
                    )
                }
            }
        }
    }
}