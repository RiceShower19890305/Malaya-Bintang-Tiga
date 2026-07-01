@echo off
REM weather-run.bat - Start the Weather Dashboard on Windows
cd /d "%~dp0"
if exist target\weather-dashboard-1.0-SNAPSHOT.jar (
    java -Xmx512m -jar target\weather-dashboard-1.0-SNAPSHOT.jar
) else (
    echo.
    echo Weather Dashboard JAR not found.
    echo Please run: mvn clean package
    echo.
    pause
)
