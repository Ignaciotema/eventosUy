# Imagen base con Java
FROM eclipse-temurin:21-jre

# Carpeta donde va a correr tu app
WORKDIR /api

# Copiamos tu jar al contenedor
COPY API.jar API.jar

# Puerto en el que corre tu API
EXPOSE 8080

COPY datosPrueba/ /api/datosPrueba/
COPY src/webservices/uploads /api/src/webservices/uploads/
COPY application.properties /root/application.properties

# Comando para arrancar tu API
CMD ["java", "-jar", "API.jar"]
