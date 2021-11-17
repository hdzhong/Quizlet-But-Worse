import model.Flashcard;
import model.FlashcardLibrary;
import model.FlashcardSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardLibraryTest {
    public FlashcardLibrary lib;
    private Integer size = 10;

    @BeforeEach
    public void createLibrary() {
        lib = new FlashcardLibrary();
    }

    @Test
    public void emptyLibraryTest() {
        assertEquals(0, lib.length());
        assertTrue(lib.isEmpty());
    }

    @Test
    public void normalLibraryTest() {
        for (int i = 0; i < size; i++) {
            lib.addSet(new FlashcardSet(String.valueOf(i)));
        }
        lib.setName("test");
        assertEquals("test", lib.getName());
        assertEquals(10, lib.length());
        assertFalse(lib.isEmpty());
        assertEquals("0", lib.getSet("0").getSetName());
        lib.removeSet("0");

        assertEquals(9, lib.length());
        assertEquals(null, lib.getSet("0"));

        assertEquals(9, lib.viewLibrary().size());
        assertFalse(lib.removeSet("0"));
    }

    @Test
    public void categoryTest() {
        for (int i = 0; i < size; i++) {
            lib.addSet(new FlashcardSet(String.valueOf(i)));
        }
        lib.getSet("0").setCategory("Science");
        lib.getSet("1").setCategory("Science");
        lib.getSet("2").setCategory("Math");

        assertEquals(2, lib.viewCategory("Science").size());
        assertEquals(1, lib.viewCategory("Math").size());
    }

    @Test
    public void completedTest() {
        for (int i = 0; i < size; i++) {
            lib.addSet(new FlashcardSet(String.valueOf(i)));
        }
        lib.getSet("0").setCompleted(true);
        lib.getSet("1").setCompleted(true);

        assertEquals(2, lib.completedSets().size());
        assertEquals(8, lib.needToComplete().size());
    }


    @Test
    public void allCompleteTest() {
        for (int i = 0; i < size; i++) {
            lib.addSet(new FlashcardSet(String.valueOf(i)));
        }
        for (FlashcardSet set: lib.getLibrary()) {
            set.setCompleted(true);
        }

        assertEquals(10, lib.completedSets().size());
        assertEquals(0, lib.needToComplete().size());
    }

    @Test
    public void noCompleteTest() {
        for (int i = 0; i < size; i++) {
            lib.addSet(new FlashcardSet(String.valueOf(i)));
        }
        assertEquals(0, lib.completedSets().size());
        assertEquals(10, lib.needToComplete().size());
    }
}


