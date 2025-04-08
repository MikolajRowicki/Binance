package pl.edu.pw.mini.zpoif.project.binance.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartanalysiscontent.ProcessTrendAnalysisChart;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class TrendAnalysisController extends AuxiliaryController {

	@FXML
	private LineChart<String, Number> lineChart;
	@FXML
	private Button generatePlotButton;
	@FXML
	private NumberAxis yAxis;

	@FXML
	private DatePicker startDatePicker;

	@FXML
	private DatePicker endDatePicker;

	@FXML
	private CheckComboBox<String> cryptoCheckComboBox;

	private ObservableList<String> cryptoOptions;

	private static ObservableList<String> cryptoOptionsUSDT = getCryptoOptionsForMarketType("USDT");
	private static ObservableList<String> cryptoOptionsBNB = getCryptoOptionsForMarketType("BNB");
	private static ObservableList<String> cryptoOptionsBTC = getCryptoOptionsForMarketType("BTC");
	private static ObservableList<String> cryptoOptionsETH = getCryptoOptionsForMarketType("ETH");

	protected static ObservableList<Currency> overviewList = FXCollections.observableArrayList();

	public static ObjectProperty<ObservableList<Currency>> trendCryptoListProperty = new SimpleObjectProperty<>(
			FXCollections.observableArrayList());

	@FXML
	private Button oneMonthBeforeButton;

	@FXML
	private Button threeMonthsBeforeButton;

	@FXML
	private Button sixMonthsBeforeButton;

	@FXML
	private Button twelveMonthsBeforeButton;

	// Add these methods to your existing controller class
	@FXML
	private void handleOneMonthBefore(ActionEvent event) {
		adjustStartDate(1); // Adjust start date 1 month before end date
	}

	@FXML
	private void handleThreeMonthsBefore(ActionEvent event) {
		adjustStartDate(3); // Adjust start date 3 months before end date
	}

	@FXML
	private void handleSixMonthsBefore(ActionEvent event) {
		adjustStartDate(6); // Adjust start date 6 months before end date
	}

	@FXML
	private void handleTwelveMonthsBefore(ActionEvent event) {
		adjustStartDate(12); // Adjust start date 12 months before end date
	}

	private void adjustStartDate(int monthsBefore) {
		// Retrieve the current end date
		LocalDate endDate = endDatePicker.getValue();

		// Calculate the new start date
		LocalDate startDate = endDate.minusMonths(monthsBefore);

		// Set the new start date
		startDatePicker.setValue(startDate);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setupCryptoCheckComboBox();
		generatePlotButton.setText("Generate Plot");
		generatePlotButton.setOnAction(this::handleButtonClick);
		setColumns();
		setupHoverInfo();
		setAxisStyle(lineChart.getXAxis());
		setAxisStyle(yAxis);
	}

	private void setupCryptoCheckComboBox() {
		// Initialize the cryptocurrency options
		updateCryptoOptions(); // Initial update

		// Add a listener to update the plot when the market type changes
		marketType.addListener((observable, oldValue, newValue) -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Market Type Changed");
			alert.setHeaderText(null);
			alert.setContentText("The market type has been changed.");
			alert.showAndWait();
			updateCryptoOptions();
		});
	}

	private void handleButtonClick(ActionEvent event) {
		// Regenerate or update the plot
		updatePlot();
	}

	private static ObservableList<String> getCryptoOptionsForMarketType(String marketType) {
		return FXCollections.observableList(new DataTablesProcessor().getStatistics(marketType).stream()
				.map(Currency::getName).collect(Collectors.toList()));
	}

	private void updateCryptoOptions() {
		cryptoCheckComboBox.getCheckModel().clearChecks();
		cryptoCheckComboBox.getItems().clear();
		cryptoOptions = marketType.get().equals("USDT") ? cryptoOptionsUSDT
				: (marketType.get().equals("BNB") ? cryptoOptionsBNB
						: (marketType.get().equals("BTC") ? cryptoOptionsBTC : cryptoOptionsETH));
		cryptoCheckComboBox.getItems().setAll(cryptoOptions);
	}

	private void updatePlot() {
		// Clear existing data from the chart

		// Get the selected cryptocurrencies
		ObservableList<String> selectedCryptos = cryptoCheckComboBox.getCheckModel().getCheckedItems();
		ObservableSet<String> selectedCryptosSet = FXCollections
				.observableSet(selectedCryptos.stream().distinct().collect(Collectors.toSet()));

		if (selectedCryptos.size() > 4) {
			Alert alert3 = new Alert(AlertType.INFORMATION);
			alert3.setTitle("Too much crypto!");
			alert3.setHeaderText(null);
			alert3.setContentText(
					"Cannot plot more than 4 cryptos! Please unselect " + (selectedCryptos.size() - 4 == 1 ? "1 crypto"
							: String.valueOf(selectedCryptos.size() - 4) + " cryptos") + " and try again!");
			alert3.showAndWait();
		} else {
			// Get new data for the selected cryptocurrencies and update the chart
			lineChart.getData().clear();
			long startDate = startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long endDate = endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			List<String> correctCryptos = new ArrayList<>();
			List<String> notCorrectCryptos = new ArrayList<>();

			for (String selectedCrypto : selectedCryptosSet) {
				String fullSymbol = selectedCrypto + marketType.get();
				XYChart.Series<String, Number> series = ProcessTrendAnalysisChart
						.getCurrencyDataAsXYChartSeries(fullSymbol, startDate, endDate);
				if (series.getData().isEmpty()) {
					notCorrectCryptos.add(selectedCrypto);
				} else {
					correctCryptos.add(selectedCrypto);

					lineChart.getData().add(series);
				}

			}
			if (correctCryptos.isEmpty()) {
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Wrong crypto data selection");
				alert2.setHeaderText(null);
				alert2.setContentText("You haven't selected any cryptos that pass all criteria. Try again");
				alert2.showAndWait();
			} else {
				if (!notCorrectCryptos.isEmpty()) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Wrong crypto data selection");
					alert.setHeaderText(null);
					alert.setContentText("Selected cryptos: "
							+ notCorrectCryptos.toString().substring(1, notCorrectCryptos.toString().length() - 1)
							+ " doesn't match the criteria. Plotting data only for correct cryptos: "
							+ correctCryptos.toString().substring(1, correctCryptos.toString().length() - 1));
					alert.showAndWait();
				}
				// Set up x-axis and y-axis
				CategoryAxis xAxis = new CategoryAxis();
				xAxis.setLabel("Date");
				yAxis.setLabel("Value");
				yAxis.setScaleShape(true);

				// Update hover info for the new data
				setupHoverInfo();
			}
		}

	}

	private void setAxisStyle(Axis<?> axis) {
		axis.setTickLabelFill(Color.WHITE);
		axis.setStyle("-fx-tick-mark-stroke: white;");
		axis.lookup(".axis-label").setStyle("-fx-text-fill: white;");
	}

	@Override
	protected void setColumns() {
		long startDate = LocalDate.now().minusDays(20).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
		long endDate = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
		XYChart.Series<String, Number> series = ProcessTrendAnalysisChart.getCurrencyDataAsXYChartSeries("LTCUSDT",
				startDate, endDate);
		XYChart.Series<String, Number> secondSeries = ProcessTrendAnalysisChart
				.getCurrencyDataAsXYChartSeries("BNBUSDT", startDate, endDate);
		startDatePicker.setValue(LocalDate.now().minusDays(20));
		endDatePicker.setValue(LocalDate.now());
		Callback<DatePicker, DateCell> dayCellFactory = datePicker -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				// Disable dates before the selected start date
				setDisable(empty || item.isBefore(startDatePicker.getValue()) || item.isAfter(LocalDate.now()));
			}
		};
		Callback<DatePicker, DateCell> dayCellFactory2 = datePicker -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				// Disable dates before the selected start date
				setDisable(empty || item.isAfter(endDatePicker.getValue()) || item.isAfter(LocalDate.now()));
			}
		};
		startDatePicker.setDayCellFactory(dayCellFactory2);
		// Apply the callback to the end date picker
		endDatePicker.setDayCellFactory(dayCellFactory);
		cryptoCheckComboBox.getCheckModel().check("LTC");
		cryptoCheckComboBox.getCheckModel().check("BNB");
		// Set up x-axis and y-axis
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Value");
		yAxis.setScaleShape(true);

		// Add the series and axis to the scatter chart
		lineChart.getData().add(series);
		lineChart.getData().add(secondSeries);
		lineChart.setAnimated(false); // Optional: Disable animation if needed
		lineChart.getXAxis().setTickLabelsVisible(true);
	}

	private void setupHoverInfo() {
		for (XYChart.Series<String, Number> series : lineChart.getData()) {
			for (XYChart.Data<String, Number> data : series.getData()) {
				double currentValue = data.getYValue().doubleValue();
				double previousValue = getPreviousDayValue(series, data.getXValue());

				// Calculate the price change percentage
				double priceChangePercentage;

				if (previousValue == 0) {
					priceChangePercentage = 0;
				} else {
					priceChangePercentage = ((currentValue - previousValue) / previousValue) * 100.0;
				}
				// Determine the color based on the price change
				String colorStyle = priceChangePercentage >= 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;";
				if (priceChangePercentage == 0) {
					colorStyle = "-fx-text-fill: white;";
				}

				Tooltip tooltip = new Tooltip("Date: " + data.getXValue() + "\n" + "Value: " + currentValue + "\n"
						+ "Price Change: " + String.format("%.2f%%", priceChangePercentage));

				// Apply the color style to the tooltip
				tooltip.setStyle(colorStyle);

				Tooltip.install(data.getNode(), tooltip);
			}
		}
	}

	private double getPreviousDayValue(XYChart.Series<String, Number> series, String currentXValue) {
		for (XYChart.Data<String, Number> data : series.getData()) {
			if (data.getXValue().equals(currentXValue)) {
				int currentIndex = series.getData().indexOf(data);
				if (currentIndex > 0) {
					XYChart.Data<String, Number> previousData = series.getData().get(currentIndex - 1);
					return previousData.getYValue().doubleValue();
				}
			}
		}
		return 0.0; // Return a default value if the previous day's data is not found
	}

	@Override
	public Queue<List<Currency>> dataDownloadingAndProcessing(DataTablesProcessor dataTablesProcessor) {
		List<Currency> newOverview = dataTablesProcessor.getStatistics(marketType.get());
		Queue<List<Currency>> result = new ArrayDeque<List<Currency>>();
		result.offer(newOverview);

		return result;
	}

}
