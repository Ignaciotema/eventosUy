# Imagen base Tomcat
FROM tomcat:11-jdk21

# Copiar configuración de Tomcat desde el repo
COPY apache-tomcat-11.0.13/conf/ $CATALINA_HOME/conf/

# Copiar el WAR generado a la imagen
COPY web/target/tarea2-0.0.1-SNAPSHOT.war $CATALINA_HOME/webapps/

# Puerto en el que corre Tomcat
EXPOSE 8081

# Comando para arrancar tomcat usando startup.sh
CMD ["catalina.sh", "run"]

