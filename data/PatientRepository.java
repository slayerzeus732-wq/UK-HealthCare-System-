package data;
import model.Patient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
    private final String filePath;

    public PatientRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        List<String[]> rows = CSVFileHandler.readCSV(filePath);
        for (String[] row : rows) {
            if (row.length >= 14) {
                Patient patient = new Patient(
                        row[0], // userId
                        row[1], // firstName
                        row[2], // lastName
                        row[7], // email
                        row[6], // phone
                        row[8], // address
                        row[9], // postcode
                        "default", // password
                        LocalDate.parse(row[3]), // dob
                        row[4], // nhsNumber
                        row[5], // gender
                        row[10], // emergencyContactName
                        row[11], // emergencyContactPhone
                        LocalDate.parse(row[12]), // registrationDate
                        row[13], // gpSurgeryId
                        "", // medicalHistory
                        "" // insuranceNumber
                );
                patients.add(patient);
            }
        }
        return patients;
    }

    public void addPatient(Patient patient) {
        // Implement add logic, perhaps append to CSV
        // For simplicity, reload and save all
        List<Patient> patients = getAllPatients();
        patients.add(patient);
        saveAll(patients);
    }

    public Patient findPatientById(String patientId) {
        List<Patient> patients = getAllPatients();
        for (Patient p : patients) {
            if (p.getUserId().equals(patientId)) {
                return p;
            }
        }
        return null;
    }

    public void updatePatient(Patient updatedPatient) {
        List<Patient> patients = getAllPatients();
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getUserId().equals(updatedPatient.getUserId())) {
                patients.set(i, updatedPatient);
                break;
            }
        }
        saveAll(patients);
    }

    private void saveAll(List<Patient> patients) {
        List<String> lines = new ArrayList<>();
        lines.add("id,firstName,lastName,dob,nhsNumber,gender,phone,email,address,postcode,emergencyContactName,emergencyContactPhone,registrationDate,gpSurgeryId,password,medicalHistory,insuranceNumber"); // header
        for (Patient p : patients) {
            lines.add(p.getUserId() + "," + p.getFirstName() + "," + p.getLastName() + "," + p.getDob() + "," + p.getNhsNumber() + "," + p.getGender() + "," + p.getPhone() + "," + p.getEmail() + "," + p.getAddress() + "," + p.getPostcode() + "," + p.getEmergencyContactName() + "," + p.getEmergencyContactPhone() + "," + p.getRegistrationDate() + "," + p.getGpSurgeryId() + "," + p.getPassword() + "," + p.getMedicalHistory() + "," + p.getInsuranceNumber());
        }
        CSVFileHandler.writeCSV(filePath, lines);
    }
}