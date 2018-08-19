package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;

@Data
@Builder
@ToString
public class Pharmaceutical implements Business {

    @NonNull
    private Integer businessId;
    @NonNull
    private String businessName;
    @NonNull
    private BusinessType businessType;
    @NonNull
    private Address address;
    @NonNull
    private Integer ownerId;
    private HashMap<Medicine, Unit> availableMedicineMap;
    private String email;
    private String phoneNumber;
}
