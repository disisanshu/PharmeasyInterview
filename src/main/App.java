import manager.*;
import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Application class
 */
public class App {

    private static PatientManager patientManager;
    private static DoctorManager doctorManager;
    private static PharmacistManager pharmacistManager;

    private static PrescriptionManager prescriptionManager;
    private static LabReportManager labReportManager;

    private static PermissionRequestManager permissionRequestManager;

    private static Integer maxUserId = 0;
    private static Integer maxMedicalReportId = 0;

    private static final String EMAIL_SIGNATURE = "@xyz.com";
    private static final String PHONE_NUMBER = "1234567890";

    private static void addSamplePatients(int count) {
        if (count == 0)
            return;
        for (int i = 0; i < count; i++) {
            Patient patient = Patient.builder().userId(maxUserId).email("patient" + i + EMAIL_SIGNATURE)
                    .phoneNumber(PHONE_NUMBER).name("Patient" + i).userType(UserType.PATIENT)
                    .medicalReportMap(new HashMap<>()).pendingPermissionRequests(new HashMap<>()).build();
            patientManager.addPatient(patient);
            maxUserId++;
        }
    }

    private static void addSampleDoctors(int count) {
        if (count == 0)
            return;
        for (int i = 0; i < count; i++) {
            Doctor doctor = Doctor.builder().userId(maxUserId).email("doctor" + i + EMAIL_SIGNATURE)
                    .phoneNumber(PHONE_NUMBER).name("Doctor" + i).userType(UserType.DOCTOR).build();
            doctorManager.addDoctor(doctor);
            maxUserId++;
        }
    }

    private static void addSamplePharmacist(int count) {
        if (count == 0)
            return;
        for (int i = 0; i < count; i++) {
            Pharmacist pharmacist = Pharmacist.builder().userId(maxUserId).email("pharmacist" + i + EMAIL_SIGNATURE)
                    .phoneNumber(PHONE_NUMBER).name("Pharmacist" + i).userType(UserType.PHARMACIST).build();
            pharmacistManager.addPharmacist(pharmacist);
            maxUserId++;
        }
    }

    private static void addSampleMedicalReportsForPatient() {
        HashMap<Integer, Patient> patientMap = patientManager.getPatientMap();
        if (patientMap == null)
            return;
        Iterator<Map.Entry<Integer, Doctor>> doctorIterator = doctorManager.getDoctorMap().entrySet().iterator();
        Doctor randomDoctor = null;
        for (Map.Entry<Integer, Patient> entry :
                patientMap.entrySet()) {
            if (doctorIterator.hasNext())
                randomDoctor = doctorIterator.next().getValue();

            HashSet<Integer> permittedUsersMap = new HashSet<>();
            permittedUsersMap.add(entry.getKey());
            if (randomDoctor != null) {
                permittedUsersMap.add(randomDoctor.getUserId());
            }
            Prescription prescription = Prescription.builder().medicalReportId(maxMedicalReportId)
                    .prescribedBy((randomDoctor != null) ? doctorManager.getDoctorMap().get(randomDoctor.getUserId())
                            : null).ownedBy(patientManager.getPatientMap().get(entry.getKey()))
                    .medicalReportType(MedicalReportType.PRESCRIPTION).permittedUsers(permittedUsersMap).build();
            prescription = prescriptionManager.addPrescription(prescription);
            patientManager.addMedicalReportForPatient(entry.getKey(), prescription);
            maxMedicalReportId++;

            LabReport labReport = LabReport.builder().medicalReportId(maxMedicalReportId).labId(123)
                    .medicalTestName("Test")
                    .prescribedBy((randomDoctor != null) ? doctorManager.getDoctorMap().get(randomDoctor.getUserId())
                            : null).ownedBy(patientManager.getPatientMap().get(entry.getKey()))
                    .medicalReportType(MedicalReportType.LAB_REPORT).permittedUsers(permittedUsersMap).build();
            labReport = labReportManager.addLabReport(labReport);
            patientManager.addMedicalReportForPatient(entry.getKey(), labReport);
            maxMedicalReportId++;
        }
    }

    private static void addSampleData() {
        addSamplePatients(10);
        addSampleDoctors(5);
        addSamplePharmacist(3);
        addSampleMedicalReportsForPatient();
    }

    private static void initializeAppWithSampleData() {
        patientManager = PatientManager.getInstance();
        doctorManager = DoctorManager.getInstance();
        pharmacistManager = PharmacistManager.getInstance();
        prescriptionManager = PrescriptionManager.getInstance();
        labReportManager = LabReportManager.getInstance();
        permissionRequestManager = PermissionRequestManager.getInstance();
        addSampleData();
    }

    private static Patient getOwnerForMedicalReport(MedicalReport medicalReport) {
        if (medicalReport == null) {
            return null;
        }
        return medicalReport.getOwnedBy();
    }

