@echo off
echo ========================================
echo   GENERAR REPORTE DE COBERTURA JACOCO
echo ========================================
echo.

echo [1/4] Limpiando proyecto anterior...
call mvn clean -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Error en la limpieza
    pause
    exit /b 1
)

echo [2/4] Compilando proyecto...
call mvn compile -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Error en la compilacion
    pause
    exit /b 1
)

echo [3/4] Ejecutando pruebas (ignorando fallos)...
call mvn test -q
REM Continuamos aunque haya fallos en las pruebas

echo [4/4] Generando reporte de cobertura...
call mvn jacoco:report -q
if %ERRORLEVEL% neq 0 (
    echo ❌ Error generando reporte JaCoCo
    pause
    exit /b 1
)

echo.
echo ========================================
echo     REPORTE GENERADO EXITOSAMENTE
echo ========================================
echo.

set JACOCO_REPORT=target\site\jacoco\index.html

if exist "%JACOCO_REPORT%" (
    echo ✅ Reporte disponible en: %CD%\%JACOCO_REPORT%
    echo.
    
    REM Mostrar resumen basico del reporte
    echo 📊 RESUMEN DE COBERTURA:
    echo ========================
    
    REM Buscar informacion basica en el CSV
    if exist "target\site\jacoco\jacoco.csv" (
        echo Archivo CSV generado: target\site\jacoco\jacoco.csv
        echo Archivo XML generado: target\site\jacoco\jacoco.xml
        echo Archivo HTML generado: target\site\jacoco\index.html
    )
    
    echo.
    set /p OPEN_REPORT="¿Deseas abrir el reporte en el navegador? (s/n): "
    if /i "%OPEN_REPORT%"=="s" (
        echo 🌐 Abriendo reporte en el navegador...
        start "" "%JACOCO_REPORT%"
        echo ✅ Reporte abierto!
    )
    
    echo.
    echo 📋 ARCHIVOS GENERADOS:
    echo ======================
    echo - HTML Report: target\site\jacoco\index.html
    echo - XML Report:  target\site\jacoco\jacoco.xml  
    echo - CSV Report:  target\site\jacoco\jacoco.csv
    echo - Sessions:    target\site\jacoco\jacoco-sessions.html
    echo.
    echo 🎯 COMO INTERPRETAR EL REPORTE:
    echo ================================
    echo - Verde: Cobertura alta (^>80%%)
    echo - Amarillo: Cobertura media (60-80%%)
    echo - Rojo: Cobertura baja (^<60%%)
    echo.
    echo - Instructions: Instrucciones bytecode ejecutadas
    echo - Branches: Ramas condicionales (if/else) cubiertas
    echo - Lines: Lineas de codigo ejecutadas
    echo - Methods: Metodos invocados
    echo - Classes: Clases utilizadas
    echo.
    echo 💡 CONSEJOS:
    echo =============
    echo - Haz clic en los paquetes para ver detalles
    echo - Haz clic en las clases para ver lineas especificas
    echo - Las lineas rojas no estan cubiertas por pruebas
    echo - Las lineas verdes estan completamente cubiertas
    echo - Las lineas amarillas estan parcialmente cubiertas
    
) else (
    echo ❌ Error: No se pudo generar el reporte
    echo Verifica que las pruebas se ejecuten correctamente
)

echo.
pause