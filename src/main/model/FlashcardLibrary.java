package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlashcardLibrary {
    private List<FlashcardSet> library;
    private FlashcardSet current;

    public FlashcardLibrary() {
        library = new LinkedList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds new set to FlashcardLibrary if library isn't full and return true.
    //otherwise return false
    public boolean addSet(FlashcardSet set) {
        return false;
    }

    //MODIFIES: this
    //EFFECTS: if the given set exists, remove it from the library and return true.
    //otherwise return false
    public boolean removeSet(String set) {
        return false;
    }

    //EFFECTS: returns the list of the names of the sets in the library
    public List<String> viewLibrary() {
        return null;
    }

    //EFFECTS: returns name of sets that match the given category.
    public List<String> viewCategory(String c) {
        return new ArrayList<>();
    }

    //EFFECTS: returns set if the given set exists and set it as current. If set doesn't exist
    //return false
    public boolean getSet(String set) {
        return false;
    }
    //EFFECTS: returns the names of completed sets. Returns false if there are no completed sets
    public List<String> completedSets() {
        return new ArrayList<>();
    }

    //EFFECTS: returns the number of sets that are not completed
    public Integer needToComplete() {
        return 0;
    }

    //EFFECTS: returns number of sets in the library
    public int length() {
        return library.size();
    }

    //EFFECTS: return true if library is empty, false otherwise
    public boolean isEmpty() {
        return library.isEmpty();
    }

}
