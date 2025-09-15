@echo off
echo ========================================
echo  MICROSERVICIO ESTUDIANTES - PRUEBAS
echo ========================================
echo.

echo [1/6] Limpiando proyecto...
call mvn clean
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en la limpieza del proyecto
    pause
    exit /b 1
)

echo.
echo [2/6] Compilando proyecto...
call mvn compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en la compilacion
    pause
    exit /b 1
)

echo.
echo [3/6] Ejecutando pruebas unitarias...
call mvn test
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en las pruebas unitarias
    pause
    exit /b 1
)

echo.
echo [4/6] Ejecutando pruebas parametrizadas...
call mvn test -Dtest="**/*ParameterizedTest"
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en las pruebas parametrizadas
    pause
    exit /b 1
)

echo.
echo [5/6] Generando reporte de cobertura JaCoCo...
call mvn jacoco:report
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en la generacion del reporte JaCoCo
    pause
    exit /b 1
)

echo.
echo [6/6] Verificando cobertura minima...
call mvn jacoco:check
if %ERRORLEVEL% neq 0 (
    echo ADVERTENCIA: La cobertura no cumple con el minimo requerido
)

echo.
echo ========================================
echo           PRUEBAS COMPLETADAS
echo ========================================
echo.
echo Reportes generados:
echo - JaCoCo HTML: target\site\jacoco\index.html
echo - JaCoCo XML:  target\site\jacoco\jacoco.xml
echo - Surefire:    target\surefire-reports\
echo.

set /p OPEN_REPORT="¿Deseas abrir el reporte de cobertura? (s/n): "
if /i "%OPEN_REPORT%"=="s" (
    echo 🌐 Abriendo reporte JaCoCo...
    start "" "target\site\jacoco\index.html"
)

echo.
echo Comandos adicionales disponibles:
echo - generate-coverage-report.bat  : Regenerar reporte de cobertura
echo - open-jacoco-report.bat       : Abrir reporte existente
echo - run-sonar.bat TU_TOKEN       : Ejecutar analisis SonarQube
echo.
pause