package hangman.hangman;

import javafx.scene.control.Alert.AlertType;

public class AlertMessage {
    public static void showMessage(String Type, String Title, String Content) {
        javafx.scene.control.Alert alert;
        if (Type.equalsIgnoreCase("E")) {
           alert = new javafx.scene.control.Alert(AlertType.ERROR);
        } else if(Type.equalsIgnoreCase("W")) {
            alert = new javafx.scene.control.Alert(AlertType.WARNING);
        } else if(Type.equalsIgnoreCase("I")) {
            alert = new javafx.scene.control.Alert(AlertType.INFORMATION);
        } else {
            alert = new javafx.scene.control.Alert(AlertType.CONFIRMATION);
        }

        alert.setTitle(Title);
        alert.setHeaderText(null);
        alert.setContentText(Content);

        alert.showAndWait();
    }
}
