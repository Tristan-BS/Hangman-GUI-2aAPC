// 2a APC ITL12 - Eibiswald
// Tristan Birnstingl

package hangman.hangman;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class AlertMessage {
    public static void showMessage(String Type, String Title, String Content) {
        Alert alert;
        if (Type.equalsIgnoreCase("E")) {
            alert = new Alert(AlertType.ERROR);
        } else if(Type.equalsIgnoreCase("W")) {
            alert = new Alert(AlertType.WARNING);
        } else if(Type.equalsIgnoreCase("I")) {
            alert = new Alert(AlertType.INFORMATION);
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
        }

        alert.setTitle(Title);
        alert.setHeaderText(null);

        Text text = new Text(Content);
        text.setTextAlignment(TextAlignment.CENTER);

        // To Center
        VBox vbox = new VBox(text);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        // Create custom buttons
        Button continueButton = new Button("Continue");
        Button exitButton = new Button("Exit");

        continueButton.setOnAction(e -> alert.setResult(ButtonType.OK));
        exitButton.setOnAction(e -> {
            alert.setResult(ButtonType.CANCEL);
            System.exit(0);
        });

        HBox buttonBox = new HBox(continueButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        vbox.getChildren().add(buttonBox);


        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().clear();

        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(AlertMessage.class.getResource("/Style/Style.css")).toExternalForm()
        );

        alert.showAndWait();
    }
}