pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-21'
            // Socket do Docker necessário para o Testcontainers subir o PostgreSQL
            args '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.m2:/root/.m2 --group-add 0'
        }
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=/root/.m2/repository'
        TESTCONTAINERS_RYUK_DISABLED = 'true'
        UNIT_TESTS = '!*IntegrationTest,!OficinaApplicationTests'
        IT_TESTS = '*IntegrationTest,OficinaApplicationTests'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean compile test-compile'
            }
        }

        stage('Testes Unitários') {
            steps {
                sh 'mvn -B jacoco:prepare-agent surefire:test -Dtest="${UNIT_TESTS}" -Dsurefire.failIfNoSpecifiedTests=false'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Testes de Integração (Testcontainers)') {
            steps {
                sh 'docker version || echo "Aviso: Docker indisponível para o Testcontainers"'
                sh 'mvn -B jacoco:prepare-agent surefire:test -Djacoco.append=true -Dtest="${IT_TESTS}" -Dsurefire.failIfNoSpecifiedTests=false'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Análise JaCoCo') {
            steps {
                sh 'mvn -B jacoco:report jacoco:check@check'
            }
            post {
                always {
                    script {
                        try {
                            recordCoverage(
                                tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                                sourceCodeRetention: 'LAST_BUILD',
                                qualityGates: [[threshold: 80.0, metric: 'LINE', baseline: 'PROJECT', criticality: 'UNSTABLE']]
                            )
                        } catch (Exception e) {
                            echo "Aviso: não foi possível registrar a cobertura do JaCoCo: ${e.getMessage()}"
                        }
                    }
                    archiveArtifacts allowEmptyArchive: true, artifacts: 'target/site/jacoco/**'
                }
            }
        }

        stage('SonarQube') {
            steps {
                sh '''
                    mvn -B sonar:sonar \
                        -Dsonar.projectKey=cicd \
                        -Dsonar.projectName=cicd \
                        -Dsonar.host.url=${SONAR_PATH} \
                        -Dsonar.token=${SONAR_TOKEN} \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                '''
            }
        }
    }
}