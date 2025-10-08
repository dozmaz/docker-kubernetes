# Aplicación Multi-Contenedor con Docker Compose

**Curso:** Docker & Kubernetes - Clase 3
**Estudiante:** GUIDO CUTIPA YUJRA

Breve descripción (1-2 líneas) de qué hace.


## Stack

- **App:** SPRINGBOOT / JAVA / mongo-express
- **Base de datos:** MongoDB

## Ejecución

1. Clonar:
   ```bash
   git clone https://github.com/dozmaz/docker-kubernetes.git
   cd docker-kubernetes/clase3
    ```

2. Levantar Servicios:  
   ```bash
   docker compose up -d
   ```

3. Acceder:

API: http://localhost:3000


4. Cómo Probar

## Verificación

1. Servicios corriendo:
   ```bash
   docker compose ps
    ```
   
2. Acceder a la web: http://localhost:XXXX
3. Verificar volumen persiste:
````
docker compose down
docker compose up -d
docker volume ls  # debe seguir existiendo
````

4. Capturas de Pantalla
* Servicios corriendo
* API funcionando
