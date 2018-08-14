@echo off
setlocal

if "%JAVA_HOME%" == "" goto NO_HOME
goto HAS_HOME

:NO_HOME
set JAVA=java

goto BUILD_COMMAND

:HAS_HOME
set JAVA="%JAVA_HOME%\bin\java.exe"

:BUILD_COMMAND
set COMMAND=%JAVA% -Djava.protocol.handler.pkgs=sun.net.www.protocol -jar "%~dp0${project.build.finalName}.${project.packaging}"

rem uncomment below to display the java version used to launch scapval
rem %JAVA% -version
rem echo:

:COMMAND_REPEAT
  if "%~1" == "" GOTO RUN
  set COMMAND=%COMMAND% %1
  shift
goto COMMAND_REPEAT

:RUN
echo %COMMAND%
%COMMAND%

endlocal
@echo on
