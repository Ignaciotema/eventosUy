@echo off
echo Building Maven projects...
echo.

echo Building api (JAR)...
cd api
call mvn clean install
if %errorlevel% neq 0 (
    echo Error building api
    pause
    exit /b 1
)
copy datosPrueba target
echo api build completed successfully
echo.

echo Building web (WAR)...
cd ..\web
call mvn clean install
if %errorlevel% neq 0 (
    echo Error building web
    pause
    exit /b 1
)
echo web build completed successfully
echo.


echo Build completed! 
echo JAR file: api\target\ServidorWS-0.0.1-SNAPSHOT-jar-with-dependencies.jar
echo WAR file: web\target\web-0.0.1-SNAPSHOT.war
pause