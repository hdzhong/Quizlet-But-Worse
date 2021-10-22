import model.Flashcard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardTest {
    private Flashcard card;

    @BeforeEach
    public void createCard() {
        card = new Flashcard("", "");
    }

    @Test
    public void notCompleteTest() {
        assertFalse(card.isCompleted());
    }

    @Test
    public void completeTest() {
        card.markCompleted();
        assertTrue(card.isCompleted());
        assertTrue(card.markCompleted());
    }

    @Test
    public void setterTests() {
        card.setFront("Hello");
        card.setBack("World");
        assertEquals("Hello", card.getFront());
        assertEquals("World", card.getBack());
    }

    @Test
    public void sideTest() {
        assertTrue(card.getSide());
        card.changeSide();
        assertFalse(card.getSide());
        card.changeSide();
        assertTrue(card.getSide());
    }


}
