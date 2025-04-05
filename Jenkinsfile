@Library('threepointssharedlib') _

pipeline {
    agent any

    stages {
        stage('Analisis de código') {
            steps {
                echo 'Iniciando...'
                script {
                    staticAnalysis(
                        failOnQualityGate: false,
                        abortPipeline: true,
                        qualityGateStatus: false //simula falla de Gate
                    )
                }
            }
        }
    }
}