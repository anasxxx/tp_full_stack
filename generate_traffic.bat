@echo off
echo ========================================
echo Generating Traffic for Grafana Metrics
echo ========================================
echo.

echo Generating 50 API requests...
echo.

FOR /L %%i IN (1,1,50) DO (
    echo Request %%i of 50...
    curl -s -u user:password http://localhost:8081/voitures > nul 2>&1
    curl -s -u user:password http://localhost:8081/proprietaires > nul 2>&1
    timeout /t 1 /nobreak > nul
)

echo.
echo ========================================
echo Traffic Generated Successfully!
echo ========================================
echo.
echo Wait 10 seconds, then check Grafana:
echo http://localhost:3000
echo.
echo Dashboard: Spring Boot Application Metrics
echo.
pause
