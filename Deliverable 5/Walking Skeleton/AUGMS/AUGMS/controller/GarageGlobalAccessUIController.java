package AUGMS.controller;

import AUGMS.entity.GarageAccess;
import AUGMS.entity.ParkingGarage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import AUGMS.service.GarageAccessOverrideService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GarageGlobalAccessUIController {

    @FXML
    private ComboBox<String> accessModeComboBox;

    @FXML
    private Label accessModeLabel;

    @FXML
    private Label availableSpotsLabel;

    @FXML
    private ComboBox<String> garageComboBox;

    @FXML
    private Label occupiedSpotsLabel;

    @FXML
    private Button overrideButton;

    @FXML
    private TextArea reasonTextArea;

    @FXML
    private Button refreshButton;

    @FXML
    private Button resetButton;

    @FXML
    private ScrollPane statusScrollPane;

    @FXML
    private TextArea statusTextArea;

    private GarageAccessOverrideService overrideService;

    @FXML
    public void initialize() {
        overrideService = new GarageAccessOverrideService();
        accessModeComboBox.setItems(FXCollections.observableArrayList("NORMAL", "OVERRIDE", "RESTRICTED", "CLOSED"));
        loadGarages();
    }

    @FXML
    void handleGarageSelection(ActionEvent event) {
        updateGarageStatus();
    }

    @FXML
    void handleOverride(ActionEvent event) {
        String selectedGarageID = garageComboBox.getValue();
        if (selectedGarageID == null) {
            displayError("Please select a garage first.");
            return;
        }

        String mode = accessModeComboBox.getValue();
        if (mode == null) {
            displayError("Please select an access mode.");
            return;
        }

        String reason = reasonTextArea.getText().trim();
        if (reason.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Override");
            alert.setHeaderText("No reason provided");
            alert.setContentText("No reason provided. Continue anyway?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return;
            }
            reason = "No reason provided";
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Override");
        confirmAlert.setHeaderText("Apply " + mode + " mode to garage " + selectedGarageID + "?");
        confirmAlert.setContentText("Reason: " + reason);
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();

        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            try {
                GarageAccess accessMode = GarageAccess.valueOf(mode.toUpperCase());
                overrideService.setGlobalOverride(selectedGarageID, accessMode, reason);
                
                ParkingGarage garage = overrideService.getParkingGarageDAO().findGarage(selectedGarageID);
                if (garage != null) {
                    displaySuccess("Override applied successfully.");
                    displayCurrentStatus(garage);
                } else {
                    displayError("Garage not found: " + selectedGarageID);
                }
            } catch (Exception e) {
                displayError("Error processing override request: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        loadGarages();
    }

    @FXML
    void handleReset(ActionEvent event) {
        String selectedGarageID = garageComboBox.getValue();
        if (selectedGarageID == null) {
            displayError("Please select a garage first.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Reset");
        confirmAlert.setHeaderText("Reset garage " + selectedGarageID + " to NORMAL access mode?");
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();

        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            try {
                overrideService.resetGlobalOverride(selectedGarageID);
                ParkingGarage garage = overrideService.getParkingGarageDAO().findGarage(selectedGarageID);
                if (garage != null) {
                    displaySuccess("Reset successful.");
                    displayCurrentStatus(garage);
                } else {
                    displayError("Garage not found: " + selectedGarageID);
                }
            } catch (Exception e) {
                displayError("Error resetting override: " + e.getMessage());
            }
        }
    }

    private void loadGarages() {
        Set<ParkingGarage> garages = overrideService.getParkingGarageDAO().getAllGarages();
        garageComboBox.getItems().clear();
        for (ParkingGarage garage : garages) {
            garageComboBox.getItems().add(garage.getGarageID());
        }
        
        if (!garageComboBox.getItems().isEmpty()) {
            garageComboBox.getSelectionModel().selectFirst();
            updateGarageStatus();
        }
    }

    private void updateGarageStatus() {
        String selectedGarageID = garageComboBox.getValue();
        if (selectedGarageID != null) {
            ParkingGarage garage = overrideService.getParkingGarageDAO().findGarage(selectedGarageID);
            if (garage != null) {
                displayCurrentStatus(garage);
            }
        }
    }

    private void displayCurrentStatus(ParkingGarage garage) {
        Platform.runLater(() -> {
            accessModeLabel.setText(garage.getGarageAccess().toString());
            availableSpotsLabel.setText(String.valueOf(garage.getAvailableSpotCount()));
            occupiedSpotsLabel.setText(String.valueOf(garage.countOccupiedSlots()));

            switch (garage.getGarageAccess()) {
                case NORMAL: accessModeLabel.setTextFill(Color.web("#006400")); break;
                case OVERRIDE: accessModeLabel.setTextFill(Color.web("#ff8c00")); break;
                case RESTRICTED: accessModeLabel.setTextFill(Color.web("#ffa500")); break;
                case CLOSED: accessModeLabel.setTextFill(Color.web("#dc143c")); break;
            }

            appendStatusText(String.format("Garage: %s | Access: %s | Available: %d/%d",
                    garage.getGarageID(), garage.getGarageAccess(), garage.getAvailableSpotCount(), garage.getTotalSpots()));
        });
    }

    private void displaySuccess(String message) {
        appendStatusText("SUCCESS: " + message);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.show();
    }

    private void displayError(String message) {
        appendStatusText("ERROR: " + message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.show();
    }

    private void appendStatusText(String text) {
        statusTextArea.appendText("[" + new Date() + "] " + text + "\n");
    }
}

