@echo off
echo Stopping any existing containers...
docker-compose down

echo Building frontend with new configuration...
docker-compose build --no-cache frontend

echo Starting all services...
docker-compose up -d

echo.
echo Waiting for services to start...
timeout /t 10

echo.
echo Checking service status...
docker-compose ps

echo.
echo ========================================
echo Services are starting!
echo ========================================
echo Frontend:    http://localhost
echo Backend:     http://localhost:8081
echo Prometheus:  http://localhost:9090
echo Grafana:     http://localhost:3000
echo ========================================
echo.
echo To view logs: docker-compose logs -f
echo To stop all:  docker-compose down
echo.
pause
