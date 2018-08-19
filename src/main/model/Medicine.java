package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;

@Data
@Builder
@ToString
public class Medicine {
    @NonNull
    private String medicineId;
    @NonNull
    private String medicineName;
    private String brandId;
    private HashMap<String, Unit> compositionMap;
}
