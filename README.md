# 🧪 Prueba Técnica Full Stack - Inventario y Productos

Aplicación full stack para la gestión de productos e inventario, desarrollada con arquitectura de microservicios.

Incluye:

- Microservicio de productos
- Microservicio de inventario
- Frontend en Vue 3
- Autenticación con JWT
- Compras con idempotencia
- Orquestación de servicios con Docker Compose

---

## 🚀 Tecnologías utilizadas

### Backend
- Java 17
- Spring Boot
- Spring Security con JWT
- PostgreSQL
- Maven

### Frontend
- Vue 3
- Vite
- Pinia
- Tailwind CSS
- Axios

### Infraestructura
- Docker
- Docker Compose

---

## 📁 Estructura del proyecto

```text
linktic-prueba-tecnica/
├── frontend/
├── products/
├── inventory/
├── docker-compose.yml
└── README.md

⚙️ Ejecuci��n del proyecto con Docker
Requisitos previos

Antes de iniciar, asegúrate de tener instalado:

Docker

Docker Compose

▶️ Levantar todo el proyecto

Desde la raíz del proyecto, ejecuta:

docker compose up --build

Esto levantará:
- Frontend
- Microservicio de productos
- Microservicio de inventario
- Bases de datos necesarias para los servicios.

🌐 Puertos esperados
- Frontend: http://localhost:5173
- Products Service: http://localhost:8081
- Inventory Service: http://localhost:8082

🔐 Autenticación

La aplicación usa autenticación con JWT.

Credenciales de prueba
usuario: admin
contraseña: admin123

📌 Funcionalidades implementadas

Productos
- Listado de productos con paginación
- Filtro por búsqueda (name o sku) mediante parámetro search
- Filtro por estado
- Creación de productos
- Consulta de detalle de producto

Inventario
- Consulta de inventario por producto
- Creación de inventario
- Visualización de stock disponible

Compras
- Compra de productos
- Validación de stock disponible
- Manejo de errores funcionales
- Idempotencia mediante header Idempotency-Key

🔎 Endpoints principales
Products Service

Base URL:
http://localhost:8081

Obtener productos
GET /products?page=0&size=5&search=texto&status=ACTIVE

Obtener producto por ID
GET /products/{id}

Crear producto
POST /products

Inventory Service
Base URL:
http://localhost:8082

Obtener inventario por producto
GET /inventory/{productId}

Crear inventario
POST /inventory

Comprar producto
POST /inventory/purchase

Header requerido:
Idempotency-Key: unique-value

🖥�?Flujo sugerido de prueba

Una vez levantado el proyecto con Docker:
1.) Iniciar sesión en el frontend
2.) Crear un producto
3.) Crear inventario para ese producto
4.) Consultar el detalle del producto
5.) Realizar una compra
6.) Validar que el stock disminuye correctamente

🎨 Mejoras aplicadas en frontend

1.) Interfaz construida con Tailwind CSS
2.) Manejo de estado con Pinia
3.) Alertas reutilizables
4.) Loaders reutilizables
5.) Estados vacíos amigables
6.) Validaciones visuales por campo
7.) Botones con estado de carga y deshabilitado
8.) Navegación protegida por autenticación

👨‍�?Autor
Desarrollado por Hugo Salcedo