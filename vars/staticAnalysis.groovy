def call(Map params = [:]) {
    // Parámetros con valores por defecto
    boolean failOnQualityGate = params.get('failOnQualityGate', false)
    boolean abortPipeline = params.get('abortPipeline', false)

    String branchName = params.get('branchName', env.BRANCH_NAME)

    boolean qualityGateStatus = params.get('qualityGateStatus', false) //simula falla o continuidad

    echo "[staticAnalysis] Ejecutando análisis estático de código -  rama '${branchName}'..."

    try {

        // Esperar al Quality Gate (hasta 5 segundos)
        timeout(time: 5, unit: 'SECONDS') {

            echo "[staticAnalysis] Ejecución de las pruebas de calidad de código..."

            if(failOnQualityGate) {
                error("[staticAnalysis] Abortando luego del QualityGate por configuración de 'failOnQualityGate'.")
            }
            else {
                if(abortPipeline) {
                    echo "[staticAnalysis] Quality Gate exitoso para la rama '${branchName}'."
                }
                else {
                    if(qualityGateStatus) {
                        echo "[staticAnalysis] Quality Gate EXITOSO"
                    }
                    else {
                        echo "[staticAnalysis] Quality Gate FALLIDO"
                        
                        if (abortPipeline) {
                            error("[staticAnalysis] Abortando pipiline por configuración de 'abortPipeline'.")
                        }
                        else {
                            echo "[staticAnalysis] No se requiere abortar el pipeline."
                        }
                    }

                }
            }

        }

    } catch (err) {
        echo "[staticAnalysis] Error durante el análisis estático: ${err}"
        throw err
    }

    echo "[Call] Análisis estático finalizado correctamente."
}