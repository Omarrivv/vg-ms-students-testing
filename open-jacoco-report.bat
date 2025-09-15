@echo off
echo ========================================
echo    ABRIR REPORTE DE COBERTURA JACOCO
echo ========================================
echo.

set JACOCO_REPORT=target\site\jacoco\index.html

if exist "%JACOCO_REPORT%" (
    echo ✅ Reporte JaCoCo encontrado
    echo 📊 Abriendo reporte en el navegador...
    echo.
    echo Ubicacion: %CD%\%JACOCO_REPORT%
    echo.
    
    REM Abrir en el navegador predeterminado
    start "" "%JACOCO_REPORT%"
    
    echo ✅ Reporte abierto exitosamente!
    echo.
    echo En el reporte podras ver:
    echo - 📈 Cobertura general del proyecto
    echo - 📊 Cobertura por paquetes
    echo - 📋 Cobertura por clases
    echo - 🔍 Lineas cubiertas y no cubiertas
    echo.
    echo Metricas importantes:
    echo - Instructions: Cobertura de instrucciones bytecode
    echo - Branches: Cobertura de ramas condicionales  
    echo - Lines: Cobertura de lineas de codigo
    echo - Methods: Cobertura de metodos
    echo - Classes: Cobertura de clases
    echo.
) else (
    echo ❌ Reporte JaCoCo no encontrado
    echo.
    echo Para generar el reporte ejecuta:
    echo 1. mvn clean test jacoco:report
    echo 2. O ejecuta: run-tests.bat
    echo.
    echo El reporte se generara en: %JACOCO_REPORT%
)

echo.
pause