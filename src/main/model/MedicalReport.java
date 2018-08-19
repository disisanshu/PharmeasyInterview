package model;

import java.util.HashSet;

public interface MedicalReport {

    Integer getMedicalReportId();

    MedicalReportType getMedicalReportType();

    HashSet<Integer> getPermittedUsers();

    Patient getOwnedBy();

    Doctor getPrescribedBy();
}
