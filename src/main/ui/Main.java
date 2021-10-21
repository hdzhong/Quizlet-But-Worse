package ui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new FlashcardApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run: file not found");
        }
    }
}
