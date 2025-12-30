#!/bin/bash
sudo swapon /swapfile
pkill -f 'java -jar'
export API_BASE_URL=$(curl -s ifconfig.me)
echo "Arrancando con la IP: $API_BASE_URL"
nohup java -Xmx128M -jar api-rest/auth-usuario/target/auth-usuario-0.0.1-SNAPSHOT.jar > auth.log 2>&1 &
nohup java -Xmx128M -jar api-rest/fuenteEstatica/target/fuenteEstatica-0.0.1-SNAPSHOT.jar > estatica.log 2>&1 &
nohup java -Xmx128M -jar api-rest/fuenteDinamica/target/fuenteDinamica-0.0.1-SNAPSHOT.jar > dinamica.log 2>&1 &
nohup java -Xmx128M -jar api-rest/fuenteProxy/target/fuenteProxy-0.0.1-SNAPSHOT.jar > proxy.log 2>&1 &
nohup java -Xmx128M -jar api-rest/estadistica/target/estadistica-0.0.1-SNAPSHOT.jar > estadistica.log 2>&1 &
nohup java -Xmx150M -jar api-rest/agregador/target/agregador-0.0.1-SNAPSHOT.jar > agregador.log 2>&1 &
nohup java -Xmx150M -jar front/web-client/target/web-client-0.0.1-SNAPSHOT.jar > webclient.log 2>&1 &
echo "Servicios lanzados en segundo plano."
