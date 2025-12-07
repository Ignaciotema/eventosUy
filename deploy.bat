@echo off

echo Inicializando api...
cd api
start "" java -jar "target\ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
cd ..

set "CATALINA_HOME=%cd%\apache-tomcat-11.0.13"
call "%CATALINA_HOME%\bin\shutdown.bat"
timeout /t 3 /nobreak

echo Copiando WAR a Tomcat...
xcopy "web\target\EventosUy-1.0.war" "apache-tomcat-11.0.13\webapps" /Y

echo Iniciando Tomcat...
call "%CATALINA_HOME%\bin\startup.bat"

timeout /t 5 /nobreak

start http://localhost:8081/EventosUy-1.0/HomeServlet

pause

