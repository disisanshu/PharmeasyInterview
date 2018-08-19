package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashSet;

@Data
@Builder
@ToString
public class MedicalLab implements Business {

    private Integer businessId;
    @NonNull
    private String businessName;
    @NonNull
    private BusinessType businessType;
    @NonNull
    private Address address;
    @NonNull
    private Integer ownerId;
    private HashSet<LabReport> availableLabReport;
    private String email;
    private String phoneNumber;
}
