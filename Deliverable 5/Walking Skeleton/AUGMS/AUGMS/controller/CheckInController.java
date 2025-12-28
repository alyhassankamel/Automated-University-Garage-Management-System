package AUGMS.controller;

import AUGMS.dao.EntryExitRepository;
import AUGMS.dao.ParkingSpotDAO;
import AUGMS.dao.VehicleDAO;
import AUGMS.entity.EntryExitLog;
import AUGMS.entity.ParkingSpot;
import AUGMS.entity.SpotStatus;
import AUGMS.entity.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class CheckInController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button checkInButton;

    @FXML
    private TextField licensePlateField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField ownerNameField;

    @FXML
    private ComboBox<String> vehicleTypeCombo;

    @FXML
    private ComboBox<String> garageCombo;

    @FXML
    private ComboBox<String> spotCombo;

    private VehicleDAO vehicleDAO;
    private ParkingSpotDAO parkingSpotDAO;
    private EntryExitRepository entryExitRepository;

    @FXML
    public void initialize() {
        vehicleDAO = new VehicleDAO();
        parkingSpotDAO = new ParkingSpotDAO();
        entryExitRepository = new EntryExitRepository();

        vehicleTypeCombo.getItems().addAll("Car", "Motorcycle", "Truck");
        vehicleTypeCombo.getSelectionModel().selectFirst();

        garageCombo.getItems().addAll("main building", "ITI", "NAID", "innovation building");
        garageCombo.getSelectionModel().selectFirst();

        loadAvailableSpots();
    }

    private void loadAvailableSpots() {
        try {
            spotCombo.getItems().clear();
            List<ParkingSpot> spots = parkingSpotDAO.getAllSpots();
            for (ParkingSpot spot : spots) {
                if (spot.getStatus() == SpotStatus.AVAILABLE) {
                    spotCombo.getItems().add(spot.getSpotID());
                }
            }
            if (!spotCombo.getItems().isEmpty()) {
                spotCombo.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showError("Error loading spots: " + e.getMessage());
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCheckIn(ActionEvent event) {
        try {
            String licensePlate = licensePlateField.getText().trim();
            String vehicleType = vehicleTypeCombo.getValue();
            String ownerName = ownerNameField.getText().trim();

            if (licensePlate.isEmpty()) {
                showError("Please enter a license plate number.");
                return;
            }

            if (vehicleDAO.isVehicleParked(licensePlate)) {
                showError("This vehicle is already checked in.");
                return;
            }

            String selectedSpotId = spotCombo.getValue();
            if (selectedSpotId == null || selectedSpotId.isEmpty()) {
                showError("No available parking slots.");
                return;
            }
            ParkingSpot availableSpot = parkingSpotDAO.findSpot(selectedSpotId);

            Vehicle vehicle = vehicleDAO.findVehicleByLicensePlate(licensePlate);
            if (vehicle == null) {
                vehicle = new Vehicle(licensePlate, vehicleType, ownerName);
                vehicleDAO.createVehicle(vehicle);
            }

            EntryExitLog entryLog = new EntryExitLog(licensePlate, "ENTRY", availableSpot.getSpotID());
            entryExitRepository.createEntryLog(entryLog);

            parkingSpotDAO.updateSpotStatus(availableSpot.getSpotID(), SpotStatus.OCCUPIED);

            showSuccess("Checked in successfully!\nSlot: " + availableSpot.getSpotID() +
                    "\nTime: " + entryLog.getTimestamp());

            licensePlateField.clear();
            ownerNameField.clear();
            vehicleTypeCombo.getSelectionModel().selectFirst();
            loadAvailableSpots();

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText(message);
    }
}

