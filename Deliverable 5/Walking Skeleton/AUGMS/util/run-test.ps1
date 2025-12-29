<#
.SYNOPSIS
  Compile and run the database connectivity test using the Microsoft JDBC driver.

USAGE
  From the Model folder run:
    .\run-test.ps1

  The script will download the JDBC driver into ./lib if it's missing.
#>

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$libDir = Join-Path $root 'lib'
$jarPath = Join-Path $libDir 'mssql-jdbc.jar'

if (-not (Test-Path $jarPath)) {
    Write-Host "JDBC driver not found at $jarPath. Downloading..."
    New-Item -ItemType Directory -Path $libDir -Force | Out-Null
    $url = 'https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/10.2.1.jre8/mssql-jdbc-10.2.1.jre8.jar'
    Invoke-WebRequest -Uri $url -OutFile $jarPath -UseBasicParsing
    Write-Host "Downloaded driver to $jarPath"
}

Write-Host 'Compiling...'
javac Database\DatabaseConnectionFactory.java Database\DatabaseConnectionTest.java

Write-Host 'Running test...'
java -cp ".;lib\mssql-jdbc.jar" Database.DatabaseConnectionTest

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
