package pl.edu.pw.mini.zpoif.project.binance.controllers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public abstract class AuxiliaryController implements Initializable {

	protected ObservableList<String> marketTypes = FXCollections.observableArrayList("USDT", "BNB", "BTC", "ETH");

	public static Set<String> favouriteSymbols = new LinkedHashSet<String>();

	@SuppressWarnings("exports")
	public static StringProperty marketType = new SimpleStringProperty("USDT");

	protected MainController mainController;

	protected abstract void setColumns();

	protected abstract Queue<List<Currency>> dataDownloadingAndProcessing(DataTablesProcessor dataTablesProcessor);

	public void columnsUpdating(ObjectProperty<ObservableList<Currency>> property, List<Currency> newColumn) {
		property.get().clear();
		property.get().addAll(newColumn);
	};

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
}
