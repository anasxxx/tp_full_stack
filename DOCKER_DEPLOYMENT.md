# Docker Deployment Guide

## Architecture

The application is deployed using Docker Compose with 5 containers:

1. **MariaDB** - Database (Port 3306)
2. **Backend** - Spring Boot API (Port 8080)
3. **Frontend** - React/Nginx (Port 80)
4. **Prometheus** - Metrics collection (Port 9090)
5. **Grafana** - Monitoring dashboards (Port 3000)

## Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+
- 4GB RAM minimum
- 10GB disk space

## Quick Start

### Build and Start All Services

```bash
docker-compose up -d
```

### Check Services Status

```bash
docker-compose ps
```

### View Logs

```bash
docker-compose logs -f
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Stop All Services

```bash
docker-compose down
```

### Stop and Remove Volumes

```bash
docker-compose down -v
```

## Service URLs

| Service | URL | Credentials |
|---------|-----|-------------|
| Frontend | http://localhost | - |
| Backend API | http://localhost:8081 | user / password |
| Swagger UI | http://localhost:8081/swagger-ui.html | user / password |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin / admin |
| MariaDB | localhost:3306 | voitureuser / voiturepass |

## Data Persistence

Data is persisted in Docker volumes:

- `mariadb_data` - Database files
- `prometheus_data` - Metrics data
- `grafana_data` - Dashboards and settings

## Monitoring

### Prometheus Metrics

Access metrics at: http://localhost:9090

Query examples:
- `rate(http_server_requests_seconds_count[1m])` - Request rate
- `jvm_memory_used_bytes` - Memory usage
- `process_cpu_usage` - CPU usage

### Grafana Dashboards

1. Open http://localhost:3000
2. Login: admin / admin
3. Navigate to "Dashboards"
4. Open "Spring Boot Application Metrics"

Dashboard shows:
- HTTP request rate
- Average response time
- CPU usage
- Memory usage
- Active threads

## Database Access

### Using MySQL Client

```bash
mysql -h localhost -P 3306 -u voitureuser -pvoiturepass voituresdb
```

### Using Docker Exec

```bash
docker exec -it voitures-mariadb mysql -u voitureuser -pvoiturepass voituresdb
```

## Troubleshooting

### Container Won't Start

```bash
docker-compose logs <service-name>
docker-compose ps
```

### Database Connection Issues

```bash
docker-compose restart mariadb
docker-compose logs mariadb
```

### Reset Everything

```bash
docker-compose down -v
docker-compose up -d --build
```

### Port Already in Use

Change ports in `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"
```

## Development vs Production

### Development

```bash
docker-compose up
```

### Production

```bash
docker-compose up -d --build
docker-compose logs -f
```

## Health Checks

All services have health checks configured:

```bash
docker inspect voitures-backend | grep -A 10 "Health"
```

## Scaling

Scale specific services:

```bash
docker-compose up -d --scale backend=3
```

## Backup

### Database Backup

```bash
docker exec voitures-mariadb mysqldump -u voitureuser -pvoiturepass voituresdb > backup.sql
```

### Restore Database

```bash
docker exec -i voitures-mariadb mysql -u voitureuser -pvoiturepass voituresdb < backup.sql
```

## Network

All containers are on the same Docker network: `voitures-network`

```bash
docker network inspect voitures-network
```

## Resource Limits

Add resource limits in `docker-compose.yml`:

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M
```

## Environment Variables

Override in `docker-compose.yml` or create `.env` file:

```env
MYSQL_ROOT_PASSWORD=newrootpass
MYSQL_PASSWORD=newpass
GF_SECURITY_ADMIN_PASSWORD=newadminpass
```

## Build Images Separately

```bash
docker build -t voitures-backend ./backend
docker build -t voitures-frontend ./frontend
```

## Push to Registry

```bash
docker tag voitures-backend:latest myregistry/voitures-backend:latest
docker push myregistry/voitures-backend:latest
```
