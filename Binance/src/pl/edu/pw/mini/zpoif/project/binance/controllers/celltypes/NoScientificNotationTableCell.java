package pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class NoScientificNotationTableCell<Currency> extends TableCell<Currency, Double> {

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
		} else {
			setText(String.format("%.6f", item.doubleValue())); // Formatuje liczbę z dwoma miejscami po przecinku
			setTextFill(Color.WHITE);
		}
	}

	// Metoda fabryczna, aby utworzyć Callback dla fabryki komórek
	public static <Currency> Callback<TableColumn<Currency, Double>, TableCell<Currency, Double>> forTableColumn() {
		return param -> new NoScientificNotationTableCell<>();
	}
}
