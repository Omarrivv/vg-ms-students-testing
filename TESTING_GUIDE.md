# Guía de Pruebas Unitarias y Cobertura - Microservicio Estudiantes

## 📋 Índice
1. [Introducción](#introducción)
2. [Pruebas Parametrizadas](#pruebas-parametrizadas)
3. [Cobertura con JaCoCo](#cobertura-con-jacoco)
4. [Análisis con SonarQube](#análisis-con-sonarqube)
5. [GitHub Actions](#github-actions)
6. [Comandos Útiles](#comandos-útiles)

## 🎯 Introducción

Este proyecto implementa un conjunto completo de pruebas unitarias y parametrizadas para el microservicio de estudiantes, incluyendo análisis de cobertura con JaCoCo y análisis de calidad con SonarQube.

### Tecnologías Utilizadas
- **JUnit 5**: Framework de pruebas
- **Mockito**: Mocking framework
- **JaCoCo**: Análisis de cobertura de código
- **SonarQube**: Análisis de calidad de código
- **GitHub Actions**: CI/CD pipeline
- **Spring Boot Test**: Pruebas de integración
- **Reactor Test**: Pruebas para programación reactiva

## 🧪 Pruebas Parametrizadas

### Tipos de Pruebas Implementadas

#### 1. Pruebas con @EnumSource
```java
@ParameterizedTest(name = "Buscar estudiante con tipo de documento: {0}")
@EnumSource(DocumentType.class)
void shouldFindStudentsByDocumentType(DocumentType documentType)
```
**Propósito**: Valida que el sistema funcione correctamente con todos los tipos de documento disponibles.

#### 2. Pruebas con @CsvSource
```java
@ParameterizedTest(name = "Buscar por nombre: {0}")
@CsvSource({
    "Juan, Juan Carlos",
    "María, María Elena", 
    "Pedro, Pedro Luis"
})
void shouldFindStudentsByPartialName(String searchTerm, String fullName)
```
**Propósito**: Verifica la funcionalidad de búsqueda parcial de nombres con diferentes combinaciones.

#### 3. Pruebas con @ValueSource
```java
@ParameterizedTest(name = "Validar número de documento: {0}")
@ValueSource(strings = {"12345678", "87654321", "11111111"})
void shouldValidateDocumentNumbers(String documentNumber)
```
**Propósito**: Valida diferentes formatos de números de documento.

#### 4. Pruebas con @MethodSource
```java
@ParameterizedTest(name = "Crear estudiante: {0}")
@MethodSource("provideStudentCreationData")
void shouldCreateStudentsWithDifferentData(CreateStudentRequest request, String expectedName)
```
**Propósito**: Permite casos de prueba complejos con múltiples parámetros y lógica personalizada.

### Beneficios de las Pruebas Parametrizadas

1. **Reducción de Código Duplicado**: Una sola prueba valida múltiples escenarios
2. **Mejor Cobertura**: Fácil agregar nuevos casos de prueba
3. **Mantenibilidad**: Cambios en la lógica de prueba se aplican a todos los casos
4. **Legibilidad**: Nombres descriptivos para cada caso de prueba

## 📊 Cobertura con JaCoCo

### Configuración en pom.xml

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Paso a Paso para Implementar JaCoCo

#### 1. Agregar Dependencia
```xml
<!-- Ya incluido en el pom.xml del proyecto -->
```

#### 2. Ejecutar Pruebas con Cobertura
```bash
mvn clean test
```

#### 3. Generar Reporte
```bash
mvn jacoco:report
```

#### 4. Verificar Cobertura Mínima
```bash
mvn jacoco:check
```

### Interpretación del Reporte JaCoCo

El reporte se genera en `target/site/jacoco/index.html` y contiene:

#### Métricas de Cobertura:
- **Instructions (C0)**: Cobertura de instrucciones bytecode
- **Branches (C1)**: Cobertura de ramas (if/else, switch)
- **Lines**: Cobertura de líneas de código
- **Methods**: Cobertura de métodos
- **Classes**: Cobertura de clases

#### Colores del Reporte:
- 🟢 **Verde**: Cobertura alta (>80%)
- 🟡 **Amarillo**: Cobertura media (60-80%)
- 🔴 **Rojo**: Cobertura baja (<60%)

#### Ejemplo de Interpretación:
```
Instructions: 85% (340/400)
- 340 instrucciones cubiertas de 400 totales
- Excelente cobertura, objetivo cumplido

Branches: 70% (14/20)
- 14 ramas cubiertas de 20 totales
- Necesita mejorar cobertura de condiciones

Lines: 88% (120/136)
- 120 líneas cubiertas de 136 totales
- Muy buena cobertura de líneas
```

### Configuración de Umbrales de Cobertura

```xml
<configuration>
    <rules>
        <rule>
            <element>PACKAGE</element>
            <limits>
                <limit>
                    <counter>LINE</counter>
                    <value>COVEREDRATIO</value>
                    <minimum>0.70</minimum>
                </limit>
            </limits>
        </rule>
    </rules>
</configuration>
```

## 🔍 Análisis con SonarQube

### Paso a Paso para SonarQube

#### 1. Configurar Propiedades en pom.xml
```xml
<properties>
    <sonar.organization>vallegrande</sonar.organization>
    <sonar.host.url>https://sonarcloud.io</sonar.host.url>
    <sonar.projectKey>vg-ms-students</sonar.projectKey>
</properties>
```

#### 2. Agregar Plugin SonarQube
```xml
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>3.9.1.2184</version>
</plugin>
```

#### 3. Configurar Token de SonarCloud
```bash
# Crear variable de entorno
export SONAR_TOKEN=tu_token_aqui
```

#### 4. Ejecutar Análisis
```bash
mvn clean verify sonar:sonar
```

### Interpretación de Resultados SonarQube

#### Quality Gate
- **Passed**: Código cumple con estándares de calidad
- **Failed**: Código necesita mejoras antes de merge

#### Métricas Principales:

1. **Bugs**: Errores que pueden causar comportamiento incorrecto
2. **Vulnerabilities**: Problemas de seguridad
3. **Code Smells**: Problemas de mantenibilidad
4. **Coverage**: Porcentaje de código cubierto por pruebas
5. **Duplications**: Porcentaje de código duplicado

#### Ejemplo de Hallazgos y Correcciones:

**Hallazgo**: "Remove this unused private method"
```java
// ❌ Antes
private void unusedMethod() {
    // código no utilizado
}

// ✅ Después
// Método eliminado o marcado como @SuppressWarnings si es necesario
```

**Hallazgo**: "Replace this lambda with a method reference"
```java
// ❌ Antes
.map(student -> StudentMapper.toResponse(student))

// ✅ Después
.map(StudentMapper::toResponse)
```

**Hallazgo**: "Add a nested comment explaining why this method is empty"
```java
// ❌ Antes
public void emptyMethod() {
}

// ✅ Después
public void emptyMethod() {
    // Método intencionalmente vacío - implementación futura
}
```

## 🚀 GitHub Actions

### Configuración del Pipeline

El archivo `.github/workflows/ci-cd.yml` implementa:

#### 1. Job de Pruebas
- Configuración de MongoDB como servicio
- Ejecución de pruebas unitarias y parametrizadas
- Generación de reportes de cobertura
- Análisis con SonarQube

#### 2. Job de Construcción
- Compilación del proyecto
- Empaquetado JAR
- Construcción de imagen Docker

#### 3. Quality Gate
- Verificación de que todas las etapas pasaron
- Bloqueo de deployment si hay fallos

### Variables de Entorno Requeridas

En GitHub Settings > Secrets:
```
SONAR_TOKEN: Token de SonarCloud
```

### Flujo de Ejecución

1. **Push/PR** → Trigger del pipeline
2. **Pruebas** → Ejecución de todas las pruebas
3. **Cobertura** → Generación de reportes JaCoCo
4. **SonarQube** → Análisis de calidad
5. **Build** → Compilación y empaquetado
6. **Quality Gate** → Verificación final

## 🛠️ Comandos Útiles

### Ejecución Local de Pruebas

```bash
# Ejecutar todas las pruebas
mvn clean test

# Ejecutar solo pruebas parametrizadas
mvn test -Dtest="**/*ParameterizedTest"

# Ejecutar pruebas con cobertura
mvn clean test jacoco:report

# Verificar cobertura mínima
mvn jacoco:check

# Ejecutar análisis SonarQube local
mvn clean verify sonar:sonar -Dsonar.token=tu_token

# Ejecutar pruebas específicas
mvn test -Dtest=StudentServiceTest

# Ejecutar con perfil de pruebas
mvn test -Dspring.profiles.active=test
```

### Generación de Reportes

```bash
# Generar reporte HTML de JaCoCo
mvn jacoco:report
# Resultado en: target/site/jacoco/index.html

# Generar reporte XML para CI/CD
mvn jacoco:report
# Resultado en: target/site/jacoco/jacoco.xml

# Ver reporte de Surefire
# Resultado en: target/surefire-reports/
```

### Docker para Pruebas

```bash
# Levantar MongoDB para pruebas
docker run -d -p 27017:27017 --name test-mongo mongo:6.0

# Ejecutar pruebas con MongoDB en Docker
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/test_db mvn test

# Limpiar contenedor
docker stop test-mongo && docker rm test-mongo
```

## 📈 Métricas de Calidad Objetivo

### Cobertura de Código
- **Líneas**: ≥ 80%
- **Ramas**: ≥ 70%
- **Métodos**: ≥ 85%

### SonarQube Quality Gate
- **Bugs**: 0
- **Vulnerabilities**: 0
- **Code Smells**: ≤ 5
- **Coverage**: ≥ 80%
- **Duplications**: ≤ 3%

### Tiempo de Ejecución
- **Pruebas Unitarias**: ≤ 2 minutos
- **Pipeline Completo**: ≤ 10 minutos

## 🎯 Importancia para el Proyecto de Responsabilidad Social

### Beneficios Técnicos
1. **Confiabilidad**: Garantiza que el sistema funcione correctamente
2. **Mantenibilidad**: Facilita cambios futuros sin romper funcionalidad
3. **Calidad**: Asegura código limpio y bien estructurado

### Impacto Social
1. **Confianza**: Los usuarios confían en un sistema bien probado
2. **Disponibilidad**: Menos errores = mayor disponibilidad del servicio
3. **Escalabilidad**: Base sólida para crecimiento futuro

### Valor Agregado
1. **Reducción de Costos**: Menos bugs en producción
2. **Tiempo de Desarrollo**: Detección temprana de problemas
3. **Documentación Viva**: Las pruebas documentan el comportamiento esperado

---

## 📞 Contacto y Soporte

Para dudas o problemas con las pruebas:
1. Revisar logs de ejecución
2. Verificar configuración de MongoDB
3. Consultar documentación de JaCoCo y SonarQube
4. Revisar issues en el repositorio del proyecto