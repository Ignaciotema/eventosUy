echo "Building Maven projects..."
echo

echo "Building api (JAR)..."
cd api || exit 1

mvn clean install
if [ $? -ne 0 ]; then
    echo "Error building api"
    exit 1
fi

echo "api build completed successfully"

mkdir -p target/datosPrueba
cp -r datosPrueba/* target/datosPrueba/

mkdir -p target/src/webservices/uploads
cp -r src/webservices/uploads/. target/src/webservices/uploads/

cp ../application.properties "$HOME/"

echo

echo "Building web (WAR)..."
cd ../web || exit 1

mvn clean install
if [ $? -ne 0 ]; then
    echo "Error building web"
    exit 1
fi

echo "web build completed successfully"
echo

echo "Build completed!"
echo "JAR file: api/target/ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
echo "WAR file: web/target/EventosUy-1.0.war"
