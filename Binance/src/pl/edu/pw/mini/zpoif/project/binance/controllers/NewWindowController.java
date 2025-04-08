package pl.edu.pw.mini.zpoif.project.binance.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent.BarData;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent.BuildBars;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent.CandleStickChart;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class NewWindowController extends AuxiliaryController {
	@FXML
	private Pane plotPane;

	@FXML
	private ChoiceBox<String> intervalChoice;

	@FXML
	private Slider hourSlider;

	@FXML
	private Slider minuteSlider;

	@FXML
	private ChoiceBox<String> marketTypeChoice;

	@FXML
	private DatePicker datePicker;

	@FXML
	private CheckBox liveCheck;

	@FXML
	private Label chooseDate;

	@FXML
	private Button generatePlot;

	private ObservableList<String> intervalTypes = FXCollections.observableArrayList("1m", "15m", "30m", "1h", "1d",
			"1M");
	private CandleStickChart candleStickChart;
	private String currencySymbol;
	private static ScheduledExecutorService scheduler;
	private boolean isWindowOpen = false;

	public void setSymbol(String symbol) {
		this.currencySymbol = symbol;
	}

	@FXML
	private void closeWindow(ActionEvent event) {
		cleanup();
		Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		stage.close();
	}

	public void cleanup() {
		isWindowOpen = false;
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}

	public void setData(Currency currency) {
		this.currencySymbol = currency.getSymbol();
		setColumns();
		generatePlot.setOnAction(this::handleButtonClick);
	}

	private void handleButtonClick(ActionEvent event) {
		if (liveCheck.isSelected()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Plot error!");
			alert.setHeaderText(null);
			alert.setContentText("Cannot generate static plot while generating live plot!");
			alert.showAndWait();
		} else {
			long hour = (long) Math.floor(hourSlider.getValue()) - 1;
			long minute = (long) Math.floor(minuteSlider.getValue());
			long day = datePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
			long startdate = day + 3600000 * hour + 60000 * minute;
			updateChart(candleStickChart, intervalChoice.getValue(), currencySymbol, startdate, 0L);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isWindowOpen = true;
		liveCheck.setOnAction(event -> toggleDatePickerVisibility());

	}

	private void toggleDatePickerVisibility() {
		boolean isLiveChecked = liveCheck.isSelected();
		if (isLiveChecked && isWindowOpen) {
			scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(() -> {
				if (MainController.isInternetConnected()) {
					Platform.runLater(() -> {
						updateChart(candleStickChart, intervalChoice.getValue(), currencySymbol, 0L,
								System.currentTimeMillis());
					});
				} else {
					if (scheduler != null && !scheduler.isShutdown()) {
						scheduler.shutdown();
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Internet connection lost!");
							alert.setHeaderText(null);
							alert.setContentText("Stopped generating live data because of lost internet connection");
							alert.showAndWait();
						});
					}
				}
			}, 0, 2, TimeUnit.SECONDS);
		} else {
			if (scheduler != null && !scheduler.isShutdown()) {
				scheduler.shutdown();
			}
		}
	}

	protected void setColumns() {
		datePicker.setValue(LocalDate.now());

		candleStickChart = new CandleStickChart(currencySymbol, BuildBars.buildBars(currencySymbol, "15m", null,
				LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(), null, 30), 1000);
		plotPane.getChildren().add(candleStickChart);
		intervalChoice.setValue("15m");
		intervalChoice.setItems(intervalTypes);

		candleStickChart.setPrefSize(982, 558);

		hourSlider.setMin(0);
		hourSlider.setMax(23);
		hourSlider.setMajorTickUnit(1);
		hourSlider.setSnapToTicks(true);
		hourSlider.setShowTickMarks(true);
		hourSlider.setShowTickLabels(true);

		minuteSlider.setMin(0);
		minuteSlider.setMax(59);
		minuteSlider.setMajorTickUnit(1);
		minuteSlider.setSnapToTicks(true);
		minuteSlider.setShowTickMarks(true);
		minuteSlider.setShowTickLabels(true);

		Callback<DatePicker, DateCell> dayCellFactory = datePicker -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				// Disable dates before the selected start date
				setDisable(empty || item.isAfter(LocalDate.now()));
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);

	}

	@Override
	protected Queue<List<Currency>> dataDownloadingAndProcessing(DataTablesProcessor dataTablesProcessor) {
		return null;
	}

	private void updateChart(CandleStickChart chart, String interval, String symbolCourse, long startTime,
			long endTime) {
		if (startTime == 0) {
			try {
				chart.setData(BuildBars.buildBars(symbolCourse, interval, null, endTime, null, 30));
			} catch (Exception e) {
			}
		} else {
			try {
				List<BarData> listOfBars = BuildBars.buildBars(symbolCourse, interval, startTime, null, null, 30);
				if (listOfBars.size() < 30) {
					Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
					confirmationAlert.setTitle("Generate Plot Confirmation");
					confirmationAlert.setHeaderText(null);
					confirmationAlert.setContentText(
							"The plot has incomplete number of candles. It may look inappropriately. Do you want to generate the plot?");
					ButtonType buttonTypeYes = new ButtonType("Yes");
					ButtonType buttonTypeNo = new ButtonType("No");
					confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
					confirmationAlert.showAndWait().ifPresent(response -> {
						if (response == buttonTypeYes) {
							chart.setData(listOfBars);
						} else {
							// User clicked "No," do nothing or handle accordingly
						}
					});
				} else {
					chart.setData(listOfBars);
				}
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Plot error!");
				alert.setHeaderText(null);
				alert.setContentText("Cannot generate plot for those parameters! Try again.");
				alert.showAndWait();
			}
		}
	}
}
