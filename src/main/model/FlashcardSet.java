package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlashcardSet {
    private List<Flashcard> flashcards;
    private String category;
    private boolean completed;
    private String setName;

    public FlashcardSet(String name) {
        flashcards = new ArrayList<>();
        completed = false;
        category = "";
        setName = name;
    }

    //setter
    public void setSetName(String setName) {
        this.setName = setName;
    }

    //setter
    public void setCategory(String category) {
        this.category = category;
    }

    //getter
    public String getCategory() {
        return category;
    }

    //getter
    public String getSetName() {
        return setName;
    }

    //MODIFIES: this
    //EFFECTS: adds new card to FlashcardSet if set isn't full and return true.
    //otherwise return false
    public boolean addCard(Flashcard card) {
        return false;
    }

    //MODIFIES: this
    //EFFECTS: if the given card exists, remove it from the set and return true.
    //otherwise return false
    public boolean removeCard(Flashcard card) {
        return false;
    }


    //EFFECTS: if all the cards within the set are completed, mark the set as completed.
    //Otherwise return false.
    public boolean isCompleted() {
        return false;
    }

    //EFFECTS: forces the set to be complete, regardless of the status of individual cards
    public void forceComplete() {
        //stub
    }




}
