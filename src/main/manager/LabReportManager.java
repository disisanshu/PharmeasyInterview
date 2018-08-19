package manager;

import lombok.Data;
import lombok.NonNull;
import model.LabReport;
import model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Data
public class LabReportManager {

    private static LabReportManager instance = null;

    private BufferedReader reader;
    private HashMap<Integer, LabReport> labReportMap;

    private LabReportManager() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        labReportMap = new HashMap<>();
    }

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

    public void viewLabReports() {
        if (labReportMap == null || labReportMap.isEmpty()) {
            System.out.println("No prescriptions present!");
            return;
        }
        for (Map.Entry<Integer, LabReport> entry : labReportMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
        System.out.println();
    }

    public void viewLabReport(Integer labReportId, User requester) {
        if (!labReportMap.get(labReportId).getPermittedUsers().contains(requester.getUserId())) {
            System.out.println("User doesn't have permission to view the report!");
        }
        System.out.println(labReportMap.get(labReportId).toString());
    }

    public LabReport addLabReport(LabReport labReport) {
        if (labReportMap == null || labReportMap.isEmpty()) {
            labReportMap = new HashMap<>();
        }
        labReportMap.put(labReport.getMedicalReportId(), labReport);
        return labReport;
    }
}
