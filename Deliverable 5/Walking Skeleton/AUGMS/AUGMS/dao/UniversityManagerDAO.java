package AUGMS.dao;

import AUGMS.entity.UniversityManager;
import java.io.*;
import java.util.*;

/**
 * Data Access Object for UniversityManager
 */
public class UniversityManagerDAO {

    private Map<String, UniversityManager> managers;
    private Map<String, UniversityManager> managersByUniID;

    /**
     * Default constructor
     */
    public UniversityManagerDAO() {
        this.managers = new HashMap<>();
        this.managersByUniID = new HashMap<>();
    }

    /**
     * @param managerID
     * @return
     */
    public UniversityManager findManager(String managerID) {
        return managers.get(managerID);
    }

    /**
     * @param uniID
     * @return
     */
    public UniversityManager findManagerByUniID(String uniID) {
        return managersByUniID.get(uniID);
    }

    /**
     * @param manager
     * @return
     */
    public void updateManager(UniversityManager manager) {
        if (manager != null) {
            managers.put(manager.getManagerID(), manager);
            managersByUniID.put(manager.getUniID(), manager);
        }
    }

    public void saveManager(UniversityManager manager) {
        updateManager(manager);
    }

    public List<UniversityManager> getAllManagers() {
        return new ArrayList<>(managers.values());
    }
}
