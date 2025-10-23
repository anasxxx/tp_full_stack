# TP Full Stack - Application de Gestion de Voitures

Application web full-stack pour la gestion de voitures et propriétaires, avec monitoring et métriques.

## Architecture

- **Backend**: Spring Boot 3.2.5 (Java 17) avec Spring Data JPA, Spring Security
- **Frontend**: React 17 avec Bootstrap 4
- **Base de données**: MariaDB 11.2
- **Monitoring**: Prometheus + Grafana
- **Conteneurisation**: Docker Compose (5 services)

## Prérequis

- Docker Desktop installé et en cours d'exécution
- Git (pour cloner le projet)
- Ports disponibles: 80, 3000, 3306, 8081, 9090

## Installation et Démarrage

### 1. Cloner le repository

```bash
git clone <votre-repo-url>
cd tp_full_stack
```

### 2. Démarrer tous les services avec Docker Compose

```bash
docker-compose up -d
```

Cette commande va:
- Construire les images Docker pour le backend et frontend
- Télécharger les images MariaDB, Prometheus et Grafana
- Créer et démarrer les 5 conteneurs
- Initialiser la base de données avec des données de test

### 3. Attendre l'initialisation (environ 30-60 secondes)

Vérifier que tous les services sont démarrés:

```bash
docker-compose ps
```

Tous les services doivent afficher le status "Up" ou "Up (healthy)".

### 4. Accéder aux services

| Service | URL | Credentials |
|---------|-----|-------------|
| **Frontend** | http://localhost | - |
| **Backend API** | http://localhost:8081/voitures | user / password |
| **Swagger UI** | http://localhost:8081/swagger-ui.html | user / password |
| **Prometheus** | http://localhost:9090 | - |
| **Grafana** | http://localhost:3000 | admin / admin |

## Fonctionnalités

### Application Web (Frontend)

- **Liste des voitures**: Affichage de toutes les voitures avec leurs propriétaires
- **Ajouter une voiture**: Formulaire pour créer une nouvelle voiture
- **Modifier une voiture**: Édition des informations d'une voiture existante
- **Supprimer une voiture**: Suppression d'une voiture
- **Sélection du propriétaire**: Dropdown pour associer une voiture à un propriétaire

### API REST (Backend)

Endpoints disponibles:

- `GET /voitures` - Liste toutes les voitures
- `GET /voitures/{id}` - Récupère une voiture par ID
- `POST /voitures` - Crée une nouvelle voiture
- `PUT /voitures/{id}` - Modifie une voiture
- `DELETE /voitures/{id}` - Supprime une voiture
- `GET /proprietaires` - Liste tous les propriétaires

Documentation complète disponible sur Swagger UI.

### Monitoring et Métriques

#### Prometheus (http://localhost:9090)

Collecte automatique des métriques du backend Spring Boot:
- Nombre de requêtes HTTP
- Temps de réponse
- Utilisation CPU et mémoire JVM
- Nombre de threads actifs

#### Grafana (http://localhost:3000)

Dashboard préconfigurée: "Spring Boot Application Metrics"

Panels disponibles:
1. **HTTP Request Rate** - Taux de requêtes par seconde
2. **Average Response Time** - Temps de réponse moyen
3. **CPU Usage** - Utilisation CPU en %
4. **Memory Usage** - Utilisation mémoire JVM en %
5. **Active Threads** - Nombre de threads actifs

## Générer du Trafic pour les Métriques

Pour voir les métriques dans Grafana, générer du trafic HTTP:

### Windows
```cmd
generate_traffic.bat
```

### Linux/Mac
```bash
chmod +x generate_traffic.sh
./generate_traffic.sh
```

### Manuellement
```bash
curl -u user:password http://localhost:8081/voitures
curl -u user:password http://localhost:8081/proprietaires
```

Après avoir généré du trafic, attendre 10-15 secondes et rafraîchir le dashboard Grafana.

## Structure du Projet

