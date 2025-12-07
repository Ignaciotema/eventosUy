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

echo api build completed successfully
mkdir target\datosPrueba
robocopy datosPrueba target\datosPrueba
mkdir target\src\webservices\uploads
robocopy src\webservices\uploads\. target\src\webservices\uploads /E

xcopy ..\application.properties "%USERPROFILE%" /Y


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
echo WAR file: web\target\EventosUy-1.0.war
pause