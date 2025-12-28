# AUGMS Setup and Run Script (JavaFX Version)
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "AUGMS JavaFX Application Setup & Run" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"

# Check Java
Write-Host "[1/3] Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "  Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "  Java not found!" -ForegroundColor Red
    Write-Host "  Please install Java 11 or higher from https://adoptium.net/" -ForegroundColor Yellow
    exit 1
}

# Check Maven
Write-Host "[2/3] Checking Maven..." -ForegroundColor Yellow
$mvnCommand = "mvn"
if (Test-Path ".\mvnw.cmd") {
    $mvnCommand = ".\mvnw.cmd"
    Write-Host "  Using Maven Wrapper" -ForegroundColor Green
} else {
    try {
        mvn -version | Out-Null
        Write-Host "  Maven found" -ForegroundColor Green
    } catch {
        Write-Host "  Maven not found!" -ForegroundColor Red
        Write-Host "  Please install Maven or ensure mvnw exists." -ForegroundColor Yellow
        exit 1
    }
}

# Compile and Run
Write-Host "[3/3] Building and Running application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting AUGMS Application via Maven" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

try {
    & $mvnCommand clean compile javafx:run
} catch {
    Write-Host ""
    Write-Host "Application failed to run!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "1. Database connection: Update credentials in AUGMS/util/DatabaseConnection.java" -ForegroundColor White
    Write-Host "2. SQL Server: Ensure it is running and database AUGMS exists" -ForegroundColor White
    Write-Host "3. Run database_schema.sql to create tables" -ForegroundColor White
    exit 1
}
