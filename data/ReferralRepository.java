package data;
import model.Refferal;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;


public class ReferralRepository {
    private final String filePath;
    public ReferralRepository(String filePath) {
        this.filePath = filePath;
    }
    public List<Refferal> loadRefferals() {
        List<Refferal> refferals = new ArrayList<>();
        List<String[]> rows = CSVFileHandler.readCSV(filePath);
        for (String[] row : rows) {
            Refferal refferal = new Refferal(
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    row[4],
                    row[5],
                    LocalDate.parse(row[6]),
                    row[7],
                    row[8],
                    row[9],
                    row[10],
                    row[11],
                    row[12],
                    row[13],
                    LocalDate.parse(row[14]),
                    LocalDate.parse(row[15])
            );
            refferals.add(refferal);
        }
    return refferals;
    }
}
