package manager;

import lombok.Data;
import lombok.NonNull;
import model.Pharmacist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data manager class for pharmacist
 */
@Data
public class PharmacistManager {

    private static PharmacistManager instance = null;

    private BufferedReader reader;
    private HashMap<Integer, Pharmacist> pharmacistMap;

    private PharmacistManager() {
        //maxPharmacistId = 0;
        reader = new BufferedReader(new InputStreamReader(System.in));
        pharmacistMap = new HashMap<>();
    }

    /**
     * To get the Singleton instance
     * @return
     */
    public static PharmacistManager getInstance() {
        if (instance == null) {
            synchronized (PatientManager.class) {
                if (instance == null) {
                    instance = new PharmacistManager();
                }
            }
        }
        return instance;
    }

    /**
     * To view all the pharmacists
     */
    public void viewPharmacists() {
        if (pharmacistMap == null || pharmacistMap.isEmpty()) {
            System.out.println("No pharmacist present!");
            return;
        }
        for (Map.Entry<Integer, Pharmacist> entry : pharmacistMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
        System.out.println();
    }

    /**
     * Add a pharmacist to the data set
     * @param doctor
     */
    public void addPharmacist(Pharmacist pharmacist) {
        if (pharmacistMap == null || pharmacistMap.isEmpty()) {
            pharmacistMap = new HashMap<>();
        }
        pharmacistMap.put(pharmacist.getUserId(), pharmacist);
    }
}
