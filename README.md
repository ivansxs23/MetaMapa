Comando para correr en docker-compose:

docker compose down -v
docker compose up --build


mvn clean install -DskipTests -Dmaven.test.skip=true -Dcheckstyle.skip=true
mvn clean package

# en numeros ponemos el numero de la ip publica"
export API_BASE_URL=numeros

# Arrancá todos a la vez en background o con screen/tmux
java -jar api-rest/fuenteEstatica/target/fuenteEstatica-0.0.1-SNAPSHOT.jar &
java -jar api-rest/fuenteDinamica/target/fuenteDinamica-0.0.1-SNAPSHOT.jar &
java -jar api-rest/fuenteProxy/target/fuenteProxy-0.0.1-SNAPSHOT.jar &
java -jar api-rest/estadistica/target/estadistica-0.0.1-SNAPSHOT.jar &
java -jar api-rest/auth-usuario/target/auth-usuario-0.0.1-SNAPSHOT.jar &
java -jar api-rest/agregador/target/agregador-0.0.1-SNAPSHOT.jar &
java -jar front/web-client/target/web-client-0.0.1-SNAPSHOT.jar &

# Para correr en distinas pantallas de screen (quilombo):

screen -S auth
java -jar api-rest/auth-usuario/target/auth-usuario-0.0.1-SNAPSHOT.jar

screen -S fuente-estatica
java -jar api-rest/fuenteEstatica/target/fuenteEstatica-0.0.1-SNAPSHOT.jar

screen -S fuente-dinamica
java -jar api-rest/fuenteDinamica/target/fuenteDinamica-0.0.1-SNAPSHOT.jar

screen -S fuente-proxy
java -jar api-rest/fuenteProxy/target/fuenteProxy-0.0.1-SNAPSHOT.jar

screen -S estadistica
java -jar api-rest/estadistica/target/estadistica-0.0.1-SNAPSHOT.jar

screen -S agregador
java -jar api-rest/agregador/target/agregador-0.0.1-SNAPSHOT.jar

screen -S web-client
java -jar front/web-client/target/web-client-0.0.1-SNAPSHOT.jar

# Para terminar

pkill -f 'java -jar'

# chequear que corren 

ps aux | grep java