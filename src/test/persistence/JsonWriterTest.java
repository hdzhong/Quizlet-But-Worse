package persistence;

import model.Flashcard;
import model.FlashcardLibrary;
import model.FlashcardSet;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            FlashcardLibrary lib = new FlashcardLibrary();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLibrary() {
        try {
            FlashcardLibrary lib = new FlashcardLibrary();
            lib.setName("Douglas' Library");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyLibrary.json");
            lib = reader.read();
            assertEquals("Douglas' Library", lib.getName());
            assertEquals(0, lib.length());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralLibrary() {
        try {
            FlashcardLibrary lib = new FlashcardLibrary();
            lib.setName("Douglas' Library");
            FlashcardSet cpsc = new FlashcardSet("CPSC 121");
            cpsc.setCategory("CPSC");
            cpsc.addCard(new Flashcard("interface", "supertype"));
            FlashcardSet math = new FlashcardSet("MATH 221");
            math.addCard(new Flashcard("matrix", "algebra"));
            math.setCategory("MATH");
            lib.addSet(cpsc);
            lib.addSet(math);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralLibrary.json");
            lib = reader.read();
            assertEquals("Douglas' Library", lib.getName());
            List<String> sets = lib.viewLibrary();
            assertEquals(2, sets.size());
            checkSet("CPSC 121", "CPSC", lib.getSet("CPSC 121"));
            checkSet("MATH 221", "MATH", lib.getSet("MATH 221"));
            assertEquals("matrix", lib.getSet("MATH 221").getNextCard().getFront());
            assertEquals("supertype", lib.getSet("CPSC 121").getNextCard().getBack());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}