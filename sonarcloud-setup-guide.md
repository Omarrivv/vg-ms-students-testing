# Guía de Configuración SonarCloud

## Paso 1: Crear Proyecto en SonarCloud

1. **Ir a SonarCloud**: https://sonarcloud.io
2. **Login con GitHub**: Usar tu cuenta de GitHub
3. **Clic en "Analyze new project"**
4. **Seleccionar GitHub** como proveedor
5. **Autorizar SonarCloud** a acceder a tus repositorios
6. **Seleccionar tu repositorio** `vg-ms-students-testing`
7. **Clic en "Set up"**

## Paso 2: Configuración del Proyecto

### Información del Proyecto:
```
Organization: tu-usuario-github
Project Key: tu-usuario-github_vg-ms-students-testing
Project Name: vg-ms-students-testing
```

### Configuración Automática:
- SonarCloud detectará que es un proyecto Maven
- Configurará automáticamente el análisis para Java

## Paso 3: Obtener el Token

### 3.1 Generar Token Personal:
1. **Clic en tu avatar** (esquina superior derecha)
2. **My Account** → **Security**
3. **Generate Token**:
   ```
   Name: vg-ms-students-token
   Type: User Token
   Expiration: 90 days (o más)
   ```
4. **Clic en "Generate"**
5. **COPIAR EL TOKEN** (solo se muestra una vez)

### 3.2 Configurar Token en GitHub:
1. **Ir a tu repositorio en GitHub**
2. **Settings** → **Secrets and variables** → **Actions**
3. **New repository secret**:
   ```
   Name: SONAR_TOKEN
   Value: [pegar el token copiado]
   ```
4. **Add secret**

## Paso 4: Configurar GitHub Actions

El archivo `.github/workflows/ci-cd.yml` ya está configurado para usar:
- `SONAR_TOKEN` como secret
- Análisis automático en cada push/PR

## Paso 5: Verificar Configuración

### En el pom.xml, actualizar:
```xml
<properties>
    <sonar.organization>TU-USUARIO-GITHUB</sonar.organization>
    <sonar.projectKey>TU-USUARIO-GITHUB_vg-ms-students-testing</sonar.projectKey>
</properties>
```

## Comandos para Prueba Local

```bash
# Con el token generado
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN_AQUI

# O usar el script
./run-sonar.bat TU_TOKEN_AQUI
```

## URLs Importantes

- **SonarCloud Dashboard**: https://sonarcloud.io/projects
- **Tu Proyecto**: https://sonarcloud.io/project/overview?id=TU-USUARIO-GITHUB_vg-ms-students-testing
- **GitHub Actions**: https://github.com/TU-USUARIO/vg-ms-students-testing/actions

## Métricas que Verás en SonarCloud

### Quality Gate:
- **Passed/Failed**: Estado general del proyecto
- **New Code**: Análisis solo del código nuevo

### Métricas Principales:
- **Bugs**: 0 (objetivo)
- **Vulnerabilities**: 0 (objetivo)
- **Code Smells**: ≤ 5 (aceptable)
- **Coverage**: ≥ 70% (configurado)
- **Duplications**: ≤ 3% (recomendado)

### Tipos de Issues:
- **Blocker**: Errores críticos que deben corregirse
- **Critical**: Errores importantes
- **Major**: Problemas de mantenibilidad
- **Minor**: Mejoras menores
- **Info**: Sugerencias

## Solución de Problemas Comunes

### Error: "Project not found"
- Verificar que el projectKey sea correcto
- Verificar que el token tenga permisos

### Error: "Authentication failed"
- Regenerar el token en SonarCloud
- Actualizar el secret en GitHub

### Error: "Coverage report not found"
- Verificar que JaCoCo genere el XML: `target/site/jacoco/jacoco.xml`
- Verificar la propiedad: `sonar.coverage.jacoco.xmlReportPaths`

## Próximos Pasos

1. ✅ Subir código a GitHub
2. ✅ Configurar proyecto en SonarCloud
3. ✅ Generar y configurar token
4. ✅ Ejecutar primer análisis
5. 📊 Revisar métricas y corregir issues
6. 🎥 Grabar video explicativo