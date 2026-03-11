# Inventory Service

Microservicio encargado de gestionar inventario y compras.

Puerto: 8082

Funcionalidades:

- Registro de inventario
- Procesamiento de compras
- Prevención de stock negativo
- Operaciones idempotentes
- Comunicación con products-service

Endpoints principales:

POST /inventory  
POST /inventory/purchase  
GET /inventory/product/{productId}