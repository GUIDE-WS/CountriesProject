import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static ArrayList<Country> parseCSV() throws IOException {
        var countries = new ArrayList<Country>();
        var isFirstRow = true;
        List<String[]> list = null;
        try {
            var reader = new CSVReader(new FileReader("Показатель счастья по странам 2015.csv"));
            list = reader.readAll();
        } catch (CsvException e) {
            e.printStackTrace();
        }
        for (var countryInfo : list) {
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }
            countries.add(new Country(countryInfo[0], countryInfo[1], Integer.parseInt(countryInfo[2]),
                    Double.parseDouble(countryInfo[3]), Double.parseDouble(countryInfo[4]),
                    Double.parseDouble(countryInfo[5]), Double.parseDouble(countryInfo[6]),
                    Double.parseDouble(countryInfo[7]), Double.parseDouble(countryInfo[8]),
                    Double.parseDouble(countryInfo[9]), Double.parseDouble(countryInfo[10]),
                    Double.parseDouble(countryInfo[11])));

        }
        return countries;
    }
}
