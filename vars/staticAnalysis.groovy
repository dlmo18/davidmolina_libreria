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

            if(qualityGateStatus) {
                echo "[staticAnalysis] Quality Gate exitoso para la rama '${branchName}'."
            }
            else {
                if (forceFailOnQualityGate) {
                    if( branchName == 'master' || branchName == 'hotfix' ) {
                        error("[staticAnalysis] Abortando: la rama '${branchName}' requiere Quality Gate aprobado.")
                    }
                    else {
                        echo "[staticAnalysis] Quality Gate fallido, pero la rama '${branchName}' no requiere abortar el pipeline."
                    }
                }
                else {
                    echo "[staticAnalysis] Quality Gate fallido, no se requiere abortar el pipeline."
                }
            }

            

        }

    } catch (err) {
        echo "[staticAnalysis] Error durante el análisis estático: ${err}"
        throw err
    }

    // Control final según abortPipeline
    if (abortPipeline) {
        error("[staticAnalysis] abortPipeline=true, abortando pipeline manualmente.")
    }

    echo "[Call] Análisis estático finalizado correctamente."
}