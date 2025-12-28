@echo off
echo ========================================
echo AUGMS Swing Application - Build & Run
echo ========================================
echo.

set JDBC_PATH=%~dp0lib\mssql-jdbc.jar

REM Check if JDBC driver exists
if not exist "%JDBC_PATH%" (
    echo ERROR: MSSQL JDBC driver not found!
    echo Please ensure mssql-jdbc.jar is in the lib directory.
    pause
    exit /b 1
)

echo [1/3] Cleaning previous build...
if exist "out" rmdir /s /q out
mkdir out

echo [2/3] Compiling Java source files...
javac -cp "%JDBC_PATH%" -d out -sourcepath AUGMS AUGMS\**\*.java

if errorlevel 1 (
    echo.
    echo Compilation failed! Please check the errors above.
    pause
    exit /b 1
)

echo [3/3] Running application...
echo.
java -cp "out;%JDBC_PATH%" AUGMS.Main

if errorlevel 1 (
    echo.
    echo Application failed to run! Please check the errors above.
    echo.
    echo Common issues:
    echo - Database connection: Update credentials in AUGMS\util\DatabaseConnection.java
    echo - SQL Server: Ensure it is running and database AUGMS exists
    pause
    exit /b 1
)

pause
