@echo off
rem ============================
rem  Smart run.bat for your project
rem ============================

set "JAVAFX_DIR=C:\javafx\javafx-sdk-25\lib"
set "JDBC_JAR=lib\mssql-jdbc-13.2.0.jre11.jar"
set "OUT_DIR=out"

echo === Preparing output folder ===
if exist "%OUT_DIR%" (
  rd /s /q "%OUT_DIR%"
)
mkdir "%OUT_DIR%"

echo === Building list of source files to compile ===
set "SOURCES=src\backend\*.java src\gui\*.java"

rem include optional model folder if present
if exist "src\backend\models\" (
  set "SOURCES=%SOURCES% src\backend\models\*.java"
)

rem include week13 demos if present
if exist "src\week13\" (
  set "SOURCES=%SOURCES% src\week13\*.java"
)

echo Sources: %SOURCES%
echo.

echo === Compiling all Java files ===
javac --module-path "%JAVAFX_DIR%" --add-modules=javafx.controls,javafx.fxml ^
 -cp ".;%JDBC_JAR%" -d "%OUT_DIR%" %SOURCES%

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed.
    pause
    exit /b
)

echo === Copying Styles to Output ===
if exist "styles" (
    xcopy "styles" "%OUT_DIR%\styles\" /E /I /Y
    echo Styles copied successfully.
) else (
    echo WARNING: 'styles' folder not found in project root.
)

echo Compilation successful!
echo.
cls

:menu
echo =============================================
echo        HOSPITAL MANAGEMENT SYSTEM           
echo =============================================
echo.
echo 1. Run Login Page (Start System)
echo 2. Run File I/O Demo (Week 13)    (only if src\week13 exists)
echo 3. Run Serialization Demo (Week 13) (only if src\week13 exists)
echo 4. Run Deserialization Demo (Week 13) (only if src\week13 exists)
echo.
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto run_login
if "%choice%"=="2" goto run_file_io
if "%choice%"=="3" goto run_serialization
if "%choice%"=="4" goto run_deserialization

echo Invalid choice.
pause
goto menu

:run_login
echo === Starting Login Page ===
java --module-path "%JAVAFX_DIR%" --add-modules=javafx.controls,javafx.fxml -cp "%OUT_DIR%;%JDBC_JAR%" gui.LoginPage
goto end

:run_file_io
if not exist "src\week13\" (
  echo Week13 folder not found - File I/O demo skipped.
  pause
  goto menu
)
echo === Running File I/O Demo ===
java -cp "%OUT_DIR%" week13.FileIODemo
goto end

:run_serialization
if not exist "src\week13\" (
  echo Week13 folder not found - Serialization demo skipped.
  pause
  goto menu
)
echo === Running Serialization Demo ===
java -cp "%OUT_DIR%" week13.SerializationDemo
goto end

:run_deserialization
if not exist "src\week13\" (
  echo Week13 folder not found - Deserialization demo skipped.
  pause
  goto menu
)
echo === Running Deserialization Demo ===
java -cp "%OUT_DIR%" week13.SerializationDemo
goto end

:end
echo.
echo === Program finished ===
pause