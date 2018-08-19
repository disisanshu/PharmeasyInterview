package manager;

import lombok.Data;
import lombok.NonNull;
import model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data manager class for patients
 */
@Data
public class PatientManager {

    private static PatientManager instance = null;

    private BufferedReader reader;
    private HashMap<Integer, Patient> patientMap;

    private PatientManager() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        patientMap = new HashMap<>();
    }

    /**
     * To get the Singleton instance
     * @return
     */
    public static PatientManager getInstance() {
        if (instance == null) {
            synchronized (PatientManager.class) {
                if (instance == null) {
                    instance = new PatientManager();
                }
            }
        }
        return instance;
    }

    /**
     * To view all the patients
     */
    public void viewPatients() {
        if (patientMap == null || patientMap.isEmpty()) {
            System.out.println("No patient present!");
            return;
        }
        for (Map.Entry<Integer, Patient> entry : patientMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
        System.out.println();
    }

    /**
     * Add a patient to the data set
     * @param doctor
     */
    public void addPatient(Patient patient) {
        if (patientMap == null || patientMap.isEmpty()) {
            patientMap = new HashMap<>();
        }
        patientMap.put(patient.getUserId(), patient);
    }

    public void addMedicalReportForPatient(Integer patientId, MedicalReport medicalReport) {
        if (!patientMap.containsKey(patientId)) {
            System.out.println("Patient not found!");
            return;
        }
        Patient patient = patientMap.get(patientId);
        patient.getMedicalReportMap().put(medicalReport.getMedicalReportId(), medicalReport);
    }

    public void changePermissionRequest(Integer permissionRequestId, Integer patientId,
                                        PermissionStatus status, MedicalReport medicalReport) {
        if (!patientMap.containsKey(patientId)) {
            System.out.println("Patient not found!");
            return;
        }
        Patient patient = patientMap.get(patientId);
        if (!patient.getPendingPermissionRequests().containsKey(permissionRequestId)) {
            System.out.println("No such permission request found for user!");
            return;
        }
        PermissionRequest permissionRequest = patient.getPendingPermissionRequests().get(permissionRequestId);
        permissionRequest.setPermissionStatus(status);
        if (status.equals(PermissionStatus.APPROVED)) {
            medicalReport.getPermittedUsers().add(permissionRequest.getRequesterId());
        }
        patient.getPendingPermissionRequests().remove(permissionRequestId);
    }
}
