package persistence;

import model.FlashcardLibrary;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            FlashcardLibrary lib = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyLibrary() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLibrary.json");
        try {
            FlashcardLibrary lib = reader.read();
            assertEquals("Douglas' Library", lib.getName());
            assertEquals(0, lib.length());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralLibrary.json");
        try {
            FlashcardLibrary lib = reader.read();
            assertEquals("Douglas' Library", lib.getName());
            List<String> libNames = lib.viewLibrary();
            assertEquals(2, libNames.size());
            checkSet("CPSC 121", "CPSC", lib.getSet("CPSC 121"));
            checkSet("MATH 221", "MATH", lib.getSet("MATH 221"));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}