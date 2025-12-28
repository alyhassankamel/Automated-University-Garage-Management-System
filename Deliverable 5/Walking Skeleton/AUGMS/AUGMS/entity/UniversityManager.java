package AUGMS.entity;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class UniversityManager {

    /**
     * Default constructor
     */
    public UniversityManager() {
    }

    /**
     * 
     */
    private String uniID;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String managerID;


    // Getters and Setters
    public String getUniID() {
        return uniID;
    }

    public void setUniID(String uniID) {
        this.uniID = uniID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    // Constructors
    public UniversityManager(String managerID, String uniID, String name, String phone) {
        this.managerID = managerID;
        this.uniID = uniID;
        this.name = name;
        this.phone = phone;
    }

    /**
     * @param alert 
     * @return
     */
    public void receiveSystemWideAlert(String alert) {
        // Implementation for receiving alerts
        System.out.println("Alert received: " + alert);
    }

    /**
     * @param garageID 
     * @param access 
     * @return
     */
    public void manageGarageAccess(String garageID, GarageAccess access) {
        // Implementation for managing garage access
        System.out.println("Managing garage " + garageID + " access: " + access);
    }

    /**
     * @return
     */
    public Set<String> reviewAlertPatterns() {
        // Implementation for reviewing alert patterns
        return new HashSet<>();
    }

    /**
     * @param action 
     * @return
     */
    public void authorizeEscalationActions(String action) {
        // Implementation for authorizing escalation actions
        System.out.println("Authorizing escalation action: " + action);
    }

}
