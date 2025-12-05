@echo off

echo Inicializando api...
cd api
start "" java -jar "target\ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
cd ..

set "CATALINA_HOME=%cd%\apache-tomcat-11.0.13"
call "%CATALINA_HOME%\bin\shutdown.bat"

echo Copiando WAR a Tomcat...
xcopy "web\target\tarea2-0.0.1-SNAPSHOT.war" "apache-tomcat-11.0.13\webapps" /Y

echo Iniciando Tomcat...
call "%CATALINA_HOME%\bin\startup.bat"

timeout /t 5 /nobreak

start http://localhost:8081/tarea2-0.0.1-SNAPSHOT/HomeServlet

