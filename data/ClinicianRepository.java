package data;
import model.Clinician;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class ClinicianRepository {
    private final String filePath;
    public ClinicianRepository(String filePath) {
        this.filePath = filePath;
    }
    public List<Clinician> loadClinicians() {
        List<Clinician> clinicians = new ArrayList<>();
        List<String[] >rows =CSVFileHandler.readCSV(filePath);
        for (String[] row : rows) {
            clinicians.add(new Clinician(
                    row[0],                 // clinician_id
                    row[1],                 // first_name
                    row[2],                 // last_name
                    row[3],                 // title
                    row[4],                 // speciality
                    row[5],                 // registration number (GMC / NMC)
                    row[6],                 // phone
                    row[7],                 // email
                    row[8],                 // workplace_id
                    row[9],                 // workplace_type
                    row[10],                // employment_status
                    LocalDate.parse(row[11])// start_date

            ));
        }
        return clinicians;
    }
    public void addClinician(Clinician clinician) {
      try(FileWriter fw = new FileWriter(filePath)) {
          fw.write(clinician.toCSV()+"\n");
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
    public void deleteClinician(String clinicianId) {
        List<Clinician> clinicians = loadClinicians();
        clinicians.removeIf(c -> c.getClinicianId().equals(clinicianId));
        saveAll(clinicians);
    }
    public void saveAll(List<Clinician> clinicians) {
        List<String> lines = new ArrayList<>();
        for (Clinician clinician : clinicians) {
            lines.add(clinician.toCSV());
        }
        CSVFileHandler.writeCSV(filePath, lines);
    }
    public void updateClinician(Clinician updatedClinician) {
        List<Clinician> clinicians = loadClinicians();
        for(int i=0; i<clinicians.size();i++) {
            if(clinicians.get(i).getClinicianId().equals(updatedClinician.getClinicianId())) {
                clinicians.set(i, updatedClinician);
                break;
            }
        }
        saveAll(clinicians);
    }
}
