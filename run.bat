@echo off
set JDBC_LIB=lib\mssql-jdbc-13.2.0.jre11.jar
java -cp ".;%JDBC_LIB%" AddPatientTest
pause
