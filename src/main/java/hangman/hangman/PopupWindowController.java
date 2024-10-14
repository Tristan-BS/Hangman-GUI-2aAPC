package hangman.hangman;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class PopupWindowController {

    @FXML
    private Button B_Accept;

    @FXML
    Label L_Subheading;
    private String WD;

    @FXML
    private void On_BAccept_Pressed() {
        System.out.println("Accept button pressed");

        Controller controller = new Controller();
        controller.AcceptPressed();

        // Close
        Stage stage = (Stage) B_Accept.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void On_BExit_Pressed() {
        System.out.println("Exit button pressed");
        System.exit(0);
    }

    public void setWord(String word) {
        WD = word;
        if (L_Subheading != null) {
            L_Subheading.setText("The word was: " + WD);
        }
    }
}