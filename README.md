Prueba Técnica Full Stack

Java Spring Boot + Vue + Docker

Descripción

Este proyecto corresponde a una prueba técnica Full Stack basada en una arquitectura de microservicios utilizando Java Spring Boot para el backend y Vue 3 para el frontend.

El sistema permite gestionar un catálogo de productos y su inventario, incluyendo funcionalidades como:

Autenticación mediante JWT

Consulta de productos con búsqueda, filtros y paginación

Consulta de inventario por producto

Compra de productos

Comunicación segura entre microservicios mediante API Key

Manejo de idempotencia en operaciones de compra

Resiliencia en llamadas entre servicios mediante Resilience4j

El sistema está completamente dockerizado, permitiendo levantar toda la aplicación con Docker Compose.

Arquitectura

El sistema está compuesto por los siguientes servicios:

Frontend (Vue)
       |
       v
Products Service (Spring Boot)
       |
       v
Inventory Service (Spring Boot)

Cada microservicio utiliza su propia base de datos PostgreSQL.

Servicios incluidos
Servicio	Descripción
products-service	Gestión de productos y autenticación
inventory-service	Gestión de inventario y compras
frontend	Aplicación web desarrollada con Vue
products-db	Base de datos PostgreSQL para productos
inventory-db	Base de datos PostgreSQL para inventario
Tecnologías utilizadas
Backend

Java 17

Spring Boot

Spring Data JPA

Hibernate

PostgreSQL

Resilience4j

JWT (JSON Web Token)

Frontend

Vue 3

Vite

Axios

CSS

Infraestructura

Docker

Docker Compose

Funcionalidades implementadas
Autenticación

Login mediante JWT

Protección de rutas en el frontend

Interceptor de Axios para enviar el token automáticamente

Productos

Listado de productos

Búsqueda por nombre o SKU

Filtro por estado

Paginación

Inventario

Consulta de inventario por producto

Registro de inventario

Compras

Compra de productos

Validación de stock disponible

Manejo de idempotencia

Comunicación entre microservicios

Uso de API Key

Manejo de errores entre servicios

Resiliencia

Retry

Timeout

Circuit Breaker con Resilience4j

Observabilidad

Endpoints disponibles:

/actuator/health
/actuator/info
/actuator/metrics
Estructura del proyecto
linktic-microservices-test
│
├── products
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
├── inventory
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend
│   ├── src
│   ├── Dockerfile
│   └── package.json
│
└── docker-compose.yml
Ejecución del proyecto
1. Compilar microservicios

Antes de levantar Docker es necesario generar los .jar.

Products service
cd products
mvn clean package -DskipTests
Inventory service
cd ../inventory
mvn clean package -DskipTests
2. Levantar el sistema completo

Desde la raíz del proyecto:

docker-compose up --build

Docker levantará automáticamente:

products-db

inventory-db

products-service

inventory-service

frontend

Acceso al sistema

Frontend:

http://localhost:5173
Puertos utilizados
Servicio	Puerto
Frontend	5173
Products Service	8081
Inventory Service	8082
Products DB	5433
Inventory DB	5434
Credenciales de acceso

Usuario:

admin

Contraseña:

admin123
Flujo básico de uso

Iniciar sesión

Consultar el listado de productos

Filtrar o buscar productos

Ver detalle del producto

Consultar inventario

Realizar compra

Seguridad

El sistema implementa dos niveles de seguridad:

Autenticación de usuario

Se utiliza JWT para autenticar usuarios desde el frontend.

Seguridad entre microservicios

Los microservicios se comunican mediante API Key.

Resiliencia

Las llamadas entre microservicios utilizan Resilience4j para manejar fallos:

Retry

Timeout

Circuit Breaker

Esto permite evitar fallos en cascada cuando un servicio no está disponible.

Notas adicionales

Cada microservicio utiliza su propia base de datos.

El frontend se conecta directamente a los microservicios mediante HTTP.

Docker Compose permite levantar toda la infraestructura con un solo comando.

Autor

Hugo Salcedo
Ingeniero de Software
Desarrollo Backend Java / Spring Boot
Full Stack Web Developer