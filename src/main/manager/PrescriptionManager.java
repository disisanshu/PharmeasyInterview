package manager;

import lombok.Data;
import lombok.NonNull;
import model.Prescription;
import model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data manager class for prescritions
 */
@Data
public class PrescriptionManager {

    private static PrescriptionManager instance = null;

    private BufferedReader reader;
    private HashMap<Integer, Prescription> prescriptionMap;

    private PrescriptionManager() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        prescriptionMap = new HashMap<>();
    }

    public static PrescriptionManager getInstance() {
        if (instance == null) {
            synchronized (PatientManager.class) {
                if (instance == null) {
                    instance = new PrescriptionManager();
                }
            }
        }
        return instance;
    }

    /**
     * To view prescription if the requestor is Authorized
     */
    public void viewPrescription(Integer prescriptionId, User requester) {
        if (!prescriptionMap.containsKey(prescriptionId) ||
                !prescriptionMap.get(prescriptionId).getPermittedUsers().contains(requester.getUserId())) {
            System.out.println("User doesn't have permission to view the report!");
            return;
        }
        System.out.println("Prescription: " + prescriptionMap.get(prescriptionId).getMedicalReportId());
    }

    /**
     * Add a prescription to the data set
     * @param doctor
     */
    public Prescription addPrescription(Prescription prescription) {
        if (prescriptionMap == null || prescriptionMap.isEmpty()) {
            prescriptionMap = new HashMap<>();
        }
        prescriptionMap.put(prescription.getMedicalReportId(), prescription);
        return prescription;
    }

    public boolean hasPermissions(Integer prescriptionId, User user) {
        if (!prescriptionMap.containsKey(prescriptionId)) {
            System.out.println("Prescription not found!");
            return false;
        }
        return prescriptionMap.get(prescriptionId).getPermittedUsers().contains(user.getUserId());
    }
}
