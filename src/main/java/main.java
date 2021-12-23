import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        try {
            var db = DatabaseHandler.getInstance();

            //Для повторного заполнения расскомментировать строку ниже и добавить обработку IOException
            //db.fillDataBase(Parser.parseCSV());

            var task = new Task(db);
            /*task.getCountriesEconomyBar();*/
            task.printHighEconomyCountry();
            task.printAverageEconomyCountry();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
