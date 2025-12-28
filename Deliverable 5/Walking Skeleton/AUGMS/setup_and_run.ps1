# AUGMS Setup and Run Script (Swing Version)
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "AUGMS Swing Application Setup & Run" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"

# Check Java
Write-Host "[1/4] Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "  ✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "  ✗ Java not found!" -ForegroundColor Red
    Write-Host "  Please install Java 11 or higher from https://adoptium.net/" -ForegroundColor Yellow
    exit 1
}

# Create lib directory
Write-Host "[2/4] Setting up directories..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path "lib" | Out-Null
Write-Host "  ✓ Directories created" -ForegroundColor Green

# Download JDBC Driver
Write-Host "[3/4] Downloading MSSQL JDBC driver..." -ForegroundColor Yellow
$jdbcUrl = "https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/12.4.2.jre11/mssql-jdbc-12.4.2.jre11.jar"
$jdbcPath = "lib\mssql-jdbc.jar"

if (-not (Test-Path $jdbcPath)) {
    try {
        Invoke-WebRequest -Uri $jdbcUrl -OutFile $jdbcPath
        Write-Host "  ✓ JDBC driver downloaded" -ForegroundColor Green
    } catch {
        Write-Host "  ✗ Failed to download JDBC driver" -ForegroundColor Red
        Write-Host "  Please download manually from: $jdbcUrl" -ForegroundColor Yellow
        exit 1
    }
} else {
    Write-Host "  ✓ JDBC driver already exists" -ForegroundColor Green
}

# Compile
Write-Host "[4/4] Compiling application..." -ForegroundColor Yellow
if (Test-Path "out") {
    Remove-Item -Recurse -Force "out"
}
New-Item -ItemType Directory -Force -Path "out" | Out-Null

$jdbcJar = $jdbcPath

$javacArgs = @(
    "-cp", $jdbcJar,
    "-d", "out",
    "-sourcepath", "AUGMS",
    "AUGMS\**\*.java"
)

try {
    & javac $javacArgs 2>&1 | ForEach-Object {
        if ($_ -match "error") {
            Write-Host "  ✗ $_" -ForegroundColor Red
        } else {
            Write-Host "  $_" -ForegroundColor Gray
        }
    }
    
    if ($LASTEXITCODE -ne 0) {
        throw "Compilation failed"
    }
    Write-Host "  ✓ Compilation successful" -ForegroundColor Green
} catch {
    Write-Host "  ✗ Compilation failed!" -ForegroundColor Red
    Write-Host "  Please check the errors above" -ForegroundColor Yellow
    exit 1
}

# Run
Write-Host "Running application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting AUGMS Application" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$classpath = "out;$jdbcJar"
$javaArgs = @(
    "-cp", $classpath,
    "AUGMS.Main"
)

try {
    & java $javaArgs
} catch {
    Write-Host ""
    Write-Host "Application failed to run!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "1. Database connection: Update credentials in AUGMS\util\DatabaseConnection.java" -ForegroundColor White
    Write-Host "2. SQL Server: Ensure it is running and database AUGMS exists" -ForegroundColor White
    Write-Host "3. Run database_schema.sql to create tables" -ForegroundColor White
    exit 1
}
