package pl.edu.pw.mini.zpoif.project.binance.dataprocessing.chartcontent;

import java.text.DecimalFormat;
import java.text.ParseException;

import javafx.util.StringConverter;

public class DecimalAxisFormatter extends StringConverter<Number> {
	protected DecimalFormat decimalFormat;

	public DecimalAxisFormatter(String format) {
		decimalFormat = new DecimalFormat(format);
	}

	public DecimalAxisFormatter(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	@Override
	public String toString(Number object) {
		return decimalFormat.format(object.doubleValue());
	}

	@Override
	public Number fromString(String string) {
		try {
			return decimalFormat.parse(string);
		} catch (ParseException ex) {
			throw new IllegalStateException(ex);
		}
	}
}