pipeline {
    agent any

    tools {
        // Nomes exatos configurados em Manage Jenkins > Tools
        jdk 'JDK-21'
        maven 'maven'
    }

    options {
        // Limpa o workspace e define tempo limite de execução
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verificar Ambiente') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Build & Testes') {
            steps {
                // Executa o ciclo de build do Maven
                sh 'mvn clean verify'
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
            // Publica relatórios de testes do JUnit no dashboard do Jenkins
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
        success {
            // Guarda o .jar gerado para download
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            echo 'Build executado com sucesso!'
        }
        failure {
            echo 'Falha no build ou nos testes automatizados.'
        }
    }
}