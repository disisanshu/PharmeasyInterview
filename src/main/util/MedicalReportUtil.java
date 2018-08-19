package util;

import model.Prescription;

public class MedicalReportUtil {

    public static Prescription generateSamplePrescription(){
        Prescription prescription = Prescription.builder().medicalReportId(1).build();
        return prescription;
    }
}
