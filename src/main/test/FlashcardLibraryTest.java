package test;

import model.FlashcardLibrary;
import model.FlashcardSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardLibraryTest {
    public FlashcardLibrary lib;
    public Integer SIZE = 10;

    @BeforeEach

    public void createLibrary() {
        lib = new FlashcardLibrary();
    }

    @Test

    public void emptyLibraryTest() {
        assertEquals(0,lib.length());
        assertTrue(lib.isEmpty());
    }

    @Test

    public void normalLibraryTest() {
        for (int i = 0; i < SIZE; i++) {
            lib.addSet(new FlashcardSet(String.valueOf(i)));
        }
        assertEquals(10, lib.length());
        assertEquals("0", lib.getSet("0").getSetName());
        lib.removeSet("0");

        assertEquals(9, lib.length());
        assertFalse(lib.getSet("0"));
    }

}
