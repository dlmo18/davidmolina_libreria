@Library('threepointssharedlib') _

pipeline {
    agent any

    stages {
        stage('Analisis de código') {
            steps {
                echo 'Iniciando...'
                script {
                    staticAnalysis(
                        failOnQualityGate: true,
                        abortPipeline: false
                    )
                }
            }
        }
    }
}