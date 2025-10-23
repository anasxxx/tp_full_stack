@echo off
echo ============================================
echo CLEAN RESTART - All Services
echo ============================================
echo.

echo Step 1: Stopping and removing all containers and volumes...
docker-compose down -v

echo.
echo Step 2: Rebuilding backend with new configuration...
docker-compose build --no-cache backend

echo.
echo Step 3: Starting all services...
docker-compose up -d

echo.
echo Step 4: Waiting for services to initialize (30 seconds)...
timeout /t 30

echo.
echo Step 5: Checking service status...
docker-compose ps

echo.
echo Step 6: Checking if data was inserted...
docker exec -it voitures-mariadb mysql -u voitureuser -pvoiturepass -e "SELECT COUNT(*) as total_voitures FROM voituresdb.voitures;"

echo.
echo ============================================
echo Services URLs:
echo ============================================
echo Frontend:    http://localhost
echo Backend:     http://localhost:8081/voitures
echo Prometheus:  http://localhost:9090
echo Grafana:     http://localhost:3000
echo ============================================
echo.
echo To view logs: docker-compose logs -f backend
echo.
pause
