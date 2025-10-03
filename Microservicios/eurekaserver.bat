@echo off
set "ROOT=C:\Users\Administrator\Tienda-Clan-Supply-App-Movil-\Microservicios\eurekaserver"
start "EurekaServer" cmd /k "cd /d "%ROOT%" && gradlew.bat bootRun"
