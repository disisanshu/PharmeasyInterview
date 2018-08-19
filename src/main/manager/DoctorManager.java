package manager;

import lombok.Data;
import lombok.NonNull;
import model.Doctor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data manager class for doctors
 */
@Data
public class DoctorManager {

    private static DoctorManager instance = null;

    private BufferedReader reader;
    private HashMap<Integer, Doctor> doctorMap;

    private DoctorManager() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        doctorMap = new HashMap<>();
    }

    /**
     * To get the Singleton instance
     * @return
     */
    public static DoctorManager getInstance() {
        if (instance == null) {
            synchronized (DoctorManager.class) {
                if (instance == null) {
                    instance = new DoctorManager();
                }
            }
        }
        return instance;
    }

    /**
     * To view all the doctors
     */
    public void viewDoctors() {
        if (doctorMap == null || doctorMap.isEmpty()) {
            System.out.println("No doctors present!");
            return;
        }
        for (Map.Entry<Integer, Doctor> entry : doctorMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
        System.out.println();
    }

    /**
     * Add a doctor to the data set
     * @param doctor
     */
    public void addDoctor(Doctor doctor) {
        if (doctorMap == null || doctorMap.isEmpty()) {
            doctorMap = new HashMap<>();
        }
        doctorMap.put(doctor.getUserId(), doctor);
    }
}
