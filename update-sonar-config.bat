@echo off
echo ========================================
echo   ACTUALIZAR CONFIGURACION SONARQUBE
echo ========================================
echo.

set /p GITHUB_USER="Ingresa tu usuario de GitHub: "
set /p REPO_NAME="Ingresa el nombre de tu repositorio (ej: vg-ms-students-testing): "

if "%GITHUB_USER%"=="" (
    echo Error: Debes ingresar tu usuario de GitHub
    pause
    exit /b 1
)

if "%REPO_NAME%"=="" (
    echo Error: Debes ingresar el nombre del repositorio
    pause
    exit /b 1
)

echo.
echo Configuracion a aplicar:
echo - Organization: %GITHUB_USER%
echo - Project Key: %GITHUB_USER%_%REPO_NAME%
echo - Project Name: %REPO_NAME%
echo.

set /p CONFIRM="¿Es correcta esta configuracion? (s/n): "
if /i not "%CONFIRM%"=="s" (
    echo Operacion cancelada
    pause
    exit /b 1
)

echo.
echo Actualizando pom.xml...

powershell -Command "(Get-Content pom.xml) -replace '<sonar.organization>vallegrande</sonar.organization>', '<sonar.organization>%GITHUB_USER%</sonar.organization>' | Set-Content pom.xml"

powershell -Command "(Get-Content pom.xml) -replace '<sonar.projectKey>vg-ms-students</sonar.projectKey>', '<sonar.projectKey>%GITHUB_USER%_%REPO_NAME%</sonar.projectKey>' | Set-Content pom.xml"

echo.
echo ✅ Configuracion actualizada exitosamente!
echo.
echo Proximos pasos:
echo 1. Commit y push de los cambios:
echo    git add pom.xml
echo    git commit -m "config: Actualizar configuracion SonarQube"
echo    git push
echo.
echo 2. El GitHub Actions se ejecutara automaticamente
echo.
echo 3. Ver resultados en:
echo    https://sonarcloud.io/project/overview?id=%GITHUB_USER%_%REPO_NAME%
echo.
pause