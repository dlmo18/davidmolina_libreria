@Library('threepointssharedlib') _

pipeline {
    agent any

    stages {
        stage('Analisis de códico') {
            steps {
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