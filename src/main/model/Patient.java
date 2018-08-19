package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;

@Data
@Builder
public class Patient implements User {

    private Integer userId;
    @NonNull
    private String name;
    @NonNull
    private UserType userType;
    @NonNull
    private String email;
    private String phoneNumber;
    private HashMap<Integer, MedicalReport> medicalReportMap;
    private HashMap<Integer, PermissionRequest> pendingPermissionRequests;
}
