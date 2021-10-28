package persistence;

import model.Flashcard;
import model.FlashcardLibrary;
import model.FlashcardSet;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// This class references code from this CPSC 210 GitHub repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public FlashcardLibrary read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFlashcardLibrary(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses library from JSON object and returns it
    private FlashcardLibrary parseFlashcardLibrary(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        FlashcardLibrary lib = new FlashcardLibrary();
        lib.setName(name);
        addLibrary(lib, jsonObject);
        return lib;
    }

    // MODIFIES: library
    // EFFECTS: parses sets from JSON object and adds them to library
    private void addLibrary(FlashcardLibrary lib, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("sets");
        for (Object json : jsonArray) {
            JSONObject nextSet = (JSONObject) json;
            addSet(lib, nextSet);
        }
    }

    // MODIFIES: library
    // EFFECTS: parses set from JSON object and adds it to library
    private void addSet(FlashcardLibrary lib, JSONObject jsonObject) {
        String name = jsonObject.getString("set name");
        String category = jsonObject.getString("category");
        Boolean completed = jsonObject.getBoolean("completed");
        JSONArray jsonArray = jsonObject.getJSONArray("cards");
        FlashcardSet set = new FlashcardSet(name);
        set.setCategory(category);
        set.setCompleted(completed);
        for (Object json : jsonArray) {
            JSONObject nextCard = (JSONObject) json;
            addCard(set, nextCard);
        }
        lib.addSet(set);
    }

    // MODIFIES: library, set(?)
    // EFFECTS: parses flashcards from JSON object and adds it to set within library
    private void addCard(FlashcardSet set, JSONObject jsonObject) {
        String front = jsonObject.getString("front");
        String back = jsonObject.getString("back");
        Boolean completed = jsonObject.getBoolean("completed");
        Flashcard card = new Flashcard(front, back);
        card.setCompleted(completed);
        set.addCard(card);
    }

}
