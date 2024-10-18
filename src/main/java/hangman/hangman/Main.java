// 2a APC ITL12 - Eibiswald
// Tristan Birnstingl

package hangman.hangman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hangman Game");

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style/Style.css")).toExternalForm());


        stage.setMinHeight(500);
        stage.setMinWidth(603);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}