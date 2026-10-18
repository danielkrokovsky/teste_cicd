pipeline {
    agent any

    tools {
        jdk 'JDK-21'
        maven 'Maven' // ou o nome configurado nas suas Tools
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Testes com JaCoCo') {
            steps {
                // Compila, executa testes e gera target/site/jacoco/jacoco.xml
                sh 'mvn clean verify -Dtestcontainers.disabled=true'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // 'SonarQube' é o nome exato configurado em Manage Jenkins > System
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=meu-projeto-java \
                        -Dsonar.projectName='Meu Projeto Java' \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                // Pausa a pipeline até receber o webhook de retorno do SonarQube (timeout 10 min)
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Empacotamento') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            // Relatório de testes unitários no Jenkins
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
        success {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }
}