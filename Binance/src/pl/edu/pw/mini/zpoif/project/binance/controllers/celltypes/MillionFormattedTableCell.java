package pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class MillionFormattedTableCell<Currency> extends TableCell<Currency, Double> {

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
		} else {
			double valueInMillions = item / 1_000_000.0;
			setText(String.format("%.6f M", valueInMillions));
			setTextFill(Color.WHITE);
		}
	}

	// Factory method to create a Callback for the cell factory
	public static <Currency> Callback<TableColumn<Currency, Double>, TableCell<Currency, Double>> forTableColumn() {
		return param -> new MillionFormattedTableCell<>();
	}
}
