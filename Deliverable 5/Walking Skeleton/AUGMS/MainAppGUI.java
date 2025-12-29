
import controller.VehicleEntryController;
import controller.VehicleExitController;
import controller.VehicleRegistrationController;
import controller.ParkingStatusController;
import service.ParkingStatusService;
import dao.ParkingGarageDAO;
import dao.ParkingSpotDAO;
import dao.OccupancySensorDAO;
import entity.ParkingGarage;
import entity.ParkingSpot;
import entity.ParkingStatus;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainAppGUI extends Application {

    private VehicleEntryController entryController;
    private VehicleExitController exitController;
    private VehicleRegistrationController registrationController;
    private ParkingStatusController statusController;

    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Initialize Controllers
        initializeControllers();

        // Setup Main Layout
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Redirect System.out to Log Area
        // Initialize Status Label
        statusLabel = new Label("Ready");
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setPadding(new Insets(10));
        statusLabel.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");

        redirectSystemOut();

        // Create Tabs
        tabPane.getTabs().addAll(
                createRegistrationTab(),
                createEntryTab(),
                createExitTab(),
                createStatusTab());

        // Header with Logo
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("AUGMS");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        header.getChildren().add(titleLabel);

        try {
            Image logo = new Image("file:assets/images/logo.png");
            ImageView logoView = new ImageView(logo);
            logoView.setFitHeight(50);
            logoView.setPreserveRatio(true);
            header.getChildren().add(logoView);
        } catch (Exception e) {
            System.out.println("Warning: Logo not found at assets/images/logo.png");
        }

        root.setTop(header);
        root.setCenter(tabPane);
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Garage Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("System Initialized. Ready.");
    }

    private void initializeControllers() {
        entryController = new VehicleEntryController();
        exitController = new VehicleExitController();
        registrationController = new VehicleRegistrationController();

        // Setup for Status Controller (mirroring ParkingStatusTest setup)
        ParkingGarageDAO garageDAO = new ParkingGarageDAO();
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();
        OccupancySensorDAO sensorDAO = new OccupancySensorDAO();
        ParkingStatusService statusService = new ParkingStatusService(garageDAO, spotDAO, sensorDAO);
        statusController = new ParkingStatusController(statusService);
    }

    private Tab createRegistrationTab() {
        Tab tab = new Tab("Register Vehicle");
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);

        TextField plateField = new TextField();
        plateField.setPromptText("License Plate");

        TextField typeField = new TextField();
        typeField.setPromptText("Vehicle Type (Default: Visitor)");

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String plate = plateField.getText().trim();
            String type = typeField.getText().trim();
            if (plate.isEmpty()) {
                System.out.println("Error: License Plate is required.");
                return;
            }
            if (type.isEmpty())
                type = "Visitor";

            registrationController.handleRegistrationRequest(plate, type);
            plateField.clear();
            typeField.clear();
        });

        vbox.getChildren().addAll(new Label("License Plate:"), plateField, new Label("Vehicle Type:"), typeField,
                registerBtn);
        tab.setContent(vbox);
        return tab;
    }

    private Tab createEntryTab() {
        Tab tab = new Tab("Enter Vehicle");
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_LEFT);

        // Input Section
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField plateField = new TextField();
        plateField.setPromptText("License Plate");
        plateField.setPrefWidth(200);

        Label selectedSpotLabel = new Label("Selected Spot: None");
        selectedSpotLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Helper to hold selection state
        final String[] selectedSpotHolder = new String[] { null };

        // Visual Map Container
        VBox mapContainer = new VBox(20);
        mapContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(mapContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setPrefHeight(350);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Runnable refreshMap = () -> {
            mapContainer.getChildren().clear();
            selectedSpotHolder[0] = null;
            selectedSpotLabel.setText("Selected Spot: None");

            Set<ParkingGarage> garages = statusController.getAllGaragesStatus();
            if (garages == null || garages.isEmpty()) {
                mapContainer.getChildren().add(new Label("No garages available."));
            } else {
                for (ParkingGarage g : garages) {
                    Label garageHeader = new Label(g.getGarageName());
                    garageHeader.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                    FlowPane spotsPane = new FlowPane();
                    spotsPane.setHgap(10);
                    spotsPane.setVgap(10);
                    spotsPane.setPadding(new Insets(10, 0, 10, 0));

                    Set<ParkingSpot> available = statusController.getAvailableSpots(g.getGarageID());
                    Set<ParkingSpot> occupied = statusController.getOccupiedSpots(g.getGarageID());

                    List<ParkingSpot> allSpots = new ArrayList<>();
                    if (available != null)
                        allSpots.addAll(available);
                    if (occupied != null)
                        allSpots.addAll(occupied);
                    allSpots.sort(Comparator.comparing(ParkingSpot::getSpotNumber, String.CASE_INSENSITIVE_ORDER));

                    for (ParkingSpot s : allSpots) {
                        boolean isOccupied = false;
                        if (occupied != null) {
                            for (ParkingSpot occ : occupied) {
                                if (occ.getSpotID() == s.getSpotID()) {
                                    isOccupied = true;
                                    break;
                                }
                            }
                        }

                        Label spotBox = new Label(s.getSpotNumber());
                        spotBox.setPrefWidth(60);
                        spotBox.setPrefHeight(60);
                        spotBox.setAlignment(Pos.CENTER);

                        if (isOccupied) {
                            spotBox.setStyle(
                                    "-fx-background-color: #ffcccc; -fx-text-fill: #cc0000; -fx-border-color: #cc0000; -fx-border-radius: 5; -fx-background-radius: 5;");
                            spotBox.setTooltip(new Tooltip("Occupied"));
                        } else {
                            spotBox.setStyle(
                                    "-fx-background-color: #ccffcc; -fx-text-fill: #006600; -fx-border-color: #006600; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
                            spotBox.setOnMouseClicked(event -> {
                                // Reset styles of other spots (simplified: just rebuild or we'd need a list of
                                // labels)
                                // For simple visual feedback, we'll just highlight this one and update label
                                // To clear previous selection visually, simpler to rely on the "Selected Spot"
                                // label for now
                                // or iterate all children to reset style. Let's just update label and highlight
                                // this one.

                                // Reset all spots in this pane to default green style (hacky but works for this
                                // scope)
                                // A better way would be to track the currently selected Label object.

                                selectedSpotHolder[0] = s.getSpotNumber();
                                selectedSpotLabel.setText("Selected Spot: " + s.getSpotNumber());

                                // Visual selection feedback
                                // reset all
                                spotsPane.getChildren().forEach(node -> {
                                    if (node instanceof Label) {
                                        Label l = (Label) node;
                                        if (!"Occupied"
                                                .equals(l.getTooltip() != null ? l.getTooltip().getText() : "")) {
                                            l.setStyle(
                                                    "-fx-background-color: #ccffcc; -fx-text-fill: #006600; -fx-border-color: #006600; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
                                        }
                                    }
                                });
                                // highlight current
                                spotBox.setStyle(
                                        "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0; -fx-border-color: #1565c0; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold;");
                            });
                        }
                        spotsPane.getChildren().add(spotBox);
                    }

                    VBox garageBlock = new VBox(5);
                    garageBlock.setStyle(
                            "-fx-background-color: white; -fx-padding: 10; -fx-border-color: #eee; -fx-border-radius: 5;");
                    garageBlock.getChildren().addAll(garageHeader, new Separator(), spotsPane);
                    mapContainer.getChildren().add(garageBlock);
                }
            }
        };

        Button refreshBtn = new Button("Refresh Map");
        refreshBtn.setOnAction(e -> refreshMap.run());

        // Initial load
        Platform.runLater(refreshMap);

        Button enterBtn = new Button("Enter Garage");
        enterBtn.setStyle("-fx-base: #4caf50; -fx-font-weight: bold;");
        enterBtn.setOnAction(e -> {
            String plate = plateField.getText().trim();
            String spot = selectedSpotHolder[0];
            int gateId = 1; // Default gate

            if (plate.isEmpty()) {
                System.out.println("Error: License Plate is required.");
                return;
            }

            if (spot == null || spot.isEmpty()) {
                // Try auto-assign if no spot selected
                entryController.handleEntryRequest(plate, gateId);
            } else {
                entryController.handleManualEntryRequest(plate, gateId, spot);
            }
            // Clear inputs
            plateField.clear();
            selectedSpotHolder[0] = null;
            selectedSpotLabel.setText("Selected Spot: None");
            refreshMap.run(); // Refresh after entry to show updated status
        });

        inputBox.getChildren().addAll(new Label("License Plate:"), plateField, enterBtn, refreshBtn);

        mainLayout.getChildren().addAll(inputBox, new Separator(), new Label("Select a Spot (Optional):"),
                selectedSpotLabel, scrollPane);
        tab.setContent(mainLayout);
        return tab;
    }

    private Tab createExitTab() {
        Tab tab = new Tab("Exit Vehicle");
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);

        TextField plateField = new TextField();
        plateField.setPromptText("License Plate");

        Button exitBtn = new Button("Exit Garage");
        exitBtn.setOnAction(e -> {
            String plate = plateField.getText().trim();
            int gateId = 1; // Default gate

            if (plate.isEmpty()) {
                System.out.println("Error: License Plate is required.");
                return;
            }

            exitController.handleExitRequest(plate, gateId);
            plateField.clear();
        });

        vbox.getChildren().addAll(new Label("License Plate:"), plateField, exitBtn);
        tab.setContent(vbox);
        return tab;
    }

    private Tab createStatusTab() {
        Tab tab = new Tab("Parking Status");
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_LEFT);

        Button refreshBtn = new Button("Refresh Status");

        // Container for garage status blocks
        VBox garagesContainer = new VBox(20);
        garagesContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(garagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        // Give it some height
        scrollPane.setPrefHeight(400);

        refreshBtn.setOnAction(e -> {
            garagesContainer.getChildren().clear();

            Set<ParkingGarage> garages = statusController.getAllGaragesStatus();
            if (garages == null || garages.isEmpty()) {
                garagesContainer.getChildren().add(new Label("No garages found."));
            } else {
                for (ParkingGarage g : garages) {
                    // Garage Header
                    Label garageHeader = new Label(g.getGarageName() + ": " + g.countAvailableSlots() + " Available");
                    garageHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

                    // Container for spots
                    FlowPane spotsPane = new FlowPane();
                    spotsPane.setHgap(10);
                    spotsPane.setVgap(10);
                    spotsPane.setPadding(new Insets(10, 0, 10, 0));

                    // Fetch and merge spots (logic adapted from ParkingStatusTest)
                    Set<ParkingSpot> available = statusController.getAvailableSpots(g.getGarageID());
                    Set<ParkingSpot> occupied = statusController.getOccupiedSpots(g.getGarageID());

                    List<ParkingSpot> allSpots = new ArrayList<>();
                    if (available != null)
                        allSpots.addAll(available);
                    if (occupied != null)
                        allSpots.addAll(occupied);

                    // Sort spots
                    allSpots.sort(Comparator.comparing(ParkingSpot::getSpotNumber, String.CASE_INSENSITIVE_ORDER));

                    if (allSpots.isEmpty()) {
                        spotsPane.getChildren().add(new Label("No spots configured."));
                    } else {
                        for (ParkingSpot s : allSpots) {
                            // Determine status
                            boolean isOccupied = false;
                            if (occupied != null) {
                                for (ParkingSpot occ : occupied) {
                                    if (occ.getSpotID() == s.getSpotID()) {
                                        isOccupied = true;
                                        break;
                                    }
                                }
                            }

                            // Create Visual Spot
                            Label spotLabel = new Label(s.getSpotNumber());
                            spotLabel.setPrefWidth(80);
                            spotLabel.setPrefHeight(40);
                            spotLabel.setAlignment(Pos.CENTER);

                            if (isOccupied) {
                                spotLabel.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #cc0000; " +
                                        "-fx-border-color: #cc0000; -fx-border-radius: 5; -fx-background-radius: 5;");
                            } else {
                                spotLabel.setStyle("-fx-background-color: #ccffcc; -fx-text-fill: #006600; " +
                                        "-fx-border-color: #006600; -fx-border-radius: 5; -fx-background-radius: 5;");
                            }

                            spotsPane.getChildren().add(spotLabel);
                        }
                    }

                    // Add garage block to container
                    VBox garageBlock = new VBox(5);
                    garageBlock.setStyle(
                            "-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
                    garageBlock.getChildren().addAll(garageHeader, new Separator(), spotsPane);

                    garagesContainer.getChildren().add(garageBlock);
                }
            }

            System.out.println("Parking status view refreshed.");
        });

        mainLayout.getChildren().addAll(refreshBtn, scrollPane);
        tab.setContent(mainLayout);
        return tab;
    }

    private void redirectSystemOut() {
        OutputStream out = new OutputStream() {
            private StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                if (b == '\n') {
                    flushBuffer();
                } else {
                    buffer.append((char) b);
                }
            }

            @Override
            public void write(byte[] b, int off, int len) {
                for (int i = 0; i < len; i++) {
                    write(b[off + i]);
                }
            }

            private void flushBuffer() {
                String message = buffer.toString().trim();
                buffer.setLength(0);
                if (!message.isEmpty()) {
                    Platform.runLater(() -> updateStatus(message));
                }
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
        String lowerMsg = message.toLowerCase();

        if (lowerMsg.contains("error") || lowerMsg.contains("fail") ||
                lowerMsg.contains("occupied") || lowerMsg.contains("not found") ||
                lowerMsg.startsWith("exception")) {
            statusLabel.setStyle(
                    "-fx-background-color: #ffe6e6; -fx-text-fill: #d32f2f; -fx-border-color: #ffcdd2; -fx-padding: 10; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle(
                    "-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32; -fx-border-color: #c8e6c9; -fx-padding: 10;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
