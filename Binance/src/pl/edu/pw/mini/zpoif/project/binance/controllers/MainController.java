package pl.edu.pw.mini.zpoif.project.binance.controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

import com.binance.connector.client.exceptions.BinanceConnectorException;

import javafx.concurrent.ScheduledService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pl.edu.pw.mini.zpoif.project.binance.controllers.services.MainControllerUpdatingService;
import pl.edu.pw.mini.zpoif.project.binance.controllers.services.TabControllersInitializatingService;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class MainController implements Initializable {

	@FXML
	public FavouriteController favouriteTabController;

	@FXML
	public HomePageController homepageTabController;

	@FXML
	public MarketsReviewController marketsreviewTabController;

	@FXML
	public TrendAnalysisController trendAnalysisController;

	public ScheduledService<List<Currency>> updateService;

	private static final DataTablesProcessor dataTablesProcessor = new DataTablesProcessor();

	public static boolean isInternetConnected() {
		try {
			boolean isReachable = InetAddress.getByName("8.8.8.8").isReachable(1000);
			return isReachable;
		} catch (UnknownHostException e) {
			return false;
		} catch (BinanceConnectorException e) {
			return false;
		} catch(IOException e) {
			return false;
		}catch(ExceptionInInitializerError e) {
			return false;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TabControllersInitializatingService.initializeTabControllers(this);
		MainControllerUpdatingService.initializeUpdateService(updateService, this);
		
	}

	public static DataTablesProcessor getDatatablesprocessor() {
		return dataTablesProcessor;
	}

}
