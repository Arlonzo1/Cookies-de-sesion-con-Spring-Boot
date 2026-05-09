# 🍪 Demo Cookies de Sesión — Spring Boot

Ejercicio práctico que demuestra el uso de cookies de sesión HTTP
en una aplicación Java con Spring Boot.

## ¿Qué hace?

Implementa un sistema básico de sesión con 3 endpoints REST:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/login?nombre=X` | Crea una sesión y envía la cookie `MI_SESION` |
| GET | `/perfil` | Lee la sesión activa usando la cookie |
| DELETE | `/logout` | Destruye la sesión e invalida la cookie |

## ¿Cómo funciona la cookie?

1. El cliente hace POST a `/login`
2. El servidor guarda los datos en memoria y genera un ID único
3. El servidor envía la cookie `MI_SESION=<ID>` al navegador
4. En cada petición siguiente, el navegador reenvía la cookie
5. El servidor usa el ID para encontrar los datos de esa sesión

## Requisitos

- Java 17+
- Maven (incluido con `mvnw`)

## Cómo ejecutar

```bash
./mvnw spring-boot:run        # Linux/Mac
.\mvnw spring-boot:run        # Windows
```

## Cómo probar

### Opción 1 — Interfaz visual
Abre el navegador en: **http://localhost:8080**

### Opción 2 — Terminal
```bash
# Login
curl.exe -c cookies.txt -X POST "http://localhost:8080/login?nombre=Juan"

# Perfil
curl.exe -b cookies.txt http://localhost:8080/perfil

# Logout
curl.exe -b cookies.txt -X DELETE http://localhost:8080/logout
```

### Opción 3 — Archivo pruebas.http
Abre `pruebas.http` en VS Code y haz clic en **Send Request**.

## Configuración de la cookie (`application.properties`)

| Propiedad | Valor | Descripción |
|-----------|-------|-------------|
| `cookie.name` | `MI_SESION` | Nombre personalizado |
| `session.timeout` | `10m` | Expira en 10 minutos |
| `cookie.http-only` | `true` | JavaScript no puede leerla |