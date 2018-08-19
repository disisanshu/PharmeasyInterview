package manager;

import lombok.NonNull;
import model.PermissionRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data manager class for permission requests
 */
public class PermissionRequestManager {

    private static PermissionRequestManager instance = null;

    @NonNull
    private static Integer maxPermissionRequestId;
    private static BufferedReader reader;
    private static HashMap<Integer, PermissionRequest> permissionRequestMap;

    private PermissionRequestManager() {
        maxPermissionRequestId = 0;
        reader = new BufferedReader(new InputStreamReader(System.in));
        permissionRequestMap = new HashMap<>();
    }

    /**
     * To get the Singleton instance
     * @return
     */
    public static PermissionRequestManager getInstance() {
        if (instance == null) {
            synchronized (PermissionRequestManager.class) {
                if (instance == null) {
                    instance = new PermissionRequestManager();
                }
            }
        }
        return instance;
    }

    /**
     * To view all the permission requests
     */
    public static void viewPermissionRequests() {
        if (permissionRequestMap == null || permissionRequestMap.isEmpty()) {
            System.out.println("No permission request present!");
            return;
        }
        for (Map.Entry<Integer, PermissionRequest> entry : permissionRequestMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
        System.out.println();
    }

    /**
     * Add a permission request to the data set
     * @param doctor
     */
    public PermissionRequest addPermissionRequest(PermissionRequest permissionRequest) {
        if (permissionRequestMap == null || permissionRequestMap.isEmpty()) {
            permissionRequestMap = new HashMap<>();
        }
        permissionRequest.setPermissionRequestId(maxPermissionRequestId);
        permissionRequestMap.put(maxPermissionRequestId, permissionRequest);
        maxPermissionRequestId++;
        return permissionRequest;
    }
}
