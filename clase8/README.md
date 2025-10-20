# Despliegue y pruebas - Clase 8

## a) Descripción del proyecto
- Stack desplegado:
  - Frontend: Angular (aplicación estática servida por nginx) — deployment `frontend` / service `frontend-service`.
  - Backend: Servicio REST (Spring Boot/Java) — deployment `backend` / service `backend-service`.

- Conceptos aplicados:
  - Ingress (controlador para exponer rutas HTTP externas al clúster).
  - Probes de salud: liveness y readiness para validar estado de los contenedores.
  - HPA (Horizontal Pod Autoscaler) para escalar el backend según uso de CPU.
  - Services y Deployments para la gestión de pods y exposición interna.

---

## b) Instrucciones de despliegue

1) Iniciar Minikube y habilitar addons necesarios (desde Windows `cmd.exe` o una terminal):

```bash
minikube start
minikube addons enable ingress
minikube addons enable metrics-server
```

Resultado obtenido
```bash
➜  k8s minikube start
😄  minikube v1.37.0 on Ubuntu 24.04 (kvm/amd64)
✨  Using the docker driver based on existing profile
👍  Starting "minikube" primary control-plane node in "minikube" cluster
🚜  Pulling base image v0.0.48 ...
🔄  Restarting existing docker container for "minikube" ...
🐳  Preparing Kubernetes v1.34.0 on Docker 28.4.0 ...
🔎  Verifying Kubernetes components...
    ▪ Using image gcr.io/k8s-minikube/storage-provisioner:v5
🌟  Enabled addons: default-storageclass, storage-provisioner
🏄  Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default
➜  k8s minikube addons enable ingress
💡  ingress is an addon maintained by Kubernetes. For any concerns contact minikube on GitHub.
You can view the list of minikube maintainers at: https://github.com/kubernetes/minikube/blob/master/OWNERS
    ▪ Using image registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.6.2
    ▪ Using image registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.6.2
    ▪ Using image registry.k8s.io/ingress-nginx/controller:v1.13.2
🔎  Verifying ingress addon...
🌟  The 'ingress' addon is enabled
➜  k8s git:(main) ✗ minikube addons enable metrics-server
💡  metrics-server is an addon maintained by Kubernetes. For any concerns contact minikube on GitHub.
You can view the list of minikube maintainers at: https://github.com/kubernetes/minikube/blob/master/OWNERS
    ▪ Using image registry.k8s.io/metrics-server/metrics-server:v0.8.0
🌟  The 'metrics-server' addon is enabled
➜  k8s git:(main) ✗
```


2) Aplicar los manifiestos Kubernetes (desde la carpeta `clase8/k8s`):

```bash
cd clase8
kubectl apply -f k8s/
```
Resultado obtenido
```bash
➜  k8s git:(main) ✗ kubectl apply -f ../k8s/
deployment.apps/backend created
service/backend-service created
deployment.apps/frontend created
service/frontend-service created
horizontalpodautoscaler.autoscaling/backend-hpa created
ingress.networking.k8s.io/app-ingress created
```

3) Verificar recursos básicos:

```bash
kubectl get pods
```

Resultado obtenido:
```bash
➜  k8s git:(main) ✗ kubectl get pods
NAME                        READY   STATUS    RESTARTS   AGE
backend-b9b5cc476-6qvpq     1/1     Running   0          104s
backend-b9b5cc476-kx4gp     1/1     Running   0          104s
frontend-55b84b596d-c2dpp   1/1     Running   0          104s
frontend-55b84b596d-rrp54   1/1     Running   0         
```

```bash
kubectl get svc
```

Resultado obtenido:
```bash
➜  k8s git:(main) ✗ kubectl get svc
NAME               TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)   AGE
backend-service    ClusterIP   10.98.190.154   <none>        80/TCP    3m18s
frontend-service   ClusterIP   10.98.241.194   <none>        80/TCP    3m18s
kubernetes         ClusterIP   10.96.0.1       <none>        443/TCP   2d23h
```

```bash
kubectl get ingress
```

