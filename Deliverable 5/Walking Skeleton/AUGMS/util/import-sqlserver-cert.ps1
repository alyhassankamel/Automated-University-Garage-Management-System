<#
Imports an exported SQL Server certificate into the JVM cacerts truststore.

Usage (run as Administrator):
  .\import-sqlserver-cert.ps1 -CertFile .\sqlserver.cer -JavaHome "C:\Program Files\Java\jre1.8.0_361" -KeystorePass changeit

Parameters:
  -CertFile    Path to the exported certificate file (.cer or .crt). Mandatory.
  -JavaHome    Path to the Java home directory (optional; defaults to $env:JAVA_HOME).
  -KeystorePass Password for the JVM cacerts keystore (default: changeit).
  -Alias       Alias to use for the imported cert (default: sqlserver).

Notes:
- Backup the cacerts file before importing. The script creates a timestamped backup.
- Run PowerShell as Administrator to write to the JVM folder.
#>

param(
    [Parameter(Mandatory=$true)] [string]$CertFile,
    [string]$JavaHome = $env:JAVA_HOME,
    [string]$KeystorePass = 'changeit',
    [string]$Alias = 'sqlserver'
)

Set-StrictMode -Version Latest

if (-not (Test-Path $CertFile)) {
    Write-Error "Certificate file not found: $CertFile"
    exit 2
}

if (-not $JavaHome) {
    Write-Host 'JAVA_HOME not set. Attempting to discover java.home via `java -XshowSettings:properties -version`...'
    $props = & java -XshowSettings:properties -version 2>&1 | Select-String 'java.home' -SimpleMatch
    if ($props) {
        $JavaHome = ($props -split '=')[1].Trim()
        Write-Host "Discovered java.home = $JavaHome"
    } else {
        Write-Error 'Could not determine Java home. Please supply -JavaHome explicitly.'
        exit 3
    }
}

$cacerts = Join-Path $JavaHome 'lib\security\cacerts'
if (-not (Test-Path $cacerts)) {
    Write-Error "cacerts not found at expected location: $cacerts"
    exit 4
}

$timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$backup = "$cacerts.$timestamp.bak"
Copy-Item -Path $cacerts -Destination $backup -Force
Write-Host "Backed up cacerts to: $backup"

$keytool = Join-Path $JavaHome 'bin\keytool.exe'
if (-not (Test-Path $keytool)) {
    Write-Error "keytool not found at: $keytool"
    exit 5
}

Write-Host "Importing certificate '$CertFile' into keystore '$cacerts' with alias '$Alias'..."
& $keytool -importcert -noprompt -trustcacerts -alias $Alias -file $CertFile -keystore $cacerts -storepass $KeystorePass

if ($LASTEXITCODE -eq 0) {
    Write-Host 'Import completed successfully.'
    Write-Host 'You may need to restart any running Java applications to pick up the updated truststore.'
} else {
    Write-Error "keytool exited with code $LASTEXITCODE. See output above for details."
    exit $LASTEXITCODE
}