    private static void viewMedicalReport(MedicalReport medicalReport, User requester) {
        if (medicalReport == null) {
            System.out.println("Invalid medical report!");
            return;
        }
        switch (medicalReport.getMedicalReportType()) {
            case PRESCRIPTION:
                prescriptionManager.viewPrescription(medicalReport.getMedicalReportId(), requester);
                break;
            case LAB_REPORT:
                labReportManager.viewLabReport(medicalReport.getMedicalReportId(), requester);
                break;
            default:
                System.out.println("Unrecognised report!");
        }
    }

    private static boolean checkPermissionForMedicalReport(MedicalReport medicalReport, User requester) {
        return medicalReport.getPermittedUsers().contains(requester.getUserId());
    }

    private static void processPermissionRequest(Integer permissionRequestId, Integer patientId,
                                                 PermissionStatus status, MedicalReport medicalReport) {
        if (medicalReport == null) {
            System.out.println("Invalid request!");
            return;
        }
        patientManager.changePermissionRequest(permissionRequestId, patientId, status, medicalReport);
    }

    private static PermissionRequest raisePermissionRequest(User requester, Patient patient, MedicalReport medicalReport) {
        if (medicalReport == null || patient == null || requester == null) {
            System.out.println("Invalid request!");
            return null;
        }
        if (!patient.getMedicalReportMap().containsKey(medicalReport.getMedicalReportId())) {
            System.out.println("Patient is not the owner of medical report!");
            return null;
        }
        if (medicalReport.getPermittedUsers().contains(requester.getUserId())) {
            System.out.println("Requester already has permission for document.");
            return null;
        }
        PermissionRequest permissionRequest = PermissionRequest.builder().permissionStatus(PermissionStatus.PENDING)
                .requesterId(requester.getUserId()).reportOwnerId(patient.getUserId())
                .medicalReportId(medicalReport.getMedicalReportId()).build();
        permissionRequest = permissionRequestManager.addPermissionRequest(permissionRequest);
        HashMap<Integer, PermissionRequest> pendingPermissionRequest = patient.getPendingPermissionRequests();
        pendingPermissionRequest.put(permissionRequest.getPermissionRequestId(), permissionRequest);
        return permissionRequest;
    }

