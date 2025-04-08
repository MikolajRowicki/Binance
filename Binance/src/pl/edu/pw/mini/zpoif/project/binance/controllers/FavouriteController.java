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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.BasicTextCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.ColoredPercentageTableCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.MillionFormattedTableCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.NoScientificNotationTableCell;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class FavouriteController extends AuxiliaryController {

	@FXML
	private TableView<Currency> favourite;

	@FXML
	private TableColumn<Currency, String> favouriteNameColumn;

	@FXML
	private TableColumn<Currency, Double> favouriteLastPriceColumn;

	@FXML
	private TableColumn<Currency, Double> favouritePriceChangePercentColumn;

	@FXML
	private TableColumn<Currency, Double> favouriteQuoteVolumeColumn;

	@FXML
	private TableColumn<Currency, String> deleteFavouriteColumn;

	protected static ObservableList<Currency> favouriteList = FXCollections.observableArrayList();

	public static ObjectProperty<ObservableList<Currency>> favouriteListProperty = new SimpleObjectProperty<>(
			FXCollections.observableArrayList());

	@Override
	protected void setColumns() {
		favourite.setItems(favouriteList);

		favouriteNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		favouriteNameColumn.setCellFactory(BasicTextCell.forTableColumn());
		favouriteLastPriceColumn.setCellValueFactory(new PropertyValueFactory<>("lastPrice"));
		favouriteLastPriceColumn.setCellFactory(NoScientificNotationTableCell.forTableColumn());
		favouriteQuoteVolumeColumn.setCellValueFactory(new PropertyValueFactory<>("quoteVolume"));
		favouriteQuoteVolumeColumn.setCellFactory(MillionFormattedTableCell.forTableColumn());
		favouritePriceChangePercentColumn.setCellValueFactory(new PropertyValueFactory<>("priceChangePercent"));
		favouritePriceChangePercentColumn.setCellFactory(ColoredPercentageTableCell.forTableColumn());

		deleteFavouriteColumn.setCellValueFactory(new PropertyValueFactory<>("emptyField"));

		deleteFavouriteColumn.setCellFactory(col -> {
			TableCell<Currency, String> cell = new TableCell<>() {
				final Button deleteButton = new Button("Usuń z ulubionych");

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setGraphic(null);
						setText(null);
					} else {
						StackPane stackPane = new StackPane(deleteButton);
						stackPane.setAlignment(Pos.CENTER);

						setGraphic(stackPane);
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

						// Dodaj obsługę zdarzenia po kliknięciu przycisku
						deleteButton.setOnAction(event -> {
							Currency currency = getTableView().getItems().get(getIndex());
							getTableView().getItems().remove(getIndex());
							favouriteSymbols.remove(currency.getName());
						});
					}
				}
			};

			return cell;
		});

		favourite.itemsProperty().bindBidirectional(favouriteListProperty);

	}

	@Override
	public Queue<List<Currency>> dataDownloadingAndProcessing(DataTablesProcessor dataTablesProcessor) {
		List<Currency> newFavourite = dataTablesProcessor.returnFavouriteCurrencies(favouriteSymbols, marketType.get());
		Queue<List<Currency>> result = new ArrayDeque<List<Currency>>();
		result.offer(newFavourite);

		return result;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumns();

	}

}
