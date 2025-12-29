import controller.VehicleEntryController;
import controller.VehicleExitController;
import controller.VehicleRegistrationController;
import test.ParkingStatusTest;

import java.util.Scanner;

/**
 * Main Application Entry Point.
 * Integrates Vehicle Entry, Exit, and Parking Status views.
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VehicleEntryController entryController = new VehicleEntryController();
        VehicleExitController exitController = new VehicleExitController();
        VehicleRegistrationController registrationController = new VehicleRegistrationController();

        while (true) {
            System.out.println("\n=== Garage Management System ===");
            System.out.println("0. Register System Vehicle");
            System.out.println("1. Enter Vehicle");
            System.out.println("2. Exit Vehicle");
            System.out.println("3. View Available Spots");
            System.out.println("4. Quit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine();

            switch (input) {
                case "0":
                    handleRegisterVehicle(scanner, registrationController);
                    break;
                case "1":
                    handleEnterVehicle(scanner, entryController);
                    break;
                case "2":
                    handleExitVehicle(scanner, exitController);
                    break;
                case "3":
                    System.out.println("\n--- Current Parking Status ---");
                    ParkingStatusTest.main(new String[] { "--list" });
                    break;
                case "4":
                    System.out.println("Goodbye.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleRegisterVehicle(Scanner scanner, VehicleRegistrationController controller) {
        System.out.println("\n--- Register Vehicle ---");
        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine().trim();
        if (licensePlate.isEmpty())
            return;

        System.out.print("Enter Vehicle Type (Default 'Visitor'): ");
        String type = scanner.nextLine().trim();
        if (type.isEmpty())
            type = "Visitor";

        controller.handleRegistrationRequest(licensePlate, type);
    }

    private static void handleEnterVehicle(Scanner scanner, VehicleEntryController controller) {
        System.out.println("\n--- Enter Vehicle ---");
        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine().trim();
        if (licensePlate.isEmpty())
            return;

        // Gate ID removed from prompt, defaulting to 1
        int gateId = 1;

        System.out.print("Enter Desired Spot Number (e.g. C-01) or leave empty for auto-assign: ");
        String spotNumber = scanner.nextLine().trim();

        if (spotNumber.isEmpty()) {
            controller.handleEntryRequest(licensePlate, gateId);
        } else {
            controller.handleManualEntryRequest(licensePlate, gateId, spotNumber);
        }
    }

    private static void handleExitVehicle(Scanner scanner, VehicleExitController controller) {
        System.out.println("\n--- Exit Vehicle ---");
        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine().trim();
        if (licensePlate.isEmpty())
            return;

        // Gate ID removed from prompt, defaulting to 1
        int gateId = 1;

        controller.handleExitRequest(licensePlate, gateId);
    }
}
