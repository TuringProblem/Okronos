@echo off
setlocal

set "JAR_PATH=%~dp0client-localhost.jar"
set "JAVA17=C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\java.exe"
set "JAVA21=C:\Users\amirm\.jdks\temurin-21.0.5\bin\java.exe"

if exist "%JAVA17%" (
  set "JAVA_BIN=%JAVA17%"
) else (
  set "JAVA_BIN=%JAVA21%"
)

if not exist "%JAVA_BIN%" (
  echo Could not find Java 17 or Java 21 at expected paths.
  echo Edit this file and set JAVA_BIN manually.
  pause
  exit /b 1
)

if not exist "%JAR_PATH%" (
  echo Missing jar: %JAR_PATH%
  pause
  exit /b 1
)

"%JAVA_BIN%" -ea --add-opens=java.base/java.lang=ALL-UNNAMED -jar "%JAR_PATH%"

endlocal