    /**
     * Main App method. It initializes app with sample data and tests several and multiple use cases which are
     * supported in the App.
     * @param args
     */
    public static void main(String args[]) {
        // initializing App with sample data
        initializeAppWithSampleData();

        Doctor doctor1 = doctorManager.getDoctorMap().get(10);
        Doctor doctor2 = doctorManager.getDoctorMap().get(11);
        Doctor doctor3 = doctorManager.getDoctorMap().get(12);
        Doctor doctor4 = doctorManager.getDoctorMap().get(13);
        Doctor doctor5 = doctorManager.getDoctorMap().get(14);

        Pharmacist pharmacist1 = pharmacistManager.getPharmacistMap().get(15);
        Pharmacist pharmacist2 = pharmacistManager.getPharmacistMap().get(16);
        Pharmacist pharmacist3 = pharmacistManager.getPharmacistMap().get(17);

        Prescription medicalReport1 = prescriptionManager.getPrescriptionMap().get(0);
        Prescription medicalReport2 = prescriptionManager.getPrescriptionMap().get(2);
        Prescription medicalReport3 = prescriptionManager.getPrescriptionMap().get(4);
        LabReport medicalReport4 = labReportManager.getLabReportMap().get(1);
        LabReport medicalReport5 = labReportManager.getLabReportMap().get(1);

        // Execution of various test cases
        System.out.println("#Test1");
        if (checkPermissionForMedicalReport(medicalReport1, doctor1)) {
            System.out.println(doctor1.getName() + " has permissions for " + medicalReport1.getMedicalReportId());
            viewMedicalReport(medicalReport1, doctor1);
        } else {
            System.out.println(doctor1.getName() + " does not has permissions for report "
                    + medicalReport1.getMedicalReportId());
            System.out.println("Raising permission request to user.");
            PermissionRequest permissionRequest = raisePermissionRequest(doctor1,
                    getOwnerForMedicalReport(medicalReport1), medicalReport1);
            if (permissionRequest != null) {
                System.out.println("Permission request raised successfully: " + permissionRequest.toString());
            } else {
                System.out.println("Permission request cannot be raised.");
                return;
            }
            processPermissionRequest(permissionRequest.getPermissionRequestId(), medicalReport1.getOwnedBy().getUserId()
                    , PermissionStatus.APPROVED, medicalReport1);
        }
        System.out.println();
        System.out.println("#Test2");
        if (checkPermissionForMedicalReport(medicalReport3, doctor2)) {
            System.out.println(doctor2.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor2);
        } else {
            System.out.println(doctor2.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
            System.out.println("Raising permission request to user.");
            PermissionRequest permissionRequest = raisePermissionRequest(doctor2,
                    getOwnerForMedicalReport(medicalReport1), medicalReport3);
            if (permissionRequest != null) {
                System.out.println("Permission request raised successfully: " + permissionRequest.toString());
                System.out.println("Processing permission reuqest with status " + PermissionStatus.APPROVED);
                processPermissionRequest(permissionRequest.getPermissionRequestId(), medicalReport3.getOwnedBy().getUserId()
                        , PermissionStatus.APPROVED, medicalReport3);
            } else {
                System.out.println("Permission request cannot be raised.");
            }
        }
        System.out.println();
        System.out.println("#Test3");
        if (checkPermissionForMedicalReport(medicalReport3, pharmacist1)) {
            System.out.println(pharmacist1.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor2);
        } else {
            System.out.println(pharmacist1.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
            System.out.println("Raising permission request to user.");
            PermissionRequest permissionRequest = raisePermissionRequest(pharmacist1,
                    getOwnerForMedicalReport(medicalReport3), medicalReport3);
            if (permissionRequest != null) {
                System.out.println("Permission request raised successfully: " + permissionRequest.toString());
                System.out.println("Processing permission request with status " + PermissionStatus.DECLINED);
                processPermissionRequest(permissionRequest.getPermissionRequestId(), medicalReport3.getOwnedBy().getUserId()
                        , PermissionStatus.DECLINED, medicalReport3);
            } else {
                System.out.println("Permission request cannot be raised.");
            }
        }
        System.out.println();
        System.out.println("#Test4");
        if (checkPermissionForMedicalReport(medicalReport3, doctor2)) {
            System.out.println(doctor2.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor2);
        } else {
            System.out.println(doctor2.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
            System.out.println("Raising permission request to user.");
            PermissionRequest permissionRequest = raisePermissionRequest(doctor2,
                    getOwnerForMedicalReport(medicalReport3), medicalReport3);
            if (permissionRequest != null) {
                System.out.println("Permission request raised successfully: " + permissionRequest.toString());
                System.out.println("\nPending requests for Patient: " + medicalReport3.getOwnedBy().getName());
                System.out.println();
                System.out.println(medicalReport3.getOwnedBy().getPendingPermissionRequests());
                System.out.println("Processing permission request with status " + PermissionStatus.APPROVED);
                processPermissionRequest(permissionRequest.getPermissionRequestId(), medicalReport3.getOwnedBy().getUserId()
                        , PermissionStatus.APPROVED, medicalReport3);
            } else {
                System.out.println("Permission request cannot be raised.");
            }
        }
        System.out.println();
        System.out.println("#Test5");
        if (checkPermissionForMedicalReport(medicalReport3, pharmacist1)) {
            System.out.println(pharmacist1.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor2);
        } else {
            System.out.println(pharmacist1.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test6");
        if (checkPermissionForMedicalReport(medicalReport3, doctor2)) {
            System.out.println(doctor2.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor2);
        } else {
            System.out.println(doctor2.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test7");
        if (checkPermissionForMedicalReport(medicalReport3, doctor3)) {
            System.out.println(doctor3.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor3);
        } else {
            System.out.println(doctor3.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test8");
        if (checkPermissionForMedicalReport(medicalReport3, doctor4)) {
            System.out.println(doctor4.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor4);
        } else {
            System.out.println(doctor4.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test9");
        if (checkPermissionForMedicalReport(medicalReport3, doctor5)) {
            System.out.println(doctor5.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor5);
        } else {
            System.out.println(doctor5.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test10");
        if (checkPermissionForMedicalReport(medicalReport3, pharmacist2)) {
            System.out.println(pharmacist2.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, pharmacist2);
        } else {
            System.out.println(pharmacist2.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
            System.out.println("Raising permission request to user.");
            PermissionRequest permissionRequest = raisePermissionRequest(pharmacist2,
                    getOwnerForMedicalReport(medicalReport3), medicalReport3);
            if (permissionRequest != null) {
                System.out.println("Permission request raised successfully: " + permissionRequest.toString());
                System.out.println("Processing permission request with status " + PermissionStatus.APPROVED);
                processPermissionRequest(permissionRequest.getPermissionRequestId(), medicalReport3.getOwnedBy().getUserId()
                        , PermissionStatus.APPROVED, medicalReport3);
            } else {
                System.out.println("Permission request cannot be raised.");
            }
        }
        System.out.println();
        System.out.println("#Test11");
        if (checkPermissionForMedicalReport(medicalReport3, pharmacist2)) {
            System.out.println(pharmacist2.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, pharmacist2);
        } else {
            System.out.println(pharmacist2.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test12");
        if (checkPermissionForMedicalReport(medicalReport4, pharmacist2)) {
            System.out.println(pharmacist2.getName() + " has permissions for " + medicalReport4.getMedicalReportId());
            viewMedicalReport(medicalReport4, pharmacist2);
        } else {
            System.out.println(pharmacist2.getName() + " does not has permissions for report "
                    + medicalReport4.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test13");
        if (checkPermissionForMedicalReport(medicalReport5, doctor2)) {
            System.out.println(pharmacist2.getName() + " has permissions for " + medicalReport3.getMedicalReportId());
            viewMedicalReport(medicalReport3, doctor2);
        } else {
            System.out.println(doctor2.getName() + " does not has permissions for report "
                    + medicalReport3.getMedicalReportId());
        }
        System.out.println();
        System.out.println("#Test14");
        raisePermissionRequest(doctor2, medicalReport2.getOwnedBy(), medicalReport2);
        System.out.println();
        System.out.println("#Test15");
        viewMedicalReport(null, pharmacist3);
    }
}
