#PharmEasy Interview Assignment

* 3 types of Roles Patient, Doctor and Pharmacist.
* The Patient has medical records/prescriptions.
* Doctor & Pharmacist asks for a patient’s prescriptions & medical records, the patient has to approve it.
* Patient’s data can be shared only if they approve it.

#Solution

The App is maintaining data in memory.

Data Models are shared below:

_**Interfaces**_

* User
* MedicalReport
* Business

Classes implemented on **_User_**
```
* Patient
* Doctor
* Pharmacist
```

Classes implemented on _**MedicalReport**_
```
* Prescription
* LabReport
```

Classes implemented on **_Business_**
```
* Hospital
* Medical Lab
* Pharmaceutical
```
Business is currently not being used as it wasn't in the scope of problem statement but has been included to accomodate further use cases.

Other data models:
```
* Address
* Medicine
* Unit
* Permission Request

* UserType
* MedicalReportType
* BusinessType
* UnitType
```

To handle data in memory, separate managers has been written to edit and maintain data flow with the App.
```
* DoctorManager
* PatientManager
* PharmacistManager

* PrescriptionManager
* LabReportManager

* PermissionReuqestManager
```
The Application is initialized with sample data containing 10 Patients, 5 Doctors and 3 Pharmacists to run and check mentioned functional requirements of the Application.

##How to run and execute the App

######Prerequisite

* Lombok (a Java library)
* JDK

######Command to execute the App

Download Lombok from `https://projectlombok.org/download` and save it to your Downloads folder

****javac -cp ~/Downloads/lombok.jar model/*.java manager/*.java && javac -cp . App.java && java App**

