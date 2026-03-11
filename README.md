# Prueba Técnica – Arquitectura de Microservicios (Products & Inventory)

Este proyecto implementa una arquitectura de microservicios utilizando **Spring Boot**, donde se gestionan productos y su inventario de manera desacoplada.

El objetivo es demostrar buenas prácticas en el desarrollo backend incluyendo:

- Arquitectura de microservicios
- Comunicación entre servicios
- Manejo de concurrencia
- Operaciones idempotentes
- Resiliencia ante fallos
- Observabilidad (logs y correlation-id)
- Health checks con Actuator

---

# Arquitectura del sistema

El sistema está compuesto por dos microservicios:

| Servicio | Puerto | Responsabilidad |
|--------|--------|----------------|
| products-service | 8081 | Gestión del catálogo de productos |
| inventory-service | 8082 | Gestión de inventario y compras |

Flujo principal del sistema:

Cliente
↓
inventory-service
↓
products-service
↓
PostgreSQL

El **inventory-service** valida la existencia de un producto consultando el **products-service** antes de procesar operaciones de inventario.

---

# Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Resilience4j
- Spring Actuator
- Docker

---

# Características implementadas

## Comunicación entre microservicios

El servicio de inventario se comunica con el servicio de productos para validar la existencia de un producto antes de registrar inventario o procesar compras.

Esto evita inconsistencias entre servicios.

---

## Manejo de concurrencia en compras

El endpoint de compra utiliza bloqueo a nivel de base de datos para evitar condiciones de carrera que puedan generar inventario negativo.

Esto garantiza que múltiples solicitudes concurrentes no puedan consumir más inventario del disponible.

---

## Operaciones idempotentes

El endpoint de compra soporta el header:
Idempotency-Key

Esto permite evitar compras duplicadas cuando un cliente repite accidentalmente una solicitud.

Ejemplo:
	POST /inventory/purchase
	Idempotency-Key: compra-001

Si la misma clave se vuelve a enviar, el sistema retorna la misma respuesta sin volver a procesar la operación.

---

## Resiliencia ante fallos

Se utiliza **Resilience4j** para manejar fallos en la comunicación entre microservicios.

Estrategias aplicadas:

- Retry
- Timeout
- Circuit Breaker

Si el servicio de productos no está disponible, el sistema responde con: 503 Service Unavailable

---

## Observabilidad

Se implementa un **Correlation ID** para rastrear solicitudes entre microservicios.

Header utilizado: X-Correlation-Id

Si el cliente no lo envía, el sistema genera uno automáticamente.

Esto permite seguir el flujo completo de una petición en los logs.

Ejemplo de log: 2026-03-11 INFO [c1b2a3d4] InventoryService - Processing purchase for productId=...

---

## Health checks

Se utilizan endpoints de **Spring Actuator** para monitorear el estado del sistema.

### Products Service

GET /actuator/health
GET /actuator/info

### Inventory Service

GET /actuator/health
GET /actuator/info

---

# Endpoints principales

## Products Service

Crear producto

POST /products


Obtener producto


GET /products/{id}

---

## Inventory Service

Crear inventario

POST /inventory

Obtener inventario por producto

GET /inventory/product/{productId}

Comprar producto

POST /inventory/purchase

---

# Ejemplo de flujo completo

### 1 Crear producto

POST /products

---

### 2 Crear inventario

POST /inventory

{
	"productId": "UUID",
	"available": 10
}

---

### 3 Comprar producto

POST /inventory/purchase

Header:

Idempotency-Key: compra-001

Body:

{
	"productId": "UUID",
	"quantity": 1
}

---

# Ejecución del proyecto

## Ejecutar servicios localmente

### products-service

mvn spring-boot:run

### inventory-service

mvn spring-boot:run

---

# Ejecutar con Docker

docker-compose up --build

Esto iniciará:

- PostgreSQL
- products-service
- inventory-service

---

# Puertos utilizados

| Servicio | Puerto |
|--------|--------|
| products-service | 8081 |
| inventory-service | 8082 |
| PostgreSQL | 5432 |

---

# Autor

Prueba técnica desarrollada por **Hugo Salcedo**.
