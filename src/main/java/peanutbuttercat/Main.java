package peanutbuttercat;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Creates and displays the JavaFX window for PeanutButterCat.
 */
public class Main extends Application {
    private final PeanutButterCat peanutButterCat = new PeanutButterCat();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane mainWindow = fxmlLoader.load();
            Scene scene = new Scene(mainWindow);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setPeanutButterCat(peanutButterCat);
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
