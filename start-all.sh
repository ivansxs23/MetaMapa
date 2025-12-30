#!/bin/bash

# Script para arrancar todos los microservicios en orden correcto

echo "=== DETENIENDO PROCESOS ANTERIORES (si hay) ==="
pkill -f 'java -jar' 2>/dev/null || true
sleep 3

echo "=== INICIANDO MICRO SERVICIOS EN ORDEN ==="

# Función para esperar a que un puerto esté abierto
wait_for_port() {
    local port=$1
    local name=$2
    echo "Esperando a que $name abra el puerto $port..."
    while ! nc -z localhost $port > /dev/null 2>&1; do
        sleep 3
    done
    echo "$name listo en puerto $port"
}

# 1. Fuente Estática (8082) - crítica para el agregador
nohup java -jar api-rest/fuenteEstatica/target/fuenteEstatica-0.0.1-SNAPSHOT.jar > fuenteEstatica.log 2>&1 &
wait_for_port 8082 "Fuente Estática"

# 2. Fuente Dinámica (8081)
nohup java -jar api-rest/fuenteDinamica/target/fuenteDinamica-0.0.1-SNAPSHOT.jar > fuenteDinamica.log 2>&1 &
wait_for_port 8081 "Fuente Dinámica"

# 3. Fuente Proxy (8083)
nohup java -jar api-rest/fuenteProxy/target/fuenteProxy-0.0.1-SNAPSHOT.jar > fuenteProxy.log 2>&1 &
wait_for_port 8083 "Fuente Proxy"

# 4. Auth (8085)
nohup java -jar api-rest/auth-usuario/target/auth-usuario-0.0.1-SNAPSHOT.jar > auth.log 2>&1 &
wait_for_port 8085 "Auth"

# 5. Agregador (8080) - ahora ya puede importar sin fallar
nohup java -jar api-rest/agregador/target/agregador-0.0.1-SNAPSHOT.jar > agregador.log 2>&1 &
wait_for_port 8080 "Agregador"

# 6. Estadísticas (8089)
nohup java -jar api-rest/estadistica/target/estadistica-0.0.1-SNAPSHOT.jar > estadistica.log 2>&1 &
wait_for_port 8089 "Estadísticas"

# 7. Frontend (web-client) - puerto 8087
nohup java -jar front/web-client/target/web-client-0.0.1-SNAPSHOT.jar > web-client.log 2>&1 &

echo " "
echo "=== TODOS LOS SERVICIOS ARRANCADOS CORRECTAMENTE ==="
echo "Frontend disponible en: http://$(curl -s http://checkip.amazonaws.com):8087"
echo " "
echo "Logs disponibles:"
echo "  tail -f fuenteEstatica.log"
echo "  tail -f agregador.log"
echo "  tail -f web-client.log"
echo "  etc."
echo " "
echo "Para detener todo: pkill -f 'java -jar'"