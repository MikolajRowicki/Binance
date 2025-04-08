package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;

public class TopCurrencyService {

	public static List<Currency> getTopCurrencies(List<Currency> statistics, Comparator<Currency> comparator, int limit,
			String marketType, boolean isComparingReversed) {
		List<Currency> currencyList = CurrencyFilterService.filterCurrenciesByMarketType(statistics, marketType);

		if (isComparingReversed) {
			comparator = comparator.reversed();
		}
		List<Currency> topCurrencies = new ArrayList<>();
		if (currencyList != null && !currencyList.isEmpty()) {
			// Process the list and find the top
			topCurrencies = currencyList.stream().sorted(comparator).limit(limit).collect(Collectors.toList());
		} else {
			System.out.println("No data for the specified market.");
		}
		return topCurrencies;
	}

}
