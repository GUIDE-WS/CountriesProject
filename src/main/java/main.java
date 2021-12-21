import java.io.IOException;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        var isCreated = true;

        try {
            var db = DatabaseHandler.getInstance();
            if (!isCreated) {
                db.fillDataBase(Parser.parseCSV());
            }
            var task = new Task(db);
            task.getCountriesEconomyBar();
            task.printHighEconomyCountry();
            task.printAverageEconomyCountry();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
