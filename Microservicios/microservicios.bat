@echo off
set "ROOT=C:\Users\Administrator\Tienda-Clan-Supply-App-Movil-\Microservicios\apigateway"
start "ApiGateway" cmd /k "cd /d "%ROOT%" && gradlew.bat bootRun"
