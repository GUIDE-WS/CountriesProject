import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Task {
    private final DatabaseHandler db;
    private final ArrayList<String> countries;

    public Task(DatabaseHandler db) {
        this.db = db;
        countries = db.getAllCountryList();
    }


    public void getCountriesEconomyBar() throws IOException {
        var lines = new ArrayList<String>();
        var data = new DefaultCategoryDataset();
        for (var e : countries) {
            var economy = db.getCountryField(e, "economy");
            data.addValue(economy, e, db.getCountryRegion(e));
            lines.add(String.format("Страна: %s     показатель экономики: %s", e, economy));
        }
        var file = Paths.get("countries.txt");
        Files.write(file, lines, StandardCharsets.UTF_8);
        var chart = ChartFactory.createBarChart3D(
                "Показатели экономики стран",
                "Страна",
                "Показатель экономики",
                data,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        chart.setBackgroundPaint(Color.white);
        chart.getTitle().setPaint(Color.black);
        var plot = chart.getCategoryPlot();
        var bar = (BarRenderer) plot.getRenderer();
        bar.setItemMargin(0.25);
        var domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        var frame = new JFrame("Таблица показателей стран 2015");
        var chartPanel = new ChartPanel(chart);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void printHighEconomyCountry() {
        var country = db.getNameWithHighestEconomy("Latin America and Caribbean",
                "Eastern Asia");
        System.out.printf("#2 Страна с самым высоким показателем экономики среди \"Latin America and Caribbean\" и \"Eastern Asia\": %s\n", country);
    }

    public void printAverageEconomyCountry() {
        var countries = db.getCountryListByRegions("Western Europe", "North America");
        var fields = new String[]{"happinessScore", "standardError", "economy", "family", "health", "freedom", "trust", "generosity", "dystopiaResidual"};
        var referenceAverages = new double[fields.length];
        for (var i = 0; i < fields.length; i++) {
            referenceAverages[i] = db.getFieldAverage(fields[i], "Western Europe", "North America");
        }
        var minDeltas = Arrays.stream(new double[fields.length]).map(x -> Double.MAX_VALUE).toArray();
        var maxMinDeltaCount = 0;
        var averageCountry = "";
        for (var country : countries) {
            var curCount = 0;
            var curDeltas = new double[fields.length];
            for (var i = 0; i < fields.length; i++) {
                var field = db.getCountryField(country, fields[i]);
                curDeltas[i] = Math.abs(referenceAverages[i] - field);
                if (curDeltas[i] < minDeltas[i]) {
                    curCount++;
                }
            }
            if (curCount > maxMinDeltaCount) {
                minDeltas = Arrays.copyOf(curDeltas, curDeltas.length);
                maxMinDeltaCount = curCount;
                averageCountry = country;
            }
        }
        System.out.printf("#3 Страна с \"самыми средними показателями\" среди \"Western Europe\" и \"North America\": %s\n", averageCountry);
    }
}
