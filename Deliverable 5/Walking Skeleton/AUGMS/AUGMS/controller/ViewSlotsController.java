package AUGMS.controller;

import AUGMS.dao.ParkingSpotDAO;
import AUGMS.entity.ParkingSpot;
import AUGMS.entity.SpotStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ViewSlotsController {

    @FXML
    private Button closeButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<SlotDisplayData> slotsTable;

    @FXML
    private TableColumn<SlotDisplayData, String> spotIdColumn;

    @FXML
    private TableColumn<SlotDisplayData, String> statusColumn;

    @FXML
    private TableColumn<SlotDisplayData, String> vehiclePlateColumn;

    private ParkingSpotDAO parkingSpotDAO;

    @FXML
    public void initialize() {
        parkingSpotDAO = new ParkingSpotDAO();

        spotIdColumn.setCellValueFactory(cellData -> cellData.getValue().spotIdProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        vehiclePlateColumn.setCellValueFactory(cellData -> cellData.getValue().vehiclePlateProperty());

        // Custom cell factory for color coding the status
        statusColumn.setCellFactory(column -> new TableCell<SlotDisplayData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Free")) {
                        setStyle("-fx-background-color: #c8e6c9; -fx-text-fill: #2e7d32;");
                    } else if (item.equals("Occupied")) {
                        setStyle("-fx-background-color: #ffcdd2; -fx-text-fill: #c62828;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        loadData();
    }

    @FXML
    void handleClose(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        loadData();
    }

    private void loadData() {
        try {
            ObservableList<SlotDisplayData> data = FXCollections.observableArrayList();
            List<ParkingSpot> spots = parkingSpotDAO.getAllSpots();

            for (ParkingSpot spot : spots) {
                String spotId = spot.getSpotID();
                String status = spot.getStatus() == SpotStatus.AVAILABLE ? "Free" : "Occupied";
                String vehiclePlate = "";

                if (spot.getStatus() == SpotStatus.OCCUPIED) {
                    vehiclePlate = parkingSpotDAO.getCurrentVehiclePlate(spotId);
                    if (vehiclePlate == null) vehiclePlate = "Unknown";
                }

                data.add(new SlotDisplayData(spotId, status, vehiclePlate));
            }

            slotsTable.setItems(data);
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
            messageLabel.setText("Data refreshed. Total slots: " + spots.size());

        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("Error loading data: " + e.getMessage());
        }
    }

    public static class SlotDisplayData {
        private final SimpleStringProperty spotId;
        private final SimpleStringProperty status;
        private final SimpleStringProperty vehiclePlate;

        public SlotDisplayData(String spotId, String status, String vehiclePlate) {
            this.spotId = new SimpleStringProperty(spotId);
            this.status = new SimpleStringProperty(status);
            this.vehiclePlate = new SimpleStringProperty(vehiclePlate);
        }

        public SimpleStringProperty spotIdProperty() { return spotId; }
        public SimpleStringProperty statusProperty() { return status; }
        public SimpleStringProperty vehiclePlateProperty() { return vehiclePlate; }
    }
}

