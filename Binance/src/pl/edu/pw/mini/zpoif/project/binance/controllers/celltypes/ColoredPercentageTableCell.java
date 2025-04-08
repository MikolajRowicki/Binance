package pl.edu.pw.mini.zpoif.project.binance.controllers.celltypes;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColoredPercentageTableCell<Currency> extends TableCell<Currency, Double> {

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			setTextFill(Color.BLACK); // Reset text color to default for empty cells
		} else {
			Text text = new Text(String.format("%.6f%%", item));
			text.setFill(item < 0 ? Color.RED : Color.YELLOWGREEN);
			setGraphic(text);
		}
	}

	// Factory method to create a Callback for the cell factory
	public static <Currency> Callback<TableColumn<Currency, Double>, TableCell<Currency, Double>> forTableColumn() {
		return param -> new ColoredPercentageTableCell<>();
	}
}
