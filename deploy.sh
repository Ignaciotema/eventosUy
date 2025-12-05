echo "Inicializando api..."
cd api || exit 1
nohup java -jar "target/ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar" >/dev/null 2>&1 &
cd ..

CATALINA_HOME="$(pwd)/apache-tomcat-11.0.13"

echo "Apagando Tomcat..."
"$CATALINA_HOME/bin/shutdown.sh"

echo "Copiando WAR a Tomcat..."
cp "web/target/EventosUy-1.0.war" "$CATALINA_HOME/webapps/"

echo "Iniciando Tomcat..."
"$CATALINA_HOME/bin/startup.sh"

echo "Esperando 5 segundos..."
sleep 5

xdg-open "http://localhost:8081/EventosUy-1.0/HomeServlet" 2>/dev/null || \
open "http://localhost:8081/EventosUy-1.0/HomeServlet" 2>/dev/null ||
start "http://localhost:8081/EventosUy-1.0/HomeServlet" 2>/dev/null