```
tp_full_stack/
├── backend/                    # Application Spring Boot
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── org/cours/
│   │       │       ├── config/         # Configuration (Security, etc.)
│   │       │       ├── model/          # Entités JPA (Voiture, Proprietaire)
│   │       │       ├── repo/           # Repositories Spring Data
│   │       │       └── web/            # Contrôleurs REST
│   │       └── resources/
│   │           ├── application.properties
│   │           └── application-docker.properties
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/                   # Application React
│   ├── public/
│   ├── src/
│   │   ├── components/         # Composants React
│   │   │   ├── App.js
│   │   │   ├── Voiture.js      # Formulaire création/édition
│   │   │   ├── VoitureListe.js # Liste des voitures
│   │   │   ├── NavigationBar.js
│   │   │   ├── Footer.js
│   │   │   └── Bienvenue.js
│   │   └── api.js              # Configuration Axios
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── docker/                     # Configuration Docker
│   ├── grafana/
│   │   ├── provisioning/
│   │   │   ├── datasources/
│   │   │   │   └── datasource.yml
│   │   │   └── dashboards/
│   │   │       └── dashboard.yml
│   │   └── dashboards/
│   │       └── spring-boot-dashboard.json
│   └── prometheus/
│       └── prometheus.yml
│
├── docker-compose.yml          # Orchestration des 5 services
├── generate_traffic.bat        # Script Windows pour générer du trafic
├── RESTART_CLEAN.bat          # Script pour redémarrage complet
├── GUIDE_TEST.md              # Guide de test détaillé
├── CLAUDE.md                  # Documentation technique
└── README.md                  # Ce fichier
```

## Commandes Utiles

### Gestion des conteneurs

```bash
# Démarrer tous les services
docker-compose up -d

# Arrêter tous les services
docker-compose down

# Voir les logs d'un service
docker-compose logs -f backend
docker-compose logs -f frontend

# Redémarrer un service
docker-compose restart backend

# Reconstruire et redémarrer
docker-compose up -d --build

# Arrêter et supprimer volumes (clean restart)
docker-compose down -v
```

### Vérifier l'état des services

```bash
# Status de tous les conteneurs
docker-compose ps

# Logs du backend
docker logs voitures-backend

# Logs de Grafana
docker logs voitures-grafana

# Accéder à la base de données
docker exec -it voitures-mariadb mysql -u voitureuser -pvoiturepass voituresdb
```

### Rebuild complet (si problèmes)

Windows:
```cmd
RESTART_CLEAN.bat
```

Ou manuellement:
```bash
docker-compose down -v
docker-compose build --no-cache backend
docker-compose up -d
```

## Données de Test

Au démarrage, le backend initialise automatiquement la base de données avec:

**Propriétaires:**
- Jean Dupont
- Marie Martin
- Pierre Durant

**Voitures:**
- Peugeot 308 (Jean Dupont)
- Renault Clio (Marie Martin)
- Citroën C3 (Pierre Durant)
- Tesla Model 3 (Jean Dupont)
- BMW X5 (Marie Martin)
- Audi A4 (Pierre Durant)

## Troubleshooting

### Le frontend affiche une erreur de connexion

Vérifier que le backend est démarré:
```bash
curl -u user:password http://localhost:8081/voitures
```

### Prometheus ne collecte pas les métriques

Vérifier les targets Prometheus:
```bash
curl http://localhost:9090/api/v1/targets
```

Le target "spring-boot-app" doit être "up".

### Grafana affiche "No data"

1. Vérifier que Prometheus collecte les métriques (voir ci-dessus)
2. Générer du trafic avec `generate_traffic.bat`
3. Attendre 10-15 secondes et rafraîchir

### Port déjà utilisé

Si un port est déjà utilisé, modifier le mapping dans `docker-compose.yml`:

Exemple pour changer le port backend de 8081 à 8082:
```yaml
backend:
  ports:
    - "8082:8080"  # au lieu de "8081:8080"
```

Puis mettre à jour `frontend/.env.production` avec la nouvelle URL.

### Base de données vide après redémarrage

C'est normal si vous utilisez `docker-compose down -v` qui supprime les volumes.

Pour conserver les données entre redémarrages, utilisez simplement:
```bash
docker-compose down
docker-compose up -d
```

## Technologies Utilisées

### Backend
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Data REST
- Spring Security (HTTP Basic)
- Spring Boot Actuator
- Micrometer (Prometheus metrics)
- MariaDB Driver
- Lombok
- SpringDoc OpenAPI (Swagger)

### Frontend
- React 17
- React Router 5
- Bootstrap 4
- React Bootstrap
- Axios
- Font Awesome
- Nginx (production server)

### DevOps & Monitoring
- Docker & Docker Compose
- Prometheus 2.48.1
- Grafana 10.2.3
- MariaDB 11.2

## Auteur

Projet réalisé dans le cadre du cours de développement full-stack.

## Licence

Ce projet est à usage éducatif uniquement.
