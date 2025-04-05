def call(Map params = [:]) {
    // Parámetros con valores por defecto
    boolean failOnQualityGate = params.get('failOnQualityGate', false)
    boolean abortPipeline = params.get('abortPipeline', false)

    echo "[Call] Ejecutando análisis estático de código..."

    try {
        echo "[Call] Ejecución de las pruebas de calidad de código",
        sh "sonar-scanner"

        // Esperar al Quality Gate (hasta 5 minutos)
        timeout(time: 5, unit: 'MINUTES') {
            def qg = waitForQualityGate() //SonarQube Plugin
            if (qg.status != 'OK') {
                echo "[Call] Quality Gate falló: ${qg.status}"

                if (failOnQualityGate) {
                    error("[Call] Abortando debido a Quality Gate fallido.")
                }
            } else {
                echo "[Call] Quality Gate aprobado."
            }
        }

    } catch (err) {
        echo "[Call] Error durante el análisis estático: ${err}"
        throw err
    }

    // Control final según abortPipeline
    if (abortPipeline) {
        error("[Call] abortPipeline=true, abortando pipeline manualmente.")
    }

    echo "[Call] Análisis estático finalizado correctamente."
}