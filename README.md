# 🌦️ WeatherAPI - API REST de Clima

> API REST desarrollada con Spring Boot que consume la API externa [WeatherAPI](https://www.weatherapi.com/) para consultar el clima actual y pronósticos por hora, y permite registrar monitores climáticos en base de datos, como parte de un laboratorio académico.

## 📋 Tabla de Contenidos

- [Descripción](#-descripción-del-proyecto)
- [Tecnologías](#-tecnologías-usadas)
- [Estructura](#-estructura-principal)
- [Flujo Funcional](#-flujo-funcional-implementado)
- [Seguridad](#-seguridad)
- [Estado](#-estado-frente-a-la-consigna)
- [Ejecución](#-cómo-ejecutar)

---

## 📝 Descripción del Proyecto

Una API REST que expone servicios de información climática, permitiendo a los clientes:

✅ Consultar el clima actual de cualquier ciudad (temperatura, condición, sensación térmica y humedad)
✅ Obtener el pronóstico por horas del día actual de una ciudad
✅ Guardar registros de monitoreo climático en base de datos con validación de datos
✅ Recibir respuestas estandarizadas con formato `ApiResponse<T>` (status, message, data)

## 💻 Tecnologías Usadas

| Tecnología | Uso |
|-----------|-----|
| Java 17+ | Lenguaje base |
| Spring Boot | Framework principal |
| Spring Web (RestTemplate) | Cliente HTTP para consumir WeatherAPI |
| Spring Security | Autenticación HTTP Basic en endpoints `/clima/**` |
| Spring Data JPA + Hibernate | Persistencia de monitores climáticos |
| H2 Database | Base de datos en memoria (MySQL disponible como alternativa) |
| Bean Validation (Jakarta) | Validación de requests |
| Lombok | Reducción de código repetitivo |
| Maven | Gestor de dependencias |

## 📂 Estructura Principal

```
weatherapi/
├── src/main/java/com/example/weatherapi/
│   ├── WeatherApiApplication.java          # Punto de entrada
│   ├── config/
│   │   └── RestClientConfig.java           # Bean RestTemplate
│   ├── controller/
│   │   ├── ClimaController.java            # Endpoints /clima/**
│   │   └── GlobalExceptionHandler.java     # Manejo global de excepciones
│   ├── dto/
│   │   ├── ApiResponse.java                # Respuesta estándar
│   │   ├── ClimaActualDto.java             # DTO de clima actual
│   │   ├── MonitoreoClimaticoRequest.java  # Request de monitoreo (validado)
│   │   └── WeatherApiDtos.java             # DTOs de deserialización WeatherAPI
│   ├── entity/
│   │   └── MonitoreoClimatico.java         # Entidad JPA
│   ├── repository/
│   │   └── MonitoreoClimaticoRepository.java
│   ├── security/
│   │   └── WebSecurityConfig.java          # HTTP Basic + usuarios en memoria
│   └── service/
│       ├── WeatherApiService.java          # Consumo de API externa (Ej. 1 y 2)
│       └── MonitoreoClimaticoService.java  # Lógica de negocio (Ej. 3)
└── src/main/resources/
    └── application.properties              # Configuración (H2/MySQL, API key, puerto)
```

## 🔄 Flujo Funcional Implementado

```
┌─────────────────────────────────────────────────────────────┐
│                 FLUJO DE LA API DE CLIMA                    │
├─────────────────────────────────────────────────────────────┤
│  1. GET /clima/actual/{ciudad}                              │
│     ↓ Consulta WeatherAPI (/v1/current.json)                │
│     ↓ Devuelve { temp_c, condition, feelslike_c, humidity } │
│                                                             │
│  2. GET /clima/pronostico/{ciudad}                          │
│     ↓ Consulta WeatherAPI (/v1/forecast.json?days=1)        │
│     ↓ Filtra las horas del día actual                       │
│     ↓ Devuelve { city, forecast: [ { hour, temp_c, cond } ] }│
│                                                             │
│  3. POST /clima/monitoreo                                   │
│     ↓ Valida request (Bean Validation)                      │
│     ↓ Valida coherencia: tempMin ≤ tempProm ≤ tempMax       │
│     ↓ Persiste en tabla monitoreo_climatico                 │
└─────────────────────────────────────────────────────────────┘
```

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/clima/actual/{ciudad}` | GET | Clima actual: temperatura, condición, sensación térmica y humedad |
| `/clima/pronostico/{ciudad}` | GET | Pronóstico por horas del día actual |
| `/clima/monitoreo` | POST | Guarda un registro de monitoreo climático |

> ⚠️ Todos los endpoints bajo `/clima/**` **requieren autenticación HTTP Basic** (usuario: `admin`, contraseña: `password`).

### 📦 Formato de Respuesta Estándar

Todas las respuestas usan el wrapper `ApiResponse<T>`:

```json
{
  "status": "success",
  "message": "OK - Clima actual obtenido correctamente",
  "data": { "temp_c": 22.5, "condition": "Sunny", "feelslike_c": 25.1, "humidity": 60 }
}
```

### 📥 Request de Monitoreo (Ejercicio 3)

```json
POST /clima/monitoreo
Content-Type: application/json
Authorization: Basic YWRtaW46cGFzc3dvcmQ=

{
  "ciudad": "Lima",
  "fecha": "2026-09-28",
  "tempPromedio": 20.5,
  "condicionMasFrecuente": "Partly cloudy",
  "tempMax": 26.0,
  "tempMin": 16.0
}
```

## 🔐 Seguridad

- **Autenticación:** HTTP Basic sobre los endpoints `/clima/**`.
- **Sesiones:** STATELESS (sin estado).
- **CSRF:** deshabilitado (API REST pensada para consumo con Basic Auth).
- **Usuarios en memoria:** `admin` / `password` (encriptado con BCrypt).
- **En producción:** reemplazar `InMemoryUserDetailsManager` por un `UserDetailsService` que consulte una tabla de usuarios.

## 🧮 Reglas de Validación del Monitoreo

**Validaciones de entrada (Bean Validation):**

| Campo | Regla |
|-------|-------|
| `ciudad` | Obligatorio |
| `fecha` | Obligatoria |
| `tempPromedio` | Obligatoria |
| `condicionMasFrecuente` | Obligatoria |
| `tempMax` | Obligatoria |
| `tempMin` | Obligatoria |

**Validación de coherencia (en servicio):**

```
tempMin ≤ tempPromedio ≤ tempMax
```

Si no se cumple, se lanza `IllegalArgumentException` y el controlador responde con `ApiResponse.error(...)` y HTTP 400.

## ✅ Estado frente a la Consigna

### ✔️ Implementado

- [x] Ejercicio 1: clima actual por ciudad (`/clima/actual/{ciudad}`)
- [x] Ejercicio 2: pronóstico por hora del día actual (`/clima/pronostico/{ciudad}`)
- [x] Ejercicio 3: guardado de monitoreo climático en BD (`/clima/monitoreo`)
- [x] Consumo de WeatherAPI con `RestTemplate` y DTOs con `@JsonIgnoreProperties`
- [x] Autenticación HTTP Basic con Spring Security
- [x] Validación de requests con Bean Validation
- [x] Validación de coherencia de temperaturas (mínima ≤ promedio ≤ máxima)
- [x] Respuestas estandarizadas con `ApiResponse<T>`
- [x] Manejo global de excepciones con `@RestControllerAdvice`
- [x] Persistencia JPA en H2 (con alternativa MySQL comentada en `application.properties`)

### 💡 Observación de Mejora

⚠️ La API key de WeatherAPI está **hardcodeada** en `application.properties` y tiene un valor por defecto en `WeatherApiService` (`@Value("${weather.api.key:...}")`).

**Recomendación:** Para producción, mover la clave a una variable de entorno:

```properties
weather.api.key=${WEATHER_API_KEY}
```

## 🚀 Cómo Ejecutar

### Requisitos Previos

- Java 17+
- Maven 3.6+ (o usar el wrapper `mvnw` si está incluido)

### Configurar la API Key

La clave ya está incluida en `application.properties`. Si necesitas cambiarla:

```properties
weather.api.key=TU_API_KEY
```

(Obtén una gratuita en [weatherapi.com](https://www.weatherapi.com/))

### Ejecutar la Aplicación

**En Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

**En Linux/macOS:**

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

### Probar los Endpoints

Con la aplicación en `http://localhost:8080`, consumir con credenciales Basic (`admin` / `password`):

```bash
curl -u admin:password http://localhost:8080/clima/actual/Lima

curl -u admin:password http://localhost:8080/clima/pronostico/Lima

curl -u admin:password -X POST http://localhost:8080/clima/monitoreo \
  -H "Content-Type: application/json" \
  -d '{"ciudad":"Lima","fecha":"2026-09-28","tempPromedio":20.5,
       "condicionMasFrecuente":"Sunny","tempMax":26.0,"tempMin":16.0}'
```

### Consola H2

Disponible en `http://localhost:8080/h2-console` para inspeccionar la tabla `monitoreo_climatico`:

- JDBC URL: `jdbc:h2:mem:weatherdb`
- Usuario: `sa`
- Contraseña: (vacía)

---

## 📚 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [WeatherAPI Docs](https://www.weatherapi.com/docs/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

---

## 📄 Licencia

Este proyecto es de uso académico y educativo como parte de un laboratorio de curso universitario.
