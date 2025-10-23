@ECHO OFF
SETLOCAL

SET "BASEDIR=%~dp0"
SET "WRAPPER_JAR=%BASEDIR%\.mvn\wrapper\maven-wrapper.jar"
SET "PROPERTIES_FILE=%BASEDIR%\.mvn\wrapper\maven-wrapper.properties"

IF NOT EXIST "%PROPERTIES_FILE%" (
  ECHO Impossible de trouver %PROPERTIES_FILE%
  EXIT /B 1
)

IF NOT EXIST "%WRAPPER_JAR%" (
  FOR /F "tokens=2 delims==" %%A IN ('findstr /B /C:"wrapperUrl=" "%PROPERTIES_FILE%"') DO SET "WRAPPER_URL=%%A"
  IF NOT DEFINED WRAPPER_URL (
    ECHO Impossible de lire wrapperUrl dans %PROPERTIES_FILE%
    EXIT /B 1
  )
  ECHO Telechargement du Maven Wrapper...
  POWERSHELL -Command "Invoke-WebRequest -UseBasicParsing -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%'" || GOTO error
)

IF NOT DEFINED JAVA_HOME (
  SET "JAVA_EXE=java"
) ELSE (
  SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%BASEDIR%" -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
EXIT /B %ERRORLEVEL%

:error
ECHO Echec du telechargement du Maven Wrapper. Installez Java et Maven ou telechargez le wrapper manuellement.
EXIT /B 1
