package model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
@Builder
//@ToString
public class Prescription implements MedicalReport {

    private Integer medicalReportId;
    @NonNull
    private MedicalReportType medicalReportType;
    private HashSet<Integer> permittedUsers;
    private Doctor prescribedBy;
    private Patient ownedBy;
    private HashMap<Medicine, Unit> prescribedMedicinesQuantityMap;
    private List<LabReport> labReports;
}
