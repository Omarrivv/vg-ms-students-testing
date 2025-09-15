@echo off
echo ========================================
echo   VERIFICACION DE CONFIGURACION
echo ========================================
echo.

echo [1/5] Verificando estructura de archivos...
if exist "src\test\java\pe\edu\vallegrande\msvstudents\application\service\StudentServiceParameterizedTest.java" (
    echo ✅ Pruebas parametrizadas encontradas
) else (
    echo ❌ Pruebas parametrizadas NO encontradas
)

if exist "src\test\java\pe\edu\vallegrande\msvstudents\application\service\StudentServiceTest.java" (
    echo ✅ Pruebas unitarias encontradas
) else (
    echo ❌ Pruebas unitarias NO encontradas
)

if exist ".github\workflows\ci-cd.yml" (
    echo ✅ GitHub Actions configurado
) else (
    echo ❌ GitHub Actions NO configurado
)

echo.
echo [2/5] Verificando configuracion Maven...
findstr /C:"jacoco-maven-plugin" pom.xml >nul
if %ERRORLEVEL% equ 0 (
    echo ✅ Plugin JaCoCo configurado
) else (
    echo ❌ Plugin JaCoCo NO configurado
)

findstr /C:"sonar-maven-plugin" pom.xml >nul
if %ERRORLEVEL% equ 0 (
    echo ✅ Plugin SonarQube configurado
) else (
    echo ❌ Plugin SonarQube NO configurado
)

echo.
echo [3/5] Verificando compilacion...
call mvn compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Proyecto compila correctamente
) else (
    echo ❌ Error en compilacion
    goto :error
)

echo.
echo [4/5] Verificando compilacion de pruebas...
call mvn test-compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Pruebas compilan correctamente
) else (
    echo ❌ Error en compilacion de pruebas
    goto :error
)

echo.
echo [5/5] Verificando generacion de reporte JaCoCo...
if exist "target\site\jacoco\index.html" (
    echo ✅ Reporte JaCoCo ya existe
) else (
    echo ℹ️  Generando reporte JaCoCo...
    call mvn jacoco:report -q
    if exist "target\site\jacoco\index.html" (
        echo ✅ Reporte JaCoCo generado exitosamente
    ) else (
        echo ❌ Error generando reporte JaCoCo
        goto :error
    )
)

echo.
echo ========================================
echo        VERIFICACION COMPLETADA
echo ========================================
echo.
echo ✅ Configuracion correcta - Todo listo para usar!
echo.
echo Archivos importantes:
echo - Pruebas parametrizadas: src\test\java\...\StudentServiceParameterizedTest.java
echo - Reporte JaCoCo: target\site\jacoco\index.html
echo - GitHub Actions: .github\workflows\ci-cd.yml
echo - Guia completa: TESTING_GUIDE.md
echo.
echo Comandos disponibles:
echo - run-tests.bat          : Ejecutar todas las pruebas
echo - run-sonar.bat TOKEN    : Analisis SonarQube
echo - mvn jacoco:report      : Generar reporte cobertura
echo.
goto :end

:error
echo.
echo ========================================
echo           ERROR DETECTADO
echo ========================================
echo.
echo Por favor revise los errores anteriores y:
echo 1. Verifique que Java 17 este instalado
echo 2. Verifique que Maven este configurado
echo 3. Revise los logs de error para mas detalles
echo.

:end
pause