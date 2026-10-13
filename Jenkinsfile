pipeline {
    agent any

    // Ferramentas configuradas globalmente no Jenkins (Global Tool Configuration)
    tools {
        maven 'Maven 3.9.x' // Substitua pelo nome exato configurado no seu Jenkins
        jdk 'JDK 17'        // Substitua pela versão do JDK configurada no seu Jenkins
    }

    // Variáveis de ambiente úteis
    environment {
        SCANNER_HOME = tool 'SonarQubeScanner' // Opcional: para análise estática
    }

    stages {
        stage('Checkout') {
            steps {
                // Faz o checkout do código fonte do repositório Git
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                // Executa o empacotamento Maven ignorando testes se necessário, ou rodando tudo
                sh 'mvn clean package'
            }
            post {
                success {
                    // Publica os resultados dos testes unitários (JUnit) no Jenkins
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Tests') {
            steps {
                // Executa testes de integração (caso o projeto possua o plugin maven-failsafe-plugin)
                sh 'mvn verify'
            }
            post {
                success {
                    // Publica os resultados dos testes de integração
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }

        /* 
        stage('SonarQube Analysis') {
            steps {
                // Etapa opcional para análise de qualidade de código
                withSonarQubeEnv('MeuServidorSonar') {
                    sh "mvn sonar:sonar"
                }
            }
        }
        */

        stage('Archive Artifacts') {
            steps {
                // Salva o arquivo .jar ou .war gerado no diretório target
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizada.'
        }
        success {
            echo 'Sucesso! Projeto compilado, testado e empacotado com sucesso.'
            // Aqui você pode adicionar notificações (ex: Slack, Email)
        }
        failure {
            echo 'Falha na pipeline. Verifique os logs acima.'
        }
    }
}