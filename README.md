# TP Full Stack - Gestion de Voitures

Application web de gestion de voitures avec Spring Boot, React, et monitoring Grafana/Prometheus.

## Prérequis

- Docker Desktop
- Ports libres: 80, 3000, 3306, 8081, 9090

## Démarrage Rapide

```bash
git clone https://github.com/anasxxx/tp_full_stack.git
cd tp_full_stack
docker-compose up -d
```

Attendre environ 1 minute pour l'initialisation.

## Accès aux Services

- **Application web**: http://localhost
- **API REST**: http://localhost:8081/voitures (user/password)
- **Swagger**: http://localhost:8081/swagger-ui.html
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090

## Générer du Trafic (pour Grafana)

```bash
generate_traffic.bat
```

Puis aller sur Grafana → Dashboard "Spring Boot Application Metrics"

## Commandes Docker

```bash
# Démarrer
docker-compose up -d

# Arrêter
docker-compose down

# Voir les logs
docker-compose logs -f backend

# Redémarrer proprement
docker-compose down -v
docker-compose up -d --build
```

## Fonctionnalités

- CRUD complet sur les voitures
- Gestion des propriétaires
- API REST documentée (Swagger)
- Métriques temps réel (Prometheus)
- Dashboard de monitoring (Grafana)
- Base de données MariaDB

## Architecture

- Backend: Spring Boot 3.2.5 + JPA + Security
- Frontend: React 17 + Bootstrap
- BDD: MariaDB 11.2
- Monitoring: Prometheus + Grafana
- Déploiement: Docker Compose (5 conteneurs)

## Technologies

**Backend**: Spring Boot, Spring Data JPA, Spring Security, Actuator, Micrometer, Lombok

**Frontend**: React, React Router, Bootstrap, Axios, Nginx

**DevOps**: Docker, Docker Compose, Prometheus, Grafana, MariaDB
