@echo off
echo ========================================
echo     ANALISIS SONARQUBE - ESTUDIANTES
echo ========================================
echo.

if "%1"=="" (
    echo ERROR: Debe proporcionar el token de SonarQube
    echo Uso: run-sonar.bat TU_SONAR_TOKEN
    echo.
    echo Para obtener un token:
    echo 1. Ir a https://sonarcloud.io
    echo 2. Login con GitHub
    echo 3. My Account ^> Security ^> Generate Token
    pause
    exit /b 1
)

set SONAR_TOKEN=%1

echo [1/4] Limpiando y compilando...
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en la compilacion
    pause
    exit /b 1
)

echo.
echo [2/4] Ejecutando pruebas con cobertura...
call mvn test jacoco:report
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en las pruebas
    pause
    exit /b 1
)

echo.
echo [3/4] Ejecutando analisis SonarQube...
call mvn sonar:sonar -Dsonar.token=%SONAR_TOKEN%
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo en el analisis SonarQube
    echo Verifique:
    echo - Token valido
    echo - Conexion a internet
    echo - Configuracion del proyecto en SonarCloud
    pause
    exit /b 1
)

echo.
echo [4/4] Analisis completado exitosamente!
echo.
echo ========================================
echo        ANALISIS SONARQUBE COMPLETO
echo ========================================
echo.
echo Resultados disponibles en:
echo https://sonarcloud.io/project/overview?id=vg-ms-students
echo.
echo Metricas principales a revisar:
echo - Bugs: Debe ser 0
echo - Vulnerabilities: Debe ser 0  
echo - Code Smells: Maximo 5
echo - Coverage: Minimo 70%%
echo - Duplications: Maximo 3%%
echo.
pause