package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;

public class BuildBars {
	public static List<BarData> buildBars(String symbol, String interval, Long startTime, Long endTime, String timeZone,
			int limit) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		SpotClient client = new SpotClientImpl();
		if (symbol != null) {
			parameters.put("symbol", symbol);
		}
		if (interval != null) {
			parameters.put("interval", interval);
		}
		if (startTime != null) {
			parameters.put("startTime", startTime);
		}
		if (endTime != null) {
			parameters.put("endTime", endTime);
		}
		if (timeZone != null) {
			parameters.put("timeZone", timeZone);
		}
		if (limit != 0) {
			parameters.put("limit", limit);
		}
		parameters.put("limit", limit);
		try {
			String requestResult = client.createMarket().uiKlines(parameters);
			List<BarData> listOfBars = buildBarsAsInStockChartsFX(requestResult);
			return listOfBars;
		} catch (Exception e) {
		}
		return null;

	}

	private static List<BarData> buildBarsAsInStockChartsFX(String requestResult) {
		requestResult = requestResult.replace("[[", "[").replace("]]", "]");
		String[] split = requestResult.split("],");
		List<String> list = Arrays.asList(split);
		List<List<String>> finalList = new ArrayList<>();
		for (String s : list) {
			String sWithCommas = s.replace("[", "").replace("]", "").replaceAll("\"", "");
			String[] splittedStrings = sWithCommas.split(",");
			List<String> oneCandleList = Arrays.asList(splittedStrings);
			finalList.add(oneCandleList);
		}
		List<BarData> barList = new ArrayList<>();
		for (List<String> listOfStrings : finalList) {
			long openTimeL = Long.parseLong(listOfStrings.get(0));
			GregorianCalendar openTime = new GregorianCalendar();
			openTime.setTimeInMillis(openTimeL);
			double openPrice = Double.parseDouble(listOfStrings.get(1));
			double highPrice = Double.parseDouble(listOfStrings.get(2));
			double lowPrice = Double.parseDouble(listOfStrings.get(3));
			double closePrice = Double.parseDouble(listOfStrings.get(4));
			double volume = Double.parseDouble(listOfStrings.get(5));
			long closeTimeL = Long.parseLong(listOfStrings.get(6));
			GregorianCalendar closeTime = new GregorianCalendar();
			closeTime.setTimeInMillis(closeTimeL);
			barList.add(new BarData(openTime, openPrice, highPrice, lowPrice, closePrice, volume));
		}
		return barList;
	}

}
