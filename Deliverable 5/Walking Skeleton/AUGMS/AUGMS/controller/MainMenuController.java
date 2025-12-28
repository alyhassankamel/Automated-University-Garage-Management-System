package AUGMS.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button checkInButton;

    @FXML
    private Button checkOutButton;

    @FXML
    private Button viewSlotsButton;

    @FXML
    private Button garageAccessButton;

    @FXML
    private Button exitButton;

    @FXML
    void handleCheckIn(ActionEvent event) {
        openWindow("/AUGMS/fxml/CheckIn.fxml", "Vehicle Check-In");
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        openWindow("/AUGMS/fxml/CheckOut.fxml", "Vehicle Check-Out");
    }

    @FXML
    void handleViewSlots(ActionEvent event) {
        openWindow("/AUGMS/fxml/ViewSlots.fxml", "View Available Slots");
    }

    @FXML
    void handleGarageAccess(ActionEvent event) {
        openWindow("/AUGMS/fxml/GarageGlobalAccess.fxml", "Garage Global Access");
    }

    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

