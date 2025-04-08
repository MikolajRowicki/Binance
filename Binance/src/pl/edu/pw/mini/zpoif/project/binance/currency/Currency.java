package pl.edu.pw.mini.zpoif.project.binance.currency;

import java.util.Objects;

public class Currency {

	private static final String emptyField = "";
	private String symbol;
	private String name;
	private double priceChangePercent;
	private double lastPrice;
	private double quoteVolume;

	public Currency(String symbol, String name, double priceChangePercent, double lastPrice, double quoteVolume) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.priceChangePercent = priceChangePercent;
		this.lastPrice = lastPrice;
		this.quoteVolume = quoteVolume;
	}

	public String getEmptyField() {
		return emptyField;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public double getPriceChangePercent() {
		return priceChangePercent;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public double getQuoteVolume() {
		return quoteVolume;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		return Objects.equals(name, other.name);
	}

}
