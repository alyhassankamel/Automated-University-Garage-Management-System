package AUGMS.entity;

/**
 * Enum representing garage access modes
 */
public enum GarageAccess {
    NORMAL,      // Normal access mode
    OVERRIDE,    // Override mode - allows all vehicles
    RESTRICTED,  // Restricted mode - only authorized vehicles
    CLOSED       // Garage is closed
}

