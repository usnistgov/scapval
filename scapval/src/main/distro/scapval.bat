@echo off
setlocal
set "MINIMUM_JAVA_VERSION=11"

if "%JAVA_HOME%" == "" goto NO_HOME
goto HAS_HOME

:NO_HOME
set "JAVA=java"

where java >nul 2>&1
if errorlevel 1 (
  call :FAIL "ERROR: Unable to find Java executable 'java' in PATH. SCAPVal requires Java %MINIMUM_JAVA_VERSION%+."
  exit /b 1
)
goto CHECK_VERSION

:HAS_HOME
set "JAVA=%JAVA_HOME%\bin\java.exe"
if not exist "%JAVA%" (
  call :FAIL "ERROR: Configured Java executable '%JAVA%' does not exist. Set JAVA_HOME to a Java %MINIMUM_JAVA_VERSION%+ installation."
  exit /b 1
)

:CHECK_VERSION
set "JAVA_VERSION_LINE="
set "JAVA_VERSION="
set "JAVA_MAJOR_VERSION="

for /f "usebackq delims=" %%L in (`"%JAVA%" -version 2^>^&1`) do (
  if not defined JAVA_VERSION_LINE set "JAVA_VERSION_LINE=%%L"
)
if errorlevel 1 (
  call :FAIL "ERROR: Failed to run '%JAVA% -version'. Set JAVA_HOME to a valid Java %MINIMUM_JAVA_VERSION%+ installation or install a newer Java runtime."
  exit /b 1
)

for /f "tokens=2 delims=\" %%V in ("%JAVA_VERSION_LINE%") do (
  if not defined JAVA_VERSION set "JAVA_VERSION=%%V"
)
if not defined JAVA_VERSION (
  call :FAIL "ERROR: Unable to determine Java version from '%JAVA%'. Reported output: %JAVA_VERSION_LINE%. SCAPVal requires Java %MINIMUM_JAVA_VERSION%+."
  exit /b 1
)

for /f "tokens=1,2 delims=." %%A in ("%JAVA_VERSION%") do (
  if "%%A"=="1" (
    set "JAVA_MAJOR_VERSION=%%B"
  ) else (
    set "JAVA_MAJOR_VERSION=%%A"
  )
)

echo(%JAVA_MAJOR_VERSION%| findstr /r "^[0-9][0-9]*$" >nul
if errorlevel 1 (
  call :FAIL "ERROR: Unable to parse Java version '%JAVA_VERSION%' from '%JAVA%'. SCAPVal requires Java %MINIMUM_JAVA_VERSION%+."
  exit /b 1
)

if %JAVA_MAJOR_VERSION% LSS %MINIMUM_JAVA_VERSION% (
  call :FAIL "ERROR: Java %MINIMUM_JAVA_VERSION%+ is required, but '%JAVA%' reports version '%JAVA_VERSION%'. Set JAVA_HOME to a newer Java runtime or install Java %MINIMUM_JAVA_VERSION%+."
  exit /b 1
)

:BUILD_COMMAND
set COMMAND="%JAVA%" -Djava.protocol.handler.pkgs=sun.net.www.protocol -jar "%~dp0scapval-1.4.1.jar"

rem uncomment below to display the java version used to launch scapval
rem "%JAVA%" -version
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
goto :EOF

:FAIL
echo %~1 1>&2
endlocal
exit /b 1
