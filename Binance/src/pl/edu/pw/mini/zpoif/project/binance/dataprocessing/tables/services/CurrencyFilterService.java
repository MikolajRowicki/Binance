package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.services;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;

public class CurrencyFilterService {

	public static List<Currency> filterCurrenciesByMarketType(List<Currency> statistics, String marketType) {
		List<Currency> currencies = new ArrayList<>();

		for (Currency item : statistics) {
			String symbol = item.getSymbol();

			// Check if the symbol ends with marketType
			if (symbol != null && symbol.endsWith(marketType)) {
				currencies.add(item);
			}
		}

		return currencies;
	}

}
