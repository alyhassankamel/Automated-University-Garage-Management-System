package test;
import controller.ParkingStatusController;
import entity.ParkingGarage;
import java.util.Set;

/**
 * Small runner that lists garages and exits — useful for testing DB connectivity.
 */
public class LiveListGarages {
    public static void main(String[] args) {
        ParkingStatusController controller = new ParkingStatusController();
        try {
            Set<ParkingGarage> garages = controller.getAllGaragesStatus();
            if (garages == null || garages.isEmpty()) {
                System.out.println("No garages found (driver or DB may be missing).");
                return;
            }
            System.out.println("Garages:");
            for (ParkingGarage g : garages) {
                System.out.println(" - ID=" + g.getGarageID() + " Name='" + g.getGarageName() + "' Total=" + g.getTotalSpots() + " Available=" + g.countAvailableSlots());
            }
        } catch (Exception e) {
            System.err.println("Error listing garages: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
