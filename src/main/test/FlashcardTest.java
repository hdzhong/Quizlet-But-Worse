package test;

import model.Flashcard;
import org.junit.jupiter.api.BeforeEach;

public class FlashcardTest {
    public Flashcard card;

    @BeforeEach
    public void CreateCard() {
        card = new Flashcard("", "");
    }



}
