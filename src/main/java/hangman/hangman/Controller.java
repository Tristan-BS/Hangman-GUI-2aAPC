package hangman.hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class Controller {

    @FXML
    private ImageView I_Viewport;

    @FXML
    private TextField I_Letters;

    @FXML
    private Label L_HiddenWord;

    @FXML
    private Label L_UsedLetterList;


    Hangman HM = new Hangman();

    public boolean ReturnValue;

    private int Wrong_Guesses = 0;
    private String Word_Guessed;
    private String Word;

    // Function executes at Program start
    @FXML
    public void initialize() {
        Renew_Image();
        Wrong_Guesses += 1;
        InitializeNewWord();
        UpdateHiddenWordLabel();

        I_Letters.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    ExecuteGuess();
                    break;
                default:
                    break;
            }
        });
    }

    @FXML
    protected void B_Enter_IsPressed(ActionEvent event) {
        ExecuteGuess();
    }

    @FXML
    protected void On_BSettings_Pressed() {
        AlertMessage.showMessage("E", "Settings", "Settings are not available at the moment.");
    }

    private void ExecuteGuess() {
        if (I_Letters.getText().isEmpty()) {
            AlertMessage.showMessage("E", "Error - Invalid Input","Please enter a letter or word.");
            return;
        }

        ReturnValue = HM.Guess_Taken(I_Letters.getText());
        Word_Guessed = I_Letters.getText();

        addUsedLetterImage(Word_Guessed);

        System.out.println(ReturnValue);
        Renew_Image();
        InitializeMain(ReturnValue);

        I_Letters.clear();

        UpdateHiddenWordLabel();
    }

    // Functions

    private void addUsedLetterImage(String letter) {
        // Get Char from HM.getUsed_Chars() and set it to L_UsedLetterList Text
        String UsedChars = HM.getUsed_Chars().toString();
        // Remove [] from String
        UsedChars = UsedChars.substring(1, UsedChars.length() - 1);

        // Create TextFlow
        TextFlow textFlow = new TextFlow();
        textFlow.setLineSpacing(10);

        // Create Text
        Text text = new Text(UsedChars);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        textFlow.getChildren().add(text);

        // Set TextFlow to L_UsedLetterList
        L_UsedLetterList.setGraphic(textFlow);


    }

    private void Renew_Image() {
        Wrong_Guesses = HM.getWrong_Attempts();
        String HangmanPath ="/Images/Hangman Images/Hangman_0" + Wrong_Guesses + ".png";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(HangmanPath)));
        I_Viewport.setImage(image);
    }

    private void InitializeMain(boolean ReturnValue) {
        if (ReturnValue) {
            showPopupWindow();

            ResetForm();
            Renew_Image();
            I_Letters.clear();
            L_UsedLetterList.setGraphic(null);

        } else {
            String Used_C = HM.getUsed_Chars().toString();
            String Used_W = HM.getUsed_Words().toString();
            Wrong_Guesses = HM.getWrong_Attempts();
            int Attempts = HM.getAttempts();

            if (!HM.LastAttempt) {

                System.out.println("Incorrect!" +
                        "\nWrong Word: " + Word_Guessed +
                        "\nWrong Attempts: " + Wrong_Guesses +
                        "\nTotal Attempts: " + Attempts);

            }

            if (!Used_C.isEmpty()) {
                System.out.println("Used Characters: " + Used_C);
            }
            if (!Used_W.isEmpty()) {
                System.out.println("Used Words: " + Used_W);
            }

            if (Wrong_Guesses == 10) {
                AlertMessage.showMessage("E", "You Lost!", "You've run out of attempts! The word was: " + HM.get_Target_Word());
            }
        }
    }

    private void ResetForm() {
        Wrong_Guesses = 0;
        Word_Guessed = "";
        Word = "";
        HM.ResetValues();
        InitializeNewWord();
    }

    private void InitializeNewWord() {
        Word = HM.get_Target_Word();
        HM.get_Hidden_Word();
        System.out.println(Word);
    }

    public void AcceptPressed() {
        ResetForm();
    }


    // Show Pop-Up Window
    private void showPopupWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hangman/hangman/PopupWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Popup Window");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();

            PopupWindowController controller = fxmlLoader.getController();
            controller.setWord(HM.getWord());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdateHiddenWordLabel() {
        String HWord = HM.GetHiddenWord();
        // a space between each _
        HWord = HWord.replaceAll("", " ").trim();

        L_HiddenWord.setText("Hidden Word: " + HWord);

    }
}

