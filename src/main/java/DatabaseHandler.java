import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String url = "jdbc:sqlite:country.s3db";
    private static DatabaseHandler instance = null;
    private final Connection connection;

    private DatabaseHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(url);
    }

    public static DatabaseHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DatabaseHandler();
        return instance;
    }

    public void addCountry(Country country) {
        try {
            var statement = connection.prepareStatement(
                    "INSERT INTO Countries(name, happinessRank, happinessScore, standardError, economy, family, health, freedom, trust, generosity, dystopiaResidual) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setObject(1, country.name);
            statement.setObject(2, country.happinessRank);
            statement.setObject(3, country.happinessScore);
            statement.setObject(4, country.standardError);
            statement.setObject(5, country.economy);
            statement.setObject(6, country.family);
            statement.setObject(7, country.health);
            statement.setObject(8, country.freedom);
            statement.setObject(9, country.trust);
            statement.setObject(10, country.generosity);
            statement.setObject(11, country.dystopiaResidual);
            statement.execute();

            var regionStatement = connection.prepareStatement("INSERT INTO Regions(name, region) VALUES (?, ?)");
            regionStatement.setObject(1, country.name);
            regionStatement.setObject(2, country.region);
            regionStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillDataBase(List<Country> countries) {
        try {
            var dbHandler = DatabaseHandler.getInstance();
            for (var country : countries) {
                dbHandler.addCountry(country);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<String> getAllCountryList() {
        var countries = new ArrayList<String>();
        try {
            var statement = connection.prepareStatement("""
                    SELECT DISTINCT Countries.name
                    FROM Countries
                    ORDER BY economy DESC
                    """);
            var countriesSet = statement.executeQuery();
            while (countriesSet.next()) {
                countries.add(countriesSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public String getNameWithHighestEconomy(String region1, String region2) {
        try {
            var statement = connection.prepareStatement("""
                    SELECT Countries.name
                    FROM Countries, Regions
                    WHERE (Regions.region = ? OR Regions.region = ?) AND Countries.name = Regions.name
                    ORDER BY economy DESC
                    LIMIT 1""");
            statement.setObject(1, region1);
            statement.setObject(2, region2);
            return statement.executeQuery().getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<String> getCountryListByRegions(String region1, String region2) {
        var countries = new ArrayList<String>();
        try {
            var statement = connection.prepareStatement("""
                    SELECT Countries.name
                    from Countries, Regions
                    where (Regions.region = ? or Regions.region = ?) and Countries.name = Regions.name
                    """);
            statement.setObject(1, region1);
            statement.setObject(2, region2);
            var countriesSet = statement.executeQuery();
            while (countriesSet.next()) {
                countries.add(countriesSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public String getCountryRegion(String name) {
        try {
            var statement = connection.prepareStatement("""
                    SELECT region FROM Regions
                    WHERE Regions.name = ?
                    """);
            statement.setObject(1, name);
            return statement.executeQuery().getString("region");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public double getCountryField(String name, String field) {
        try {
            var statement = connection.prepareStatement(String.format("SELECT \"%s\" FROM Countries WHERE Countries.name = ?", field));
            statement.setObject(1, name);
            return statement.executeQuery().getDouble(field);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0.0;
    }

    public double getFieldAverage(String field, String region1, String region2) {
        try {
            var statement = connection.prepareStatement(String.format("SELECT AVG(\"%s\"), region FROM Countries, Regions WHERE Regions.region = ? OR Regions.region = ?", field));
            statement.setObject(1, region1);
            statement.setObject(2, region2);
            return statement.executeQuery().getDouble(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0.0;
    }
}
