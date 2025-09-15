@echo off
echo ========================================
echo    SUBIR PROYECTO A GITHUB
echo ========================================
echo.

echo IMPORTANTE: Antes de ejecutar este script:
echo 1. Crea un repositorio en GitHub
echo 2. Copia la URL del repositorio
echo 3. Instala Git si no lo tienes
echo.

set /p REPO_URL="Ingresa la URL de tu repositorio GitHub (ej: https://github.com/usuario/vg-ms-students-testing.git): "

if "%REPO_URL%"=="" (
    echo Error: Debes ingresar la URL del repositorio
    pause
    exit /b 1
)

echo.
echo [1/6] Inicializando repositorio Git...
git init
if %ERRORLEVEL% neq 0 (
    echo Error: Git no esta instalado o no funciona correctamente
    echo Descarga Git desde: https://git-scm.com/download/win
    pause
    exit /b 1
)

echo.
echo [2/6] Agregando archivos al repositorio...
git add .

echo.
echo [3/6] Creando commit inicial...
git commit -m "feat: Implementacion completa de pruebas unitarias, parametrizadas y cobertura con JaCoCo

- ✅ Pruebas parametrizadas con @EnumSource, @CsvSource, @ValueSource, @MethodSource
- ✅ Cobertura de codigo con JaCoCo (70%% minimo)
- ✅ Configuracion SonarQube para analisis de calidad
- ✅ GitHub Actions CI/CD pipeline completo
- ✅ Scripts de automatizacion (run-tests.bat, run-sonar.bat)
- ✅ Documentacion completa (TESTING_GUIDE.md)

Tipos de pruebas implementadas:
- Pruebas unitarias del servicio (36 pruebas)
- Pruebas parametrizadas (16 pruebas con multiples casos)
- Pruebas de controlador (12 pruebas de integracion)
- Total: 52+ casos de prueba

Metricas de calidad:
- Cobertura objetivo: ≥ 70%%
- Bugs: 0 tolerancia
- Vulnerabilidades: 0 tolerancia
- Code Smells: ≤ 5 permitidos"

echo.
echo [4/6] Configurando rama principal...
git branch -M main

echo.
echo [5/6] Agregando repositorio remoto...
git remote add origin %REPO_URL%

echo.
echo [6/6] Subiendo codigo a GitHub...
git push -u origin main

if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================
    echo     CODIGO SUBIDO EXITOSAMENTE
    echo ========================================
    echo.
    echo ✅ Tu codigo ya esta en GitHub!
    echo ✅ Ahora puedes configurar SonarCloud
    echo.
    echo Siguiente paso: Configurar SonarCloud
    echo URL de tu repositorio: %REPO_URL%
) else (
    echo.
    echo ❌ Error al subir el codigo
    echo Posibles causas:
    echo - No tienes permisos en el repositorio
    echo - La URL del repositorio es incorrecta
    echo - Problemas de autenticacion con GitHub
    echo.
    echo Solucion:
    echo 1. Verifica que la URL sea correcta
    echo 2. Configura tu autenticacion con GitHub
    echo 3. git config --global user.name "Tu Nombre"
    echo 4. git config --global user.email "tu@email.com"
)

echo.
pause