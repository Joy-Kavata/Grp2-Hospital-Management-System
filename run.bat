@echo off
echo === Compiling all Java files ===
javac --module-path "C:\javafx\javafx-sdk-25\lib" --add-modules=javafx.controls,javafx.fxml -cp ".;lib\mssql-jdbc-13.2.0.jre11.jar" -d out src\backend\*.java src\gui\*.java

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo === Starting Hospital Dashboard ===
java --module-path "C:\javafx\javafx-sdk-25\lib" --add-modules=javafx.controls,javafx.fxml -cp "out;lib\mssql-jdbc-13.2.0.jre11.jar" gui.HospitalDashboard
pause
