import model.Flashcard;
import model.FlashcardSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardSetTest {
    private FlashcardSet set;
    private int size = 10;

    @BeforeEach
    public void createSet() {
        set = new FlashcardSet("");
    }

    @Test
    public void emptySet() {
        assertEquals(0, set.length());
        assertTrue(set.isEmpty());
        assertEquals(null, set.getCard("bob"));
        assertEquals(null, set.getNextCard());
    }

    @Test
    public void normalSetTest() {
        for (int i = 0; i < size; i++) {
            set.addCard(new Flashcard(String.valueOf(i), String.valueOf(i)));
        }
        set.setSetName("bob");
        assertEquals("bob", set.getSetName());
        assertEquals(10, set.length());
        assertFalse(set.isEmpty());
        set.removeCard("0");

        assertEquals(9, set.length());
        assertEquals("1", set.getCurrent().getFront());
        assertEquals("2",set.getNextCard().getFront());
        assertEquals(9, set.length());
        assertEquals("1", set.getPrevCard().getFront());
    }

    @Test
    public void completionTest() {
        for (int i = 0; i < 3; i++) {
            set.addCard(new Flashcard(String.valueOf(i), String.valueOf(i)));
        }
        assertFalse(set.markCompleted());
        set.getCard("0").markCompleted();
        set.getCard("1").markCompleted();
        set.getCard("2").markCompleted();
        assertTrue(set.markCompleted());
    }

}
