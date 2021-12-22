import org.apache.commons.lang3.tuple.Pair;
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

public class Task {
    private final DatabaseHandler db;

    public Task(DatabaseHandler db) {
        this.db = db;
    }


    public void getCountriesEconomyBar() throws IOException {
        var countries = db.getAllCountryList();
        var lines = new ArrayList<String>();
        var data = new DefaultCategoryDataset();
        for (var e : countries) {
            data.addValue(e.getRight(), e.getLeft(), e.getMiddle());
            lines.add(String.format("Страна: %s     показатель экономики: %s", e.getLeft(), e.getRight()));
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
        var countries = db.getNameEconomyCountryList("Western Europe", "North America");
        var maxEconomyCountry = countries.get(0);
        var minEconomyCountry = countries.get(countries.size() - 1);
        var averageEconomyCountry = getNameAverageCountry(maxEconomyCountry, minEconomyCountry, countries);
        System.out.printf("#3 Страна с \"самыми средними показателями\" среди \"Western Europe\" и \"North America\": %s\n", averageEconomyCountry);
    }

    private String getNameAverageCountry(Pair<String, Double> max, Pair<String, Double> min, ArrayList<Pair<String, Double>> countries) {

        var approximateAverage = Pair.of("", Double.MAX_VALUE);
        var average = (max.getRight() + min.getRight()) / 2;
        var minDelta = Double.MAX_VALUE;
        var left = 0;
        var right = countries.size() - 1;
        while (left <= right) {
            var i = (left + right) / 2;
            if (Math.abs(countries.get(i).getRight() - average) < minDelta){
                minDelta = Math.abs(countries.get(i).getRight() - average);
                approximateAverage = Pair.of(countries.get(i).getLeft(), countries.get(i).getRight());
            }
            if (i + i/2 < countries.size() && Math.abs(countries.get(i + i/2).getRight() - average) < minDelta){
                left = i + i/2;
                minDelta = Math.abs(countries.get(i + i/2).getRight() - average);
                approximateAverage = Pair.of(countries.get(i + i/2).getLeft(), countries.get(i + i/2).getRight());
                continue;
            }
            else if (i - i/2 >= 0 && Math.abs(countries.get(i - i/2).getRight() - average) < minDelta){
                right = i - i/2;
                minDelta = Math.abs(countries.get(i - i/2).getRight() - average);
                approximateAverage = Pair.of(countries.get(i - i/2).getLeft(), countries.get(i - i/2).getRight());
                continue;
            }
            break;
        }
        return approximateAverage.getLeft();
    }
}