Resultado obtenido:
```bash
➜  k8s git:(main) ✗ kubectl get ingress
NAME          CLASS   HOSTS   ADDRESS        PORTS   AGE
app-ingress   nginx   *       192.168.49.2   80      3m45s
```

```bash
kubectl get hpa
```

Resultado obtenido:
```bash
➜  k8s git:(main) ✗ kubectl get hpa
NAME          REFERENCE            TARGETS       MINPODS   MAXPODS   REPLICAS   AGE
backend-hpa   Deployment/backend   cpu: 3%/50%   2         5         2          4m
```

4) Probar Ingress (ejemplo con Minikube):

- Obtener IP de Minikube:

```bash
minikube ip
```

Resultado obtenido:
```bash
➜  k8s git:(main) ✗ minikube ip
192.168.49.2
```


- Probar rutas del Ingress:

```bash
curl http://192.168.49.2/
```

Resultado obtenido:


```bash
curl http://192.168.49.2/api
```

5) Probar HPA con carga (ejemplo simple usando un Pod generador de carga):

- Comprobar el HPA del backend (nombre de ejemplo `backend-hpa`):

```bash
kubectl get hpa backend-hpa
```

- Crear un pod que haga peticiones continuas al backend para forzar escalado (ejemplo con busybox):

```bash
kubectl run -it --rm load-generator --image=busybox -- /bin/sh -c "while true; do wget -q -O- http://backend-service:8080/api >/dev/null; sleep 0.1; done"
```

Deja correr la carga durante 1-2 minutos y observa el HPA y los pods.

Nota: ajusta la URL/puerto (`backend-service:8080`) según la configuración de tus Services y puertos en los manifests.

---

## c) Comandos de verificación

Usa estos comandos para comprobar el estado del clúster y el comportamiento del HPA / Ingress:

```bash
kubectl get all
kubectl get ingress
kubectl get hpa
kubectl top pods
```

- `kubectl get all` muestra deployments, pods, services y más.
- `kubectl top pods` requiere `metrics-server` habilitado.

---

## d) Capturas de pantalla (recomendadas)

Toma y guarda capturas que muestren lo siguiente:

1. Ingress funcionando: salida de `curl http://<MINIKUBE_IP>/` y `curl http://<MINIKUBE_IP>/api`.
2. Health probes configurados: salida de `kubectl describe pod <POD_NAME>` mostrando `Liveness` y `Readiness` probes.
3. HPA en reposo: `kubectl get hpa` mostrando TARGETS por debajo del umbral (ej. 0%/50%).
4. HPA escalando bajo carga: `kubectl get hpa` mostrando TARGETS > 50% y aumento en `DESIRED`.
5. Pods escalados: `kubectl get pods` mostrando aumento de réplicas (por ejemplo de 2 a 4-5).

Consejo: para la captura del `describe pod`, puedes filtrar las secciones con:

```bash
kubectl describe pod <POD_NAME> | findstr /R "Liveness Readiness"
```

---

## e) Comandos de limpieza

Para eliminar los recursos creados por esta práctica (ajusta nombres si tus manifests usan otros):

```bash
kubectl delete ingress app-ingress
kubectl delete hpa backend-hpa
kubectl delete service frontend-service backend-service
kubectl delete deployment frontend backend
```

Si los recursos no existen con esos nombres, usa `kubectl get ingress,hpa,svc,deploy` para identificar los nombres reales y eliminarlos.

---

## Notas finales y buenas prácticas
- Verifica los nombres de `Service`, `Deployment` y `HPA` en tus manifiestos antes de ejecutar los comandos de prueba/limpieza.
- Ajusta los thresholds del HPA (target CPU) y recursos requests/limits en el `Deployment` del backend para conseguir un comportamiento de escalado realista.
- Para pruebas de carga más avanzadas considera usar herramientas como `hey`, `wrk` o `bombardier` desde un pod.

Si quieres, puedo:
- Adaptar las rutas exactas de `kubectl apply` según dónde estén tus manifests en el repo.
- Añadir ejemplos de manifiestos o un script de `load-test` más robusto.
