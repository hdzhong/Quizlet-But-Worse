package test;

import model.Flashcard;
import org.junit.jupiter.api.BeforeEach;

public class FlashcardTest {
    private Flashcard card;

    @BeforeEach
    public void createCard() {
        card = new Flashcard("", "");
    }



}
