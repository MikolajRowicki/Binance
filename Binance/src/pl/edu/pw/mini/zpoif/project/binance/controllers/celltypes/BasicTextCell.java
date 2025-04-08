package pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class BasicTextCell<Currency> extends TableCell<Currency, String> {

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			setText(item.toString());
			setTextFill(Color.WHITE); // Ustawienie koloru tekstu na czarny
		}
	}

	public static <Currency> Callback<TableColumn<Currency, String>, TableCell<Currency, String>> forTableColumn() {
		return param -> new BasicTextCell<>();
	}
}
