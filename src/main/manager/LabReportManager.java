package manager;

import lombok.Data;
import lombok.NonNull;
import model.LabReport;
import model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data manager class for lab reports
 */
@Data
public class LabReportManager {

    private static LabReportManager instance = null;

    private BufferedReader reader;
    private HashMap<Integer, LabReport> labReportMap;

    private LabReportManager() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        labReportMap = new HashMap<>();
    }

    /**
     * To get the Singleton instance
     * @return
     */
    public static LabReportManager getInstance() {
        if (instance == null) {
            synchronized (LabReportManager.class) {
                if (instance == null) {
                    instance = new LabReportManager();
                }
            }
        }
        return instance;
    }

    /**
     * To view lab reports if the requestor is Authorized
     */
    public void viewLabReport(Integer labReportId, User requester) {
        if (!labReportMap.containsKey(labReportId) ||
                !labReportMap.get(labReportId).getPermittedUsers().contains(requester.getUserId())) {
            System.out.println("User doesn't have permission to view the report!");
            return;
        }
        System.out.println(labReportMap.get(labReportId).toString());
    }

    /**
     * Add a lab report to the data set
     * @param doctor
     */
    public LabReport addLabReport(LabReport labReport) {
        if (labReportMap == null || labReportMap.isEmpty()) {
            labReportMap = new HashMap<>();
        }
        labReportMap.put(labReport.getMedicalReportId(), labReport);
        return labReport;
    }
}
