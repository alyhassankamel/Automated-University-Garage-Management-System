@echo off
java -cp "lib\mssql-jdbc.jar;." MainApp

java -cp "lib\mssql-jdbc.jar;." --module-path "c:\Users\xaali\Personal Data\Uni\Y3\Y3S1\Software\Project\Automated-University-Garage-Management-System\Deliverable 5\Walking Skeleton\AUGMS\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml MainAppGUI

pause
