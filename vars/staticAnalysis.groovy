def call(Map params = [:]) {
    // Parámetros con valores por defecto
    boolean failOnQualityGate = params.get('failOnQualityGate', false)
    boolean abortPipeline = params.get('abortPipeline', false)

    String branchName = params.get('branchName', env.BRANCH_NAME)

    echo "[staticAnalysis] Ejecutando análisis estático de código -  rama '${branchName}'..."

    try {
        withSonarQubeEnv('SonarQubeServer') { // SonarQube en Jenkins
            sh "sonar-scanner"
        }

        // Esperar al Quality Gate (hasta 5 minutos)
        timeout(time: 5, unit: 'MINUTES') {
            def qg = waitForQualityGate()

            echo "[staticAnalysis] Ejecución de las pruebas de calidad de código..."
            if (qg.status != 'OK') {
                echo "[staticAnalysis] Quality Gate FALLIDO: ${qg.status}"
                            
                if (abortPipeline) {
                    error("[staticAnalysis] Abortando pipeline por configuración de 'abortPipeline'.")
                }
                else {                        
                    //evaluando ramas
                    if( branchName=='main' || branchName=='hotfix' ) {
                        error("[staticAnalysis] Abortando pipeline ser rama '${branchName}'.")
                    }
                    else {
                        echo "[staticAnalysis] No se requiere abortar el pipeline."
                    }
                }           
            } else {
                echo "[staticAnalysis] Quality Gate aprobado EXITOSAMENTE."                
            }

            if(failOnQualityGate) {
                error("[staticAnalysis] Abortando por configuración de 'failOnQualityGate'.")
            }

        }

    } catch (err) {
        error("[staticAnalysis] Error durante el análisis estático: ${err}")
        throw err
    }

    echo "[Call] Análisis estático finalizado correctamente."
}