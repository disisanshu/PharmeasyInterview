package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@ToString
public class Pharmacist implements User {

    private Integer userId;
    @NonNull
    private String name;
    @NonNull
    private UserType userType;
    private HashMap<Integer, Business> associatedBusiness;
    @NonNull
    private String email;
    private String phoneNumber;
}
