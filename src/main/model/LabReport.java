package model;

import lombok.*;

import java.util.HashMap;
import java.util.HashSet;

@Data
@AllArgsConstructor
@Builder
@ToString
public class LabReport implements MedicalReport {

    private Integer medicalReportId;
    @NonNull
    private String medicalTestName;
    @NonNull
    private MedicalReportType medicalReportType;
    private HashSet<Integer> permittedUsers;
    private Integer labId;
    private Doctor prescribedBy;
    private Patient ownedBy;
}
