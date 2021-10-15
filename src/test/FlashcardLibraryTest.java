import model.FlashcardLibrary;
import model.FlashcardSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardLibraryTest {
    private FlashcardLibrary lib;
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
        assertEquals(10, lib.length());
        assertFalse(lib.isEmpty());
        assertEquals("0", lib.getSet("0").getSetName());
        lib.removeSet("0");

        assertEquals(9, lib.length());
        assertEquals("null", lib.getSet("0").getSetName());

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
        lib.getSet("0").forceComplete();
        lib.getSet("1").forceComplete();

        assertEquals(2, lib.completedSets().size());
        assertEquals(8, lib.needToComplete().size());
    }
}

