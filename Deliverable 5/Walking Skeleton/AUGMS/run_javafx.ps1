# Manual script to run AUGMS with local JavaFX SDK
$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
Set-Location -Path $PSScriptRoot

$FX_LIB = "javafx-sdk-17.0.2\lib"
$JDBC_JAR = "lib\mssql-jdbc.jar"
$OUT_DIR = "out"

# Kill existing java processes to allow restart
Stop-Process -Name java -ErrorAction SilentlyContinue

# 1. Compile only what's needed
$essentialFiles = @(
    "AUGMS\Main.java",
    "AUGMS\controller\MainMenuController.java",
    "AUGMS\controller\CheckInController.java",
    "AUGMS\controller\CheckOutController.java",
    "AUGMS\controller\ViewSlotsController.java",
    "AUGMS\controller\GarageGlobalAccessUIController.java",
    "AUGMS\dao\VehicleDAO.java",
    "AUGMS\dao\ParkingSpotDAO.java",
    "AUGMS\dao\EntryExitRepository.java",
    "AUGMS\dao\ParkingGarageDAO.java",
    "AUGMS\entity\Vehicle.java",
    "AUGMS\entity\ParkingSpot.java",
    "AUGMS\entity\EntryExitLog.java",
    "AUGMS\entity\SpotStatus.java",
    "AUGMS\entity\ParkingGarage.java",
    "AUGMS\entity\GarageAccess.java",
    "AUGMS\entity\ParkingStatus.java",
    "AUGMS\service\GarageAccessOverrideService.java",
    "AUGMS\util\DatabaseConnection.java"
)

Write-Host "Compiling..."
javac --module-path $FX_LIB --add-modules javafx.controls,javafx.fxml -cp $JDBC_JAR -d $OUT_DIR $essentialFiles

if ($LASTEXITCODE -eq 0) {
    # 2. Prepare resources
    if (-not (Test-Path "$OUT_DIR\AUGMS\fxml")) { New-Item -ItemType Directory -Path "$OUT_DIR\AUGMS\fxml" -Force | Out-Null }
    if (-not (Test-Path "$OUT_DIR\AUGMS\css")) { New-Item -ItemType Directory -Path "$OUT_DIR\AUGMS\css" -Force | Out-Null }
    Copy-Item "AUGMS\fxml\*.fxml" "$OUT_DIR\AUGMS\fxml\" -Force
    Copy-Item "AUGMS\css\*.css" "$OUT_DIR\AUGMS\css\" -Force
    
    Write-Host "Running..."
    java --module-path $FX_LIB --add-modules javafx.controls,javafx.fxml -cp "$OUT_DIR;$JDBC_JAR" AUGMS.Main
} else {
    Write-Host "Compilation failed!"
}
