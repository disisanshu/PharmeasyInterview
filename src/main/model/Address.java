package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@Builder
@ToString
public class Address {

    @NonNull
    private String addressId;
    @NonNull
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String landmark;
    @NonNull
    private String city;
    @NonNull
    private String state;
    @NonNull
    private String country;
    @NonNull
    private String zipCode;
}
