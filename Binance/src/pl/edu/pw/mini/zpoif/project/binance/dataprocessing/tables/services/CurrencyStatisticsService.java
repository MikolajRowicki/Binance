package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.tables.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.edu.pw.mini.zpoif.project.binance.currency.Currency;

public class CurrencyStatisticsService {

	public static List<Currency> getCurrencyStatistics() {
		List<Currency> currencyList = new ArrayList<>();
		try {
			Map<String, Object> parameters = new LinkedHashMap<>();
			SpotClient client = new SpotClientImpl();
			String result = client.createMarket().ticker24H(parameters);
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, String>> dataList = objectMapper.readValue(result,
					new TypeReference<List<Map<String, String>>>() {
					});

			for (Map<String, String> item : dataList) {
				String symbol = item.get("symbol");
				String name = "";
				if (symbol.endsWith("USDT")) {
					name = symbol.substring(0, symbol.length() - 4);
				} else if (symbol.endsWith("BNB") || symbol.endsWith("BTC") || symbol.endsWith("ETH")) {
					name = symbol.substring(0, symbol.length() - 3);
				}
				double priceChangePercent = Double.parseDouble(item.get("priceChangePercent"));
				double lastPrice = Double.parseDouble(item.get("lastPrice"));
				double quoteVolume = Double.parseDouble(item.get("quoteVolume"));

				currencyList.add(new Currency(symbol, name, priceChangePercent, lastPrice, quoteVolume));
			}

		} catch (Exception e) {

		}

		return currencyList;
	}
}
