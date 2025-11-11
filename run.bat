@echo off
echo === Compiling all Java files ===

:: We must add src\backend\models\*.java to compile your new PatientModel
javac --module-path "C:\javafx\javafx-sdk-25\lib" --add-modules=javafx.controls,javafx.fxml -cp ".;lib\mssql-jdbc-13.2.0.jre11.jar" -d out src\backend\*.java src\backend\models\*.java src\gui\*.java

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Compilation successful!
echo.
cls

:menu
echo ===================================
echo  HOSPITAL MANAGEMENT SYSTEM
echo ===================================
echo.
echo 1. Run Hospital Dashboard (JavaFX)
echo 2. Run File I/O (Export Patients to .txt)
echo 3. Run Serialization (Export Patients to .ser)
echo 4. Run Deserialization (Load Patients from .ser)
echo.
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto run_dashboard
if "%choice%"=="2" goto run_file_io
if "%choice%"=="3" goto run_serialization
if "%choice%"=="4" goto run_deserialization

echo Invalid choice.
pause
goto menu

:run_dashboard
echo === Starting Hospital Dashboard ===
java --module-path "C:\javafx\javafx-sdk-25\lib" --add-modules=javafx.controls,javafx.fxml -cp "out;lib\mssql-jdbc-13.2.0.jre11.jar" gui.HospitalDashboard
goto end

:run_file_io
echo === Running File I/O (Exporting to patients_report.txt) ===
:: Note: This is a console app, so it doesn't need the --module-path
java -cp "out;lib\mssql-jdbc-13.2.0.jre11.jar" backend.PatientFileExporter
goto end

:run_serialization
echo === Running Serialization (Exporting to patients.ser) ===
java -cp "out;lib\mssql-jdbc-13.2.0.jre11.jar" backend.PatientSerializationExporter
goto end

:run_deserialization
echo === Running Deserialization (Loading from patients.ser) ===
java -cp "out;lib\mssql-jdbc-13.2.0.jre11.jar" backend.PatientSerializationLoader
goto end

:end
echo.
echo === Program finished ===
pause