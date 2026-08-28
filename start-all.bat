@echo off
echo ========================================
echo   TRANS-SYNC — Iniciando todos los servicios
echo ========================================

echo.
echo [1/6] Levantando Docker (bases de datos)...
docker compose up -d
timeout /t 5 /nobreak > nul

echo.
echo [2/6] Iniciando ms-usuarios :8081...
start "ms-usuarios" cmd /k "cd ms-usuarios && mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak > nul

echo.
echo [3/6] Iniciando ms-viajes :8083...
start "ms-viajes" cmd /k "cd ms-viajes && mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak > nul

echo.
echo [4/6] Iniciando ms-pasajes :8082...
start "ms-pasajes" cmd /k "cd ms-pasajes && mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak > nul

echo.
echo [5/6] Iniciando ms-encomiendas :8084...
start "ms-encomiendas" cmd /k "cd ms-encomiendas && mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak > nul

echo.
echo [6/6] Iniciando ms-auditoria :8085...
start "ms-auditoria" cmd /k "cd ms-auditoria && mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak > nul

echo.
echo [7/7] Iniciando api-gateway :8080...
start "api-gateway" cmd /k "cd api-gateway && mvnw.cmd spring-boot:run"

echo.
echo ========================================
echo   Todos los servicios iniciando...
echo   Espera 30 segundos y abre:
echo   http://localhost:4200
echo ========================================
pause
