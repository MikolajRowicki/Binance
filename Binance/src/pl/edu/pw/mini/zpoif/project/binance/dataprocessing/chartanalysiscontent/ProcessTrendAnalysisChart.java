package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartanalysiscontent;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.scene.chart.XYChart;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent.BarData;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent.BuildBars;

public class ProcessTrendAnalysisChart {

	public static XYChart.Series<String, Number> getCurrencyDataAsXYChartSeries(String symbol, long startDate,
			long endDate) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName(symbol);
		List<BarData> listOfBars = BuildBars.buildBars(symbol, "1d", startDate, endDate, null, 1000);
		if (listOfBars != null) {
			for (BarData bar : listOfBars) {
				series.getData().add(
						new XYChart.Data<>(parseGregorianCalendar(bar.getDateTime()), Double.valueOf(bar.getClose())));
			}
			series.getData().sort(Comparator.comparing(XYChart.Data::getXValue));
		}
		return series;
	}

	private static String parseGregorianCalendar(GregorianCalendar consideredDate) {
		String day = consideredDate.get(Calendar.DAY_OF_MONTH) < 10
				? "0" + Integer.toString(consideredDate.get(Calendar.DAY_OF_MONTH))
				: Integer.toString(consideredDate.get(Calendar.DAY_OF_MONTH));
		String month = 1 + consideredDate.get(Calendar.MONTH) < 10
				? "0" + Integer.toString(1 + consideredDate.get(Calendar.MONTH))
				: Integer.toString(1 + consideredDate.get(Calendar.MONTH));
		String year = Integer.toString(consideredDate.get(Calendar.YEAR));
		return year + "-" + month + "-" + day;
	}

}
