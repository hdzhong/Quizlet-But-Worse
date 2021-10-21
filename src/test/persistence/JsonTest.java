package persistence;


import model.FlashcardSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkSet(String name, String category, FlashcardSet set) {
        assertEquals(name, set.getSetName());
        assertEquals(category, set.getCategory());
    }
}
