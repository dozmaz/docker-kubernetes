# Título: "Docker Microservicios con Cache"


### 1. Título y Descripción
- Nombre del proyecto: **Docker Microservicios con Cache**
- Descripción breve de qué hace:
    Aplicación de gestión de productos con arquitectura de microservicios, utilizando un gateway para enrutar solicitudes, un servicio backend desarrollado en Spring Boot, una base de datos MongoDB para persistencia de datos y Redis para caching. La aplicación incluye un frontend en Angular servido a través de Nginx y un administrador de base de datos mongo-express.
- Tecnologías utilizadas: Docker, Docker Compose, Node.js, Express, Redis, MongoDB, Nginx, spring boot

### 2. Arquitectura
```
Diagrama ASCII mostrando:
- Cliente
- Gateway (Nginx)
- Servicios backend
- Redis
- Base de datos
- Redes Docker
```

```
                              +-------------------+
                              |     Cliente       |
                              +--------+----------+
                                       |
                                       v
                              +--------+----------+
                              |   Gateway (Nginx) |  <--- [Red: frontend]
                              +--------+----------+
                                       |
         +-----------------------------+---------------------------+
         |                             |                           |
         v                             v                           v
+--------+----------+        +---------+---------+       +---------+---------+
|  Angular Frontend |        |  mongo-express    |       | Servicios Backend |  <--- [Red: backend]
+--------+----------+        +-------------------+       |   (Spring Boot)   |
                                      |                  +---------+---------+
                                      |                            |
                                      |                            v
                                      |                  +---------+---------+
                                      |                  |      Redis        |  <--- [Red: backend]
                                      |                  +---------+---------+
                                      |                            |
                                      |                            v
                                      |                  +---------+---------+
                                      |----------------> |     MongoDB       |  <--- [Red: backend]
                                                         +---------+---------+

```

### 3. Servicios

Tabla describiendo cada servicio:

| Servicio | Tecnología      | Puerto | Descripción                   |
|----------|-----------------|--------|-------------------------------|
| gateway  | Nginx           | 8080 | API Gateway                   |
| backend  | SpringBoot      | 5000 | API principal                 |
| redis    | Redis           | 6379 | Cache                         |
| db       | MongoDB         | 27017 | Base de datos                 |
| admin DB  | mongo-express   | 80 | Administrador de base de datos |
| frontend | Nginx + Angular | 80 | Interfaz web                  |

### 4. Instrucciones de Uso

```bash
# Clonar repositorio
git clone https://github.com/dozmaz/docker-kubernetes.git


cd docker-kubernetes/clase4

# Levantar servicios
docker compose up -d

# Verificar estado
docker compose ps

# Ver logs
docker compose logs -f

# Acceder a la aplicación
http://localhost:8080
```

### 5. Endpoints de la API

```
GET /api/productos
Descripción: Lista todos los productos
Response: { "source": "cache|database", "data": [
  {
    "id": "68ec8de558aa580e43745e92",
    "nombre": "Manzana",
    "precio": 1.5
  },
  {
    "id": "68ec8de558aa580e43745e93",
    "nombre": "Banana",
    "precio": 2.0
  },
  {
    "id": "68ec8de558aa580e43745e94",
    "nombre": "Naranja",
    "precio": 1.8
  }
] }
```

```
DELETE /api/productos/68ec986e75dc805541fc4ec1
Descripción: Eliminar un producto por ID
Response: { null } 200 OK
```

```
POST /api/productos
Descripción: crear un producto
payload: {"nombre":"sandia","precio":100.0}
Response: {"id":"68eca68e3dbb0648fdb2260f","nombre":"pera","precio":100.0}
```
![img_6.png](screenshots/img_6.png)


### 6. Capturas de Pantalla

Incluye mínimo:
- Frontend funcionando

![img.png](screenshots/img.png)

- Resultado de `docker compose ps`

![img_1.png](screenshots/img_1.png)

- Logs mostrando conexión a Redis y DB

![img_2.png](screenshots/img_2.png)

![img_3.png](screenshots/img_3.png)

- Respuesta de API con `"source": "cache"`

![img_5.png](screenshots/img_5.png)

- Respuesta de API con `"source": "database"`

![img_4.png](screenshots/img_4.png)

## Parte 5: Pruebas a Realizar

Documenta los resultados de:

### 1. Cache Hit/Miss
```bash
# Primera consulta (cache MISS)
curl http://localhost:8080/api/productos
# Resultado: [{"id":"68ec8de558aa580e43745e92","nombre":"Manzana","precio":1.5},{"id":"68ec8de558aa580e43745e93","nombre":"Banana","precio":2.0},{"id":"68ec8de558aa580e43745e94","nombre":"Naranja","precio":1.8},{"id":"68ec981675dc805541fc4ebf","nombre":"banana","precio":100.0},{"id":"68eca93d4f42e83b0a9319af","nombre":"uva","precio":200.0}]

# Segunda consulta (cache HIT)
curl http://localhost:8080/api/productos
# Resultado: [{"id":"68ec8de558aa580e43745e92","nombre":"Manzana","precio":1.5},{"id":"68ec8de558aa580e43745e93","nombre":"Banana","precio":2.0},{"id":"68ec8de558aa580e43745e94","nombre":"Naranja","precio":1.8},{"id":"68ec981675dc805541fc4ebf","nombre":"banana","precio":100.0},{"id":"68eca93d4f42e83b0a9319af","nombre":"uva","precio":200.0}]
```

![img_17.png](screenshots/img_17.png)

![img_7.png](screenshots/img_7.png)

Captura los logs mostrando "Cache MISS" y "Cache HIT"

### 2. Invalidación de Cache
```bash
# Crear nuevo producto
curl --location 'http://localhost:8080/api/productos' \
--header 'Content-Type: application/json' \
--data '{"nombre":"sandia","precio":100.0}'
```
#### Cache de 30 segundos

![img_15.png](screenshots/img_15.png)

#### invalidación de cache
![img_14.png](screenshots/img_14.png)

#### resultado
![img_8.png](screenshots/img_8.png)

```bash
# Verificar que cache se invalidó
curl http://localhost:8080/api/productos
```

![img_9.png](screenshots/img_9.png)


### 3. Persistencia de Datos
```bash
# Crear datos
curl -X POST ...
```
![img_10.png](screenshots/img_10.png)

```bash
# Detener servicios
docker compose down

# Levantar de nuevo
docker compose up -d
```

![img_11.png](screenshots/img_11.png)

```bash
# Verificar que datos persisten
curl http://localhost:8080/api/productos
```

![img_12.png](screenshots/img_12.png)

#### Mongo-express

http://localhost:8080/mongo-express/db/test/productos

![img_16.png](screenshots/img_16.png)


### 4. Gateway Routing
```bash
# Verificar que gateway rutea correctamente
curl http://localhost:8080/gateway/health
curl http://localhost:8080/api/productos/health
curl http://localhost:8080/  # Debe mostrar frontend
```

![img_13.png](screenshots/img_13.png)

