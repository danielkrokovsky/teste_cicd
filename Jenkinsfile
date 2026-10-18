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

    stage('Build') {
        steps {
            // Compila, executa testes e gera target/site/jacoco/jacoco.xml
          sh 'mvn -B clean test jacoco:report -Pno-containers'
        }
    }

   stage('SonarQube Analysis') {
        steps {
            // 'SonarQube' é o nome exato configurado em Manage Jenkins > System
            withSonarQubeEnv('SonarQube') {
                sh 'mvn -DskipTests org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=cicd -Dsonar.projectName=cicd -Dsonar.host.url=http://sonarqube:9000 -Dsonar.token=sqp_aa0f8dbd7b43711883eaccb262de088b3b0e1d71'
            }
        }
    }

    stage('Dependency Check') {
        steps {
            sh 'mvn dependency-check:check'
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

    stage('Package') {
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