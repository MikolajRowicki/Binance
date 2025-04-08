package pl.edu.pw.mini.zpoif.project.binance.controllers.services;

import java.util.List;
import java.util.Queue;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import pl.edu.pw.mini.zpoif.project.binance.controllers.AuxiliaryController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.FavouriteController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.HomePageController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.MainController;
import pl.edu.pw.mini.zpoif.project.binance.controllers.MarketsReviewController;
import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.DataTablesProcessor;

public class MainControllerUpdatingService {

	public static void initializeUpdateService(ScheduledService<List<Currency>> updateService,
			MainController mainController) {
		updateService = new ScheduledService<>() {
			@Override
			protected Task<List<Currency>> createTask() {
				return new Task<>() {
					@Override
					protected List<Currency> call() throws Exception {
						// Fetch and update data based on the selected market type
						DataTablesProcessor dataTablesProcessor = new DataTablesProcessor();
						Queue<List<Currency>> newHomePage = mainController.homepageTabController
								.dataDownloadingAndProcessing(dataTablesProcessor);
						Queue<List<Currency>> newMarketsReview = mainController.marketsreviewTabController
								.dataDownloadingAndProcessing(dataTablesProcessor);
						Queue<List<Currency>> newFavourite = mainController.favouriteTabController
								.dataDownloadingAndProcessing(dataTablesProcessor);

						// Update the lists through the bound properties
						Platform.runLater(() -> {
							mainController.homepageTabController.columnsUpdating(HomePageController.gainersListProperty,
									newHomePage.poll());
							mainController.homepageTabController.columnsUpdating(HomePageController.losersListProperty,
									newHomePage.poll());
							mainController.homepageTabController.columnsUpdating(HomePageController.volumesListProperty,
									newHomePage.poll());
							mainController.marketsreviewTabController.columnsUpdating(
									MarketsReviewController.overviewListProperty, newMarketsReview.poll());
							mainController.favouriteTabController
									.columnsUpdating(FavouriteController.favouriteListProperty, newFavourite.poll());
						});

						return null;
					}
				};
			}
		};

		AuxiliaryController.marketType.addListener((observable, oldValue, newValue) -> {
			TabControllersInitializatingService.initializeTabControllers(mainController);
		});

		// Set the period for the update service (every 10 seconds)
		updateService.setPeriod(Duration.seconds(10));

		// Start the update service
		updateService.start();
	}
}
