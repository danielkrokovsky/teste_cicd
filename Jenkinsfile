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
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            // Este comando vai falhar
            sh 'mvn -B clean test jacoco:report -Pno-containers'
        }
    }

   stage('SonarQube Analysis') {
        steps {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -DskipTests org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=cicd -Dsonar.projectName=cicd -Dsonar.host.url=http://sonarqube:9000 -Dsonar.token=sqp_aa0f8dbd7b43711883eaccb262de088b3b0e1d71'
                }
            }
        }
    }

/*
    stage('Dependency Check') {
        steps {
            sh 'mvn dependency-check:check'
        }
    }


    stage('Snyk Test') {
       steps {
           script {
               // Run Snyk test
               withCredentials([string(credentialsId: ${SNYK_TOKEN}, variable: 'Snyk')]) {
                   sh 'snyk test --token=${SNYK_TOKEN}'
               }
           }
       }
   }*/

    stage('Quality Gate') {
        steps {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    stage('Package') {
        steps {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                sh 'mvn package -DskipTests'
            }
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