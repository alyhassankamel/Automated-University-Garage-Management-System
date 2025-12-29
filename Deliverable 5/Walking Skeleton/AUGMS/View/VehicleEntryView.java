package View;

import controller.VehicleEntryController;
import java.util.Scanner;

/**
 * CLI View for Vehicle Entry Use Case.
 */
public class VehicleEntryView {

    public static void main(String[] args) {
        VehicleEntryController controller = new VehicleEntryController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Vehicle Entry Simulation ===");
        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine();

        try {
            System.out.print("Enter Entry Gate ID: ");
            int gateId = Integer.parseInt(scanner.nextLine());
            controller.handleEntryRequest(licensePlate, gateId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Gate ID format.");
        }

        // Note: Scanner not closed here if meant to be part of larger app,
        // but for standalone it's fine.
    }
}
