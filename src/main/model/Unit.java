package model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Unit {
    @NonNull
    private Integer value;
    @NonNull
    private UnitType unitType;
}
