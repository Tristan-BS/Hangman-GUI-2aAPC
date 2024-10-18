// 2a APC ITL12 - Eibiswald
// Tristan Birnstingl

package hangman.hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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

    @FXML
    private ComboBox<String> CB_Difficulty;

    @FXML
    private CheckMenuItem CB_ShowUW;

    @FXML
    private Button B_Enter;

    @FXML
    private MenuButton MB_MenuButton;


    Hangman HM = new Hangman();

    public boolean ReturnValue;

    private int Wrong_Guesses = 0;
    protected int Max_Wrong_Guesses = 10;
    private String Word_Guessed;
    private String Word;
    private int DifficultyMode;

    // Function executes at Program start
    @FXML
    public void initialize() {
        Renew_Image();
        Wrong_Guesses += 1;
        InitializeNewWord();
        UpdateHiddenWordLabel();

        // Set ComboBox Items
        CB_Difficulty.getItems().addAll("Easy", "Medium", "Hard");
        CB_Difficulty.setValue("Easy");

        // Combobox Listener
        CB_Difficulty.valueProperty().addListener((observable, OldValue, NewValue) -> {
            AlertMessage.showMessage("I", "Difficulty Changed", "Difficulty changed to: " + NewValue);
            // if difficulty is set to hard:
            if(NewValue.equalsIgnoreCase("Hard")) {
                Max_Wrong_Guesses = 5;
            }
            CB_Difficulty_OnAction(NewValue);
        });

        // CheckMenuItem CB_ShowUW Listener
        CB_ShowUW.selectedProperty().addListener((observable, oldValue, Value) -> {
            if (L_UsedLetterList.isVisible()) {
                L_UsedLetterList.setVisible(false);
                AlertMessage.showMessage("I", "Hide Used Words", "Used Words are now hidden.");
            } else {
                L_UsedLetterList.setVisible(true);
                AlertMessage.showMessage("I", "Show Used Words", "Used Words are now visible.");
            }
        });

        I_Letters.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    ExecuteGuess();
                    break;
                default:
                    break;
            }
        });

        // Tooltips
        I_Letters.setTooltip(new Tooltip("Enter a letter or word."));
        CB_Difficulty.setTooltip(new Tooltip("Change the difficulty."));
        B_Enter.setTooltip(new Tooltip("Enter your guess."));
        MB_MenuButton.setTooltip(new Tooltip("Open the Settings"));
    }

    @FXML
    protected void B_Enter_IsPressed(ActionEvent event) {
        ExecuteGuess();
    }

    @FXML
    protected void MI_ChangeTries_Pressed(ActionEvent event) {
        // Create a new Stage
        Stage inputStage = new Stage();
        inputStage.setTitle("Change maximum tries.");

        TextField inputField = new TextField();
        inputField.setPromptText("Enter number of tries");

        VBox vbox = getvBox(inputField, inputStage);

        Scene scene = new Scene(vbox, 300, 100);
        inputStage.setScene(scene);
        inputStage.show();
    }

    private VBox getvBox(TextField inputField, Stage inputStage) {
        String Difficulty = CB_Difficulty.getValue();
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            try {
                int newMaxTries = Integer.parseInt(inputField.getText());
                if (newMaxTries < Wrong_Guesses) {
                    AlertMessage.showMessage("E", "Invalid Input", "New maximum tries cannot be less than current wrong guesses.");
                } else if(Difficulty.equalsIgnoreCase("Hard") || newMaxTries < 5) {
                    AlertMessage.showMessage("E", "Invalid Input", "With difficulty set to Hard, maximum tries is 5.");
                } else {
                    Max_Wrong_Guesses = newMaxTries;
                    Renew_Image();
                    inputStage.close();
                }
            } catch (NumberFormatException ex) {
                AlertMessage.showMessage("E", "Invalid Input", "Please enter a valid number.");
            }
        });

        VBox vbox = new VBox(10, inputField, confirmButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    @FXML
    protected void CB_Difficulty_OnAction(String newValue) {
        String Difficulty = CB_Difficulty.getValue();
        if (!Objects.equals(Difficulty, newValue)) {
            Difficulty = newValue;
        }
        switch (Difficulty) {
            case "Easy":
                DifficultyMode = 0;
                break;
            case "Medium":
                DifficultyMode = 1;
                break;
            case "Hard":
                DifficultyMode = 2;
                break;
            case "Impossible":
                DifficultyMode = 3;
                break;
            case "Custom":
                DifficultyMode = 4;
                break;
            default:
                break;
        }
        ResetForm();
        Renew_Image();
        I_Letters.clear();
        L_UsedLetterList.setGraphic(null);
        InitializeNewWord();
        UpdateHiddenWordLabel();
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
        if (Wrong_Guesses <= Max_Wrong_Guesses) {
            int imageIndex;
            if (Wrong_Guesses == Max_Wrong_Guesses) {
                imageIndex = 9; // Show last Image
            } else {
                imageIndex = (int) Math.floor((double) Wrong_Guesses / Max_Wrong_Guesses * 9);
            }
            String HangmanPath = "/Images/Hangman Images/Hangman_0" + imageIndex + ".png";
            try {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(HangmanPath)));
                I_Viewport.setImage(image);
            } catch (NullPointerException e) {
                System.err.println("Image not found: " + HangmanPath);
                AlertMessage.showMessage("E", "Image Error", "Could not load image: " + HangmanPath);
            }
        }
    }

    private void InitializeMain(boolean ReturnValue) {
        if (ReturnValue) {
            AlertMessage.showMessage("I", "You Won!", "Congratulations! \nYou've guessed the word: " + HM.getWord() + "   ");
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

            if (Wrong_Guesses >= Max_Wrong_Guesses) {
                AlertMessage.showMessage("E", "You Lost!", "You've run out of attempts! \nThe word was: " + HM.getWord());
                ResetForm();
                Renew_Image();
                I_Letters.clear();
                L_UsedLetterList.setGraphic(null);
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
        Word = HM.get_Target_Word(DifficultyMode);
        HM.get_Hidden_Word();
        System.out.println(Word);
    }

    public void UpdateHiddenWordLabel() {
        String HWord = HM.GetHiddenWord();
        // a space between each _
        HWord = HWord.replaceAll("", " ").trim();

        L_HiddenWord.setText("Hidden Word: " + HWord);

    }
}

