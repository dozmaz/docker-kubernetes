```cmd
minikube start
minikube addons enable ingress
minikube addons enable metrics-server
```

2. Aplicar los manifiestos:

```cmd
kubectl apply -f tarea-clase8-k8s/k8s/
```

3. Verificar recursos:

```cmd
kubectl get pods
kubectl get svc
kubectl get ingress
kubectl get hpa
```

4. Probar (ejemplo con minikube):

- Obtener IP de minikube y probar ingress:

```cmd
minikube ip
curl http://<MINIKUBE_IP>/
curl http://<MINIKUBE_IP>/api
```

Si usas otra instancia (no minikube), adapta los pasos para tu controlador de ingress.

