# Guide de Test - Prometheus & Grafana

## URLs des Services

- **Frontend**: http://localhost
- **Backend API**: http://localhost:8081/voitures
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000
- **MariaDB**: localhost:3306

## Credentials

- **Backend API**: user / password
- **Grafana**: admin / admin
- **MariaDB**: voitureuser / voiturepass

---

## 1. Test de Prometheus

### Vérifier que Prometheus est accessible
Ouvrir dans le navigateur: http://localhost:9090

### Vérifier les Targets
1. Aller sur http://localhost:9090/targets
2. Vérifier que les deux targets sont UP:
   - **prometheus** (self-monitoring)
   - **spring-boot-app** (notre backend)

### Tester les Requêtes PromQL
Dans l'interface Prometheus (http://localhost:9090/graph):

1. **Nombre total de requêtes HTTP**:
   ```
   http_server_requests_seconds_count
   ```

2. **Requêtes par endpoint**:
   ```
   sum by (uri) (http_server_requests_seconds_count)
   ```

3. **Temps de réponse moyen**:
   ```
   rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m])
   ```

4. **Utilisation mémoire JVM**:
   ```
   jvm_memory_used_bytes
   ```

5. **Threads actifs**:
   ```
   jvm_threads_live_threads
   ```

---

## 2. Test de Grafana

### Se connecter à Grafana
1. Ouvrir http://localhost:3000
2. Se connecter avec: **admin** / **admin**
3. (Optionnel) Changer le mot de passe ou cliquer "Skip"

### Vérifier la Data Source Prometheus
1. Menu (☰) → Configuration → Data Sources
2. Vérifier que "Prometheus" est configuré
3. URL devrait être: http://prometheus:9090
4. Cliquer "Save & Test" → devrait afficher "Data source is working"

### Accéder au Dashboard
1. Menu (☰) → Dashboards → Browse
2. Cliquer sur "Spring Boot Application Metrics"
3. OU directement: http://localhost:3000/d/spring-boot-voitures/spring-boot-application-metrics

### Panels du Dashboard

Le dashboard contient 5 panels:

1. **HTTP Request Rate** (Graphique)
   - Montre le nombre de requêtes HTTP par seconde
   - Groupé par URI (/voitures, /proprietaires, etc.)

2. **Average Response Time** (Graphique)
   - Temps de réponse moyen en millisecondes
   - Par endpoint

3. **CPU Usage** (Gauge)
   - Utilisation CPU du processus JVM
   - Valeurs entre 0-100%

4. **Memory Usage** (Gauge)
   - Mémoire JVM utilisée en MB
   - Affiche la mémoire heap utilisée

5. **Active Threads** (Stat)
   - Nombre de threads actifs dans la JVM

---

## 3. Générer du Trafic

### Méthode 1: Script Batch (Windows)
```cmd
generate_traffic.bat
```
Génère 50 requêtes vers le backend

### Méthode 2: Commandes curl manuelles
```bash
curl -u user:password http://localhost:8081/voitures
curl -u user:password http://localhost:8081/proprietaires
```

### Méthode 3: Via le Frontend
1. Ouvrir http://localhost
2. Naviguer vers "Liste des voitures"
3. Ajouter/modifier/supprimer des voitures
4. Chaque action génère des requêtes HTTP

---

## 4. Vérifications à Faire

### ✅ Checklist Prometheus

- [ ] Prometheus accessible sur http://localhost:9090
- [ ] Target "spring-boot-app" est UP
- [ ] Requête `http_server_requests_seconds_count` retourne des données
- [ ] Les métriques JVM sont disponibles (`jvm_memory_used_bytes`)

### ✅ Checklist Grafana

- [ ] Grafana accessible sur http://localhost:3000
- [ ] Login admin/admin fonctionne
- [ ] Data source "Prometheus" configurée et testée
- [ ] Dashboard "Spring Boot Application Metrics" visible
- [ ] Les 5 panels affichent des données (pas "No data")

### ✅ Checklist Métriques en Temps Réel

1. Ouvrir le dashboard Grafana
2. Exécuter `generate_traffic.bat`
3. Actualiser le dashboard (ou attendre l'auto-refresh)
4. Vérifier que:
   - [ ] HTTP Request Rate augmente
   - [ ] Les graphiques se mettent à jour
   - [ ] Les valeurs CPU/Memory sont affichées

---

## 5. Dépannage

### Prometheus ne collecte pas les métriques

**Vérifier le target**:
```bash
curl http://localhost:9090/api/v1/targets
```

Si le target est DOWN:
- Vérifier que le backend est accessible: `curl -u user:password http://localhost:8081/actuator/prometheus`
- Vérifier les logs du backend: `docker logs voitures-backend`

### Grafana affiche "No data"

1. Vérifier que Prometheus collecte des données (voir ci-dessus)
2. Vérifier la data source dans Grafana: Configuration → Data Sources → Prometheus → "Save & Test"
3. Générer du trafic avec `generate_traffic.bat`
4. Attendre 10-15 secondes et rafraîchir le dashboard

### Backend ne répond pas

```bash
docker logs voitures-backend
docker restart voitures-backend
```

---

## 6. Commandes Utiles

### Vérifier l'état des services
```bash
docker-compose ps
```

### Voir les logs
```bash
docker logs voitures-backend
docker logs voitures-prometheus
docker logs voitures-grafana
```

### Redémarrer un service
```bash
docker restart voitures-backend
docker restart voitures-prometheus
docker restart voitures-grafana
```

### Tester l'API directement
```bash
curl -u user:password http://localhost:8081/voitures
curl -u user:password http://localhost:8081/proprietaires
curl -u user:password http://localhost:8081/actuator/health
curl -u user:password http://localhost:8081/actuator/prometheus
```

---

## Résultat Attendu

Après avoir généré du trafic, vous devriez voir dans Grafana:
- **HTTP Request Rate**: Pics d'activité correspondant aux requêtes générées
- **Response Time**: Temps de réponse entre 5-50ms généralement
- **CPU Usage**: Entre 5-20% en activité normale
- **Memory Usage**: ~200-400 MB pour l'application
- **Active Threads**: ~20-30 threads

Tous les panels doivent afficher des données, pas de message "No data".
