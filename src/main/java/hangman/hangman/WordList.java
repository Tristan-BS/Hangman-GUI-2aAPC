// 2a APC ITL12 - Eibiswald
// Tristan Birnstingl

package hangman.hangman;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class WordList {

    private static final String EASY_WORDS_PATH = "/Words/EASY_WORDS.txt";
    private static final String MEDIUM_WORDS_PATH = "/Words/MEDIUM_WORDS.txt";
    private static final String HARD_WORDS_PATH = "/Words/HARD_WORDS.txt";

    private static List<String> loadWords(String path) {
        try {
            return Files.lines(Paths.get(Objects.requireNonNull(WordList.class.getResource(path)).toURI()))
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error loading words from file: " + path, e);
        }
    }

    public static String getRandomWord(int difficulty) {
        List<String> words;
        switch (difficulty) {
            case 0: // Easy
                words = loadWords(EASY_WORDS_PATH);
                break;
            case 1: // Medium
                words = loadWords(MEDIUM_WORDS_PATH);
                break;
            case 2: // Hard
                words = loadWords(HARD_WORDS_PATH);
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficulty);
        }
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }
}