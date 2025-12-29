package View;

import controller.VehicleExitController;
import java.util.Scanner;

public class VehicleExitView {

    public static void main(String[] args) {
        VehicleExitController controller = new VehicleExitController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Vehicle Exit Simulator ===");
        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine();

        System.out.print("Enter Gate ID (integer): ");
        int gateId = 1;
        try {
            gateId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Gate ID, defaulting to 1");
        }

        controller.handleExitRequest(licensePlate, gateId);
        
        System.out.println("Simulation request sent.");
    }
}
