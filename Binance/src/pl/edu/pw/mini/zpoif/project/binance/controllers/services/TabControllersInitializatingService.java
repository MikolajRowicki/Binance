package pl.edu.pw.mini.zpoif.project.binance.controllers.services;

import java.util.List;

import javafx.collections.FXCollections;
import pl.edu.pw.mini.zpoif.project.binance.controllers.AuxiliaryController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.FavouriteController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.HomePageController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.MainController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.MarketsReviewController;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;

public class TabControllersInitializatingService {

	public static void initializeTabControllers(MainController mainController) {
		List<Currency> newGainers = MainController.getDatatablesprocessor()
				.returnBiggestGainers(AuxiliaryController.marketType.get());
		List<Currency> newLosers = MainController.getDatatablesprocessor()
				.returnBiggestLosers(AuxiliaryController.marketType.get());
		List<Currency> newVolumes = MainController.getDatatablesprocessor()
				.returnBiggestVolumes(AuxiliaryController.marketType.get());
		List<Currency> newOverview = MainController.getDatatablesprocessor()
				.getStatistics(AuxiliaryController.marketType.get());
		List<Currency> newFavourite = MainController.getDatatablesprocessor()
				.returnFavouriteCurrencies(AuxiliaryController.favouriteSymbols, AuxiliaryController.marketType.get());

		// Update the lists through the bound properties
		HomePageController.gainersListProperty.set(FXCollections.observableArrayList(newGainers));
		HomePageController.losersListProperty.set(FXCollections.observableArrayList(newLosers));
		HomePageController.volumesListProperty.set(FXCollections.observableArrayList(newVolumes));
		MarketsReviewController.overviewListProperty.set(FXCollections.observableArrayList(newOverview));
		FavouriteController.favouriteListProperty.set(FXCollections.observableArrayList(newFavourite));
	}

}
