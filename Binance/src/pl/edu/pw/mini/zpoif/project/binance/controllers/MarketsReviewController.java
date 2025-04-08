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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.BasicTextCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.ColoredPercentageTableCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.MillionFormattedTableCell;
import pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes.NoScientificNotationTableCell;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class MarketsReviewController extends AuxiliaryController {

	@FXML
	private TableView<Currency> overview;

	@FXML
	private TableColumn<Currency, String> nameColumn4;

	@FXML
	private TableColumn<Currency, Double> lastPriceColumn4;

	@FXML
	private TableColumn<Currency, Double> priceChangePercentColumn4;

	@FXML
	private TableColumn<Currency, Double> quoteVolumeColumn4;

	@FXML
	private TableColumn<Currency, String> favouriteColumn4;

	@FXML
	private TableColumn<Currency, String> plotColumn4;

	protected static ObservableList<Currency> overviewList = FXCollections.observableArrayList();

	public static ObjectProperty<ObservableList<Currency>> overviewListProperty = new SimpleObjectProperty<>(
			FXCollections.observableArrayList());

	private void addToFavorites(Currency currency) {
		// Tutaj dodaj currency do listy ulubionych

		if (!favouriteSymbols.contains(currency.getName())) {
			favouriteSymbols.add(currency.getName());
			FavouriteController.favouriteListProperty.get().add(currency);
			// Znajdź indeks wiersza odpowiadającego dodanej walucie
			int index = overviewList.indexOf(currency);

			// Aktualizuj komórkę w kolumnie "Usuń z ulubionych"
			if (index != -1) {
				TableView.TableViewSelectionModel<Currency> selectionModel = overview.getSelectionModel();
				selectionModel.select(index);
				selectionModel.clearSelection();
			}
		}

	}

	@Override
	protected void setColumns() {
		overview.setItems(overviewList);

		nameColumn4.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn4.setCellFactory(BasicTextCell.forTableColumn());
		favouriteColumn4.setCellValueFactory(new PropertyValueFactory<>("emptyField"));
		favouriteColumn4.setCellFactory(col -> {
			TableCell<Currency, String> cell = new TableCell<>() {
				final Button addButton = new Button("Dodaj do ulubionych");

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setGraphic(null);
						setText(null);
						setTextFill(Color.BLACK);
					} else {
						StackPane stackPane = new StackPane(addButton);
						stackPane.setAlignment(Pos.CENTER);

						setGraphic(stackPane);
						setText(item);
						setTextFill(Color.BLACK);

						// Dodaj obsługę zdarzenia po kliknięciu przycisku
						addButton.setOnAction(event -> {
							Currency currency = getTableView().getItems().get(getIndex());
							addToFavorites(currency);
						});
					}
				}
			};

			return cell;
		});
		lastPriceColumn4.setCellValueFactory(new PropertyValueFactory<>("lastPrice"));
		lastPriceColumn4.setCellFactory(NoScientificNotationTableCell.forTableColumn());
		quoteVolumeColumn4.setCellValueFactory(new PropertyValueFactory<>("quoteVolume"));
		quoteVolumeColumn4.setCellFactory(MillionFormattedTableCell.forTableColumn());
		priceChangePercentColumn4.setCellValueFactory(new PropertyValueFactory<>("priceChangePercent"));
		priceChangePercentColumn4.setCellFactory(ColoredPercentageTableCell.forTableColumn());

		plotColumn4.setCellValueFactory(param -> new SimpleObjectProperty<>(null));
		plotColumn4.setCellFactory(col -> new TableCell<>() {
			final Button openWindowButton = new Button("Otwórz");

			{
				StackPane stackPane = new StackPane(openWindowButton);
				stackPane.setAlignment(Pos.CENTER);

				openWindowButton.setOnAction(event -> {
					Currency currency = getTableView().getItems().get(getIndex());
					// Call a method to open a new window, passing the currency as a parameter
					openNewWindow(currency);

				});

				setGraphic(stackPane);
				setTextFill(Color.BLACK);
			}
		});
		overview.itemsProperty().bindBidirectional(overviewListProperty);
	}

	@Override
	public Queue<List<Currency>> dataDownloadingAndProcessing(DataTablesProcessor dataTablesProcessor) {
		List<Currency> newOverview = dataTablesProcessor.getStatistics(marketType.get());
		Queue<List<Currency>> result = new ArrayDeque<List<Currency>>();
		result.offer(newOverview);

		return result;
	}

	private void openNewWindow(Currency currency) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/pl/edu/pw/mini/zpoif/project/binance/main/newwindow.fxml"));
			Parent root = loader.load();

			// Access the controller of the loaded FXML file
			NewWindowController newWindowController = loader.getController();
			// Pass any necessary data to the new window controller

			newWindowController.setData(currency);

			// Create a new stage for the new window
			Stage newStage = new Stage();
			newStage.setScene(new Scene(root));

			// Set additional properties if needed, e.g., title
			newStage.setTitle("Chart window");

			// Show the new window
			newStage.show();
			newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					newWindowController.cleanup();
				}
			});
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Cannot open a window!");
			alert.setHeaderText(null);
			alert.setContentText("Cannot open the candle stick chart. No internet connection! ");
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumns();
	}

}
