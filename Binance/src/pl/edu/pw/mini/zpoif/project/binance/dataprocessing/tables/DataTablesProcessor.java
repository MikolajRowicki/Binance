package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.services.CurrencyStatisticsService;
import pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.services.TopCurrencyService;

public class DataTablesProcessor {

	private final List<Currency> statistics;

	
	public List<Currency> getStatistics(String marketType) {
		return statistics.stream().filter(p -> p.getSymbol().endsWith(marketType)).collect(Collectors.toList());
	}

	public DataTablesProcessor() {
		statistics = CurrencyStatisticsService.getCurrencyStatistics();
	}

	public List<Currency> returnFavouriteCurrencies(Set<String> listOfSymbols, String marketType) {
		return statistics.stream().filter(p -> listOfSymbols.contains(p.getName()))
				.filter(p -> p.getSymbol().endsWith(marketType)).collect(Collectors.toList());

	}

	public List<Currency> returnBiggestVolumes(String marketType) {
		return TopCurrencyService.getTopCurrencies(statistics, Comparator.comparing(Currency::getQuoteVolume), 5,
				marketType, true);
	}

	public List<Currency> returnBiggestGainers(String marketType) {
		return TopCurrencyService.getTopCurrencies(statistics, Comparator.comparing(Currency::getPriceChangePercent), 5,
				marketType, true);
	}

	public List<Currency> returnBiggestLosers(String marketType) {
		return TopCurrencyService.getTopCurrencies(statistics, Comparator.comparing(Currency::getPriceChangePercent), 5,
				marketType, false);

	}

}