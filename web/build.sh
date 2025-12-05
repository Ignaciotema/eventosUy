#!/bin/bash

# Configurar Java 21 para Maven
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# Obtener el directorio donde está el script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Ruta a Tomcat (relativa al directorio del proyecto)
TOMCAT_DIR="$SCRIPT_DIR/apache-tomcat-11.0.13"
WEBAPPS_DIR="$TOMCAT_DIR/webapps"

# Verificar que Tomcat existe
if [ ! -d "$TOMCAT_DIR" ]; then
    echo "ERROR: No se encontró Tomcat en $TOMCAT_DIR"
    echo "Por favor, descarga Tomcat 11.0.13 y colócalo en el directorio raíz del proyecto"
    exit 1
fi

echo "Building Maven projects..."
echo

echo "Building gui_entrega2 (JAR)..."
cd "$SCRIPT_DIR/gui_entrega2"
mvn clean install
if [ $? -ne 0 ]; then
    echo "Error building gui_entrega2"
    exit 1
fi
echo "gui_entrega2 build completed successfully"
echo

# Copiar datosPrueba desde gui_entrega2 a la carpeta target
echo "Copying datosPrueba to target folder..."
cp -r ./datosPrueba ./target/
if [ $? -ne 0 ]; then
    echo "Error copying datosPrueba"
    exit 1
fi
echo "datosPrueba copied successfully to target folder."
echo

echo "Building tarea2 (WAR)..."
cd "$SCRIPT_DIR/tarea2"
mvn clean install
if [ $? -ne 0 ]; then
    echo "Error building tarea2"
    exit 1
fi
echo "tarea2 build completed successfully"
echo

echo "Building dispositivoMobile (WAR)..."
cd "$SCRIPT_DIR/mobile"
mvn clean install
if [ $? -ne 0 ]; then
    echo "Error building mobile"
    exit 1
fi
echo "mobile build completed successfully"
echo

# Copiar WARs a Tomcat webapps
echo "Copying WAR files to Tomcat webapps..."
cp "$SCRIPT_DIR/tarea2/target/tarea2-0.0.1-SNAPSHOT.war" "$WEBAPPS_DIR/"
if [ $? -ne 0 ]; then
    echo "Error copying tarea2 WAR to Tomcat"
    exit 1
fi
echo "tarea2 WAR copied to $WEBAPPS_DIR"

cp "$SCRIPT_DIR/mobile/target/movil-0.0.1-SNAPSHOT.war" "$WEBAPPS_DIR/"
if [ $? -ne 0 ]; then
    echo "Error copying movil WAR to Tomcat"
    exit 1
fi
echo "movil WAR copied to $WEBAPPS_DIR"
echo

# Configurar puerto 8085 y address 0.0.0.0 en Tomcat
echo "Configuring Tomcat to use port 8085 with address 0.0.0.0..."
SERVER_XML="$TOMCAT_DIR/conf/server.xml"

# Backup del server.xml original (solo la primera vez)
if [ ! -f "$SERVER_XML.backup" ]; then
    cp "$SERVER_XML" "$SERVER_XML.backup"
    echo "Backup created: $SERVER_XML.backup"
fi

# Restaurar desde backup para evitar modificaciones acumuladas
cp "$SERVER_XML.backup" "$SERVER_XML"

# Cambiar el Connector HTTP/1.1 a puerto 8085 con address 0.0.0.0
sed -i 's/<Connector port="8080" protocol="HTTP\/1.1"/<Connector port="8085" protocol="HTTP\/1.1"\n               address="0.0.0.0"/' "$SERVER_XML"

echo "Tomcat configured to use port 8085 with address 0.0.0.0"
echo

echo "Build completed!"
echo "JAR file: gui_entrega2/target/ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
echo "WAR files copied to: $WEBAPPS_DIR"
echo

# Ejecutar el archivo JAR generado
echo "Running the generated JAR..."
cd ../gui_entrega2
JAR_PATH="./target/ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"

if [ -f "$JAR_PATH" ]; then
    java -jar "$JAR_PATH"
else
    echo "JAR file not found: $JAR_PATH"
    exit 1
fi

echo "JAR executed successfully."