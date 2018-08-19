package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;

@Data
@Builder
@ToString
public class Doctor implements User {

    private Integer userId;
    @NonNull
    private String name;
    @NonNull
    private UserType userType;
    @NonNull
    private String email;
    private HashMap<Integer, Business> associatedBusiness;
    private String phoneNumber;
}
