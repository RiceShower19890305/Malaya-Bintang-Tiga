@echo off
REM run.bat - Start the Malaya Bintang Tiga game on Windows
cd /d "%~dp0"
java -Xmx2048m -jar target\malaya-bintang-tiga-1.0-SNAPSHOT.jar
pause
