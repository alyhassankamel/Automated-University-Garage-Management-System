package AUGMS.controller;

import AUGMS.dao.EntryExitRepository;
import AUGMS.dao.ParkingSpotDAO;
import AUGMS.dao.VehicleDAO;
import AUGMS.entity.EntryExitLog;
import AUGMS.entity.SpotStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;

public class CheckOutController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button checkOutButton;

    @FXML
    private TextField licensePlateField;

    @FXML
    private Label messageLabel;

    private VehicleDAO vehicleDAO;
    private ParkingSpotDAO parkingSpotDAO;
    private EntryExitRepository entryExitRepository;

    private static final double BASE_FEE = 5.0;
    private static final double HOURLY_RATE = 2.0;

    @FXML
    public void initialize() {
        vehicleDAO = new VehicleDAO();
        parkingSpotDAO = new ParkingSpotDAO();
        entryExitRepository = new EntryExitRepository();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        try {
            String licensePlate = licensePlateField.getText().trim();

            if (licensePlate.isEmpty()) {
                showError("Please enter a license plate number.");
                return;
            }

            if (!vehicleDAO.isVehicleParked(licensePlate)) {
                showError("Vehicle not found or not checked in.");
                return;
            }

            EntryExitLog entryLog = entryExitRepository.getLatestEntryLog(licensePlate);
            if (entryLog == null) {
                showError("Entry record not found.");
                return;
            }

            Timestamp entryTime = entryLog.getTimestamp();
            Timestamp exitTime = new Timestamp(System.currentTimeMillis());
            long durationMillis = exitTime.getTime() - entryTime.getTime();
            long hours = durationMillis / (1000 * 60 * 60);
            long minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);

            double fee = BASE_FEE;
            long totalHours = hours + (minutes > 0 ? 1 : 0);
            if (totalHours == 0) totalHours = 1;
            fee += totalHours * HOURLY_RATE;

            EntryExitLog exitLog = new EntryExitLog(licensePlate, "EXIT", entryLog.getSpotID());
            exitLog.setTimestamp(exitTime);
            exitLog.setFee(fee);
            entryExitRepository.createExitLog(exitLog);

            parkingSpotDAO.updateSpotStatus(entryLog.getSpotID(), SpotStatus.AVAILABLE);

            String durationStr = String.format("%d hours, %d minutes", hours, minutes);
            showSuccess(String.format("Checked out successfully!\nSlot: %s\nDuration: %s\nTotal Fee: $%.2f",
                    entryLog.getSpotID(), durationStr, fee));

            licensePlateField.clear();

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

