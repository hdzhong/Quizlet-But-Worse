package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// A collection of flashcard sets. We can add and remove sets of flashcards.
public class FlashcardLibrary implements Writable {
    private String name;
    private List<FlashcardSet> library;

    public FlashcardLibrary() {
        library = new LinkedList<>();
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }

    //getter
    public String getName() {
        return name;
    }

    //getter
    public List<FlashcardSet> getLibrary() {
        return library;
    }

    //MODIFIES: this
    //EFFECTS: adds new set to FlashcardLibrary
    public void addSet(FlashcardSet set) {
        library.add(set);
    }

    //MODIFIES: this
    //EFFECTS: if the given set exists, remove it from the library.
    //otherwise return false
    public boolean removeSet(String set) {
        LinkedList<FlashcardSet> remove = new LinkedList<>();
        for (FlashcardSet s : library) {
            if (s.getSetName().equals(set)) {
                remove.add(s);
            }
        }
        return library.removeAll(remove);
    }

    //EFFECTS: returns the list of the names of the sets in the library
    public List<String> viewLibrary() {
        List<String> names = new ArrayList<>();
        for (FlashcardSet s : library) {
            names.add(s.getSetName());
        }
        return names;
    }

    //EFFECTS: returns name of sets that match the given category.
    public List<String> viewCategory(String c) {
        List<String> names = new ArrayList<>();
        for (FlashcardSet s : this.library) {
            if (s.getCategory().equals(c)) {
                names.add(s.getSetName());
            }
        }
        return names;
    }

    //EFFECTS: returns set if the given set exists and set it as current. If set doesn't exist
    //return new set called "null"
    public FlashcardSet getSet(String set) {
        for (FlashcardSet s : library) {
            if (s.getSetName().equals(set)) {
                return s;
            }
        }
        return new FlashcardSet("null");
    }

    //EFFECTS: returns the names of completed sets. Returns false if there are no completed sets
    public List<String> completedSets() {
        List<String> names = new ArrayList<>();
        for (FlashcardSet s : library) {
            if (s.isCompleted()) {
                names.add(s.getSetName());
            } else {
                continue;
            }
        }
        return names;
    }

    //EFFECTS: returns the name of the sets that need to be completed as a list.
    // False if all sets are completed
    public List<String> needToComplete() {
        List<String> names = new ArrayList<>();
        for (FlashcardSet s : library) {
            if (!s.isCompleted()) {
                names.add(s.getSetName());
            } else {
                continue;
            }
        }
        return names;
    }


    //EFFECTS: returns number of sets in the library
    public int length() {
        return library.size();
    }

    //EFFECTS: return true if library is empty, false otherwise
    public boolean isEmpty() {
        return library.isEmpty();
    }

    // These methods reference code from this CPSC 210 GitHub repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("sets", setsToJson());
        return json;
    }

    // EFFECTS: returns sets in this library as a JSON array
    private JSONArray setsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (FlashcardSet set : library) {
            jsonArray.put(set.toJson());
        }

        return jsonArray;
    }

}
