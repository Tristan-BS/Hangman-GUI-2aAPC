module hangman.hangman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens hangman.hangman to javafx.fxml;
    exports hangman.hangman;
}