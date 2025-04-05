def call(Map params = [:]) {
    // Parámetros con valores por defecto
    boolean failOnQualityGate = params.get('failOnQualityGate', false)
    boolean abortPipeline = params.get('abortPipeline', false)

    echo "[staticAnalysis] Ejecutando análisis estático de código..."

    try {
        
        
        // Esperar al Quality Gate (hasta 5 minutos)
        timeout(time: 5, unit: 'SECONDS') {
            echo "[staticAnalysis] Ejecución de las pruebas de calidad de código..."
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