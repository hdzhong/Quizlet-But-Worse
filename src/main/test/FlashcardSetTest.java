package test;

import model.Flashcard;
import model.FlashcardSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardSetTest {
    public FlashcardSet set;
    public Integer SIZE = 10;

    @BeforeEach
    public void createSet() {
        set = new FlashcardSet();
    }

    @Test
    public void emptySet() {
        assertEquals(0, set.length());
        assertTrue(set.isEmpty());
    }
    @Test
    public void normalSetTest() {
        for (int i = 0; i < SIZE; i++) {
            set.addCard(new Flashcard(String.valueOf(i), String.valueOf(i)));
        }
        assertEquals(10, set.length());
        assertFalse(set.isEmpty());
        set.removeCard("0");

        assertEquals(9, set.length());
    }

    @Test
    public void completionTest() {
        for (int i = 0; i < SIZE; i++) {
            set.addCard(new Flashcard(String.valueOf(i), String.valueOf(i)));
        }
        assertFalse(set.isCompleted());

        for (Flashcard f: set) {
            f.markCompleted();
        }
        assertTrue(set.isCompleted());

    }
}
