package pl.edu.pw.mini.zpoif.project.binance.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("binance.fxml"));
		primaryStage.setTitle("Binance API");
		Scene scene = new Scene(root, 1500, 750);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
