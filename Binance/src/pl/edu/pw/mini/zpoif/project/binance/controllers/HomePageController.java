package pl.edu.pw.mini.zpoif.project.binance.controllers;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.BasicTextCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.ColoredPercentageTableCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.MillionFormattedTableCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.NoScientificNotationTableCell;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class HomePageController extends AuxiliaryController {

	@FXML
	protected TableView<Currency> biggestGainers;

	@FXML
	protected TableView<Currency> biggestLosers;

	@FXML
	protected TableView<Currency> biggestVolumes;

	@FXML
	protected TableColumn<Currency, String> symbolColumn1;

	@FXML
	protected TableColumn<Currency, Double> lastPriceColumn1;

	@FXML
	protected TableColumn<Currency, Double> priceChangePercentColumn1;

	@FXML
	protected TableColumn<Currency, String> symbolColumn2;

	@FXML
	protected TableColumn<Currency, Double> lastPriceColumn2;

	@FXML
	protected TableColumn<Currency, Double> priceChangePercentColumn2;

	@FXML
	protected TableColumn<Currency, String> symbolColumn3;

	@FXML
	protected TableColumn<Currency, Double> lastPriceColumn3;

	@FXML
	protected TableColumn<Currency, Double> volumeColumn3;

	protected static ObservableList<Currency> biggestGainersList = FXCollections.observableArrayList();
	protected static ObservableList<Currency> biggestLosersList = FXCollections.observableArrayList();
	protected static ObservableList<Currency> biggestVolumesList = FXCollections.observableArrayList();

	@FXML
	protected ChoiceBox<String> marketTypeChoice;

	public static ObjectProperty<ObservableList<Currency>> gainersListProperty = new SimpleObjectProperty<>(
			FXCollections.observableArrayList());
	public static ObjectProperty<ObservableList<Currency>> losersListProperty = new SimpleObjectProperty<>(
			FXCollections.observableArrayList());
	public static ObjectProperty<ObservableList<Currency>> volumesListProperty = new SimpleObjectProperty<>(
			FXCollections.observableArrayList());

	@Override
	protected void setColumns() {
		biggestGainers.setItems(biggestGainersList);
		biggestLosers.setItems(biggestLosersList);
		biggestVolumes.setItems(biggestVolumesList);

		symbolColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));
		symbolColumn1.setCellFactory(BasicTextCell.forTableColumn());
		lastPriceColumn1.setCellValueFactory(new PropertyValueFactory<>("lastPrice"));
		lastPriceColumn1.setCellFactory(NoScientificNotationTableCell.forTableColumn());
		priceChangePercentColumn1.setCellValueFactory(new PropertyValueFactory<>("priceChangePercent"));
		priceChangePercentColumn1.setCellFactory(ColoredPercentageTableCell.forTableColumn());

		symbolColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
		symbolColumn2.setCellFactory(BasicTextCell.forTableColumn());
		lastPriceColumn2.setCellValueFactory(new PropertyValueFactory<>("lastPrice"));
		lastPriceColumn2.setCellFactory(NoScientificNotationTableCell.forTableColumn());
		priceChangePercentColumn2.setCellValueFactory(new PropertyValueFactory<>("priceChangePercent"));
		priceChangePercentColumn2.setCellFactory(ColoredPercentageTableCell.forTableColumn());

		symbolColumn3.setCellValueFactory(new PropertyValueFactory<>("name"));
		symbolColumn3.setCellFactory(BasicTextCell.forTableColumn());
		lastPriceColumn3.setCellValueFactory(new PropertyValueFactory<>("lastPrice"));
		lastPriceColumn3.setCellFactory(NoScientificNotationTableCell.forTableColumn());
		volumeColumn3.setCellValueFactory(new PropertyValueFactory<>("quoteVolume"));
		volumeColumn3.setCellFactory(MillionFormattedTableCell.forTableColumn());

		biggestGainers.itemsProperty().bindBidirectional(gainersListProperty);
		biggestLosers.itemsProperty().bindBidirectional(losersListProperty);
		biggestVolumes.itemsProperty().bindBidirectional(volumesListProperty);

	}

	protected void setMarketTypeChoiceBox() {
		marketTypeChoice.setValue("USDT");
		marketTypeChoice.setItems(marketTypes);
		// Bind marketTypeChoice value to marketType property
		marketTypeChoice.valueProperty().bindBidirectional(marketType);
	}

	@Override
	public Queue<List<Currency>> dataDownloadingAndProcessing(DataTablesProcessor dataTablesProcessor) {
		List<Currency> newGainers = dataTablesProcessor.returnBiggestGainers(marketType.get());
		List<Currency> newLosers = dataTablesProcessor.returnBiggestLosers(marketType.get());
		List<Currency> newVolumes = dataTablesProcessor.returnBiggestVolumes(marketType.get());
		Queue<List<Currency>> result = new ArrayDeque<List<Currency>>();
		result.offer(newGainers);
		result.offer(newLosers);
		result.offer(newVolumes);
		return result;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumns();
		setMarketTypeChoiceBox();

	}

}
