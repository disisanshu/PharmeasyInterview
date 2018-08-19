package manager;

import lombok.NonNull;
import model.PermissionRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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
