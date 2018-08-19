package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class Hospital implements Business {

    private Integer businessId;
    @NonNull
    private String businessName;
    @NonNull
    private BusinessType businessType;
    @NonNull
    private Address address;
    @NonNull
    private Integer ownerId;
    private List<String> doctorsList;
    private String email;
    private String phoneNumber;
}
