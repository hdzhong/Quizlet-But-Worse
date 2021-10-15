package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlashcardSet {
    private LinkedList<Flashcard> flashcards;
    private String category;
    private boolean completed;
    private String setName;
    private Flashcard current;

    public FlashcardSet(String name) {
        flashcards = new LinkedList<>();
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

    //EFFECTS: if the flashcard with name card exists, return card. Otherwise return null
    public Flashcard getCard(String card) {
        for (Flashcard c : flashcards) {
            if (c.getFront().equals(card)) {
                return c;
            }
        }
        return null;
    }

    public Flashcard getNextCard() {
        current = flashcards.pop();
        flashcards.offer(current);
        return current;
    }


    //MODIFIES: this
    //EFFECTS: adds new card to FlashcardSet
    public void addCard(Flashcard card) {
        flashcards.add(card);
    }

    //MODIFIES: this
    //EFFECTS: if a card with given name exists, remove it from the set and return true.
    //otherwise return false
    public boolean removeCard(String name) {
        List<Flashcard> remove = new ArrayList<>();
        for (Flashcard c : flashcards) {
            if (c.getFront().equals(name)) {
                remove.add(c);
            }
        }
        return flashcards.removeAll(remove);
    }


    //EFFECTS: if all the cards within the set are completed, mark the set as completed.
    //Otherwise return false.
    public boolean isCompleted() {
        for (Flashcard c : flashcards) {
            if (!(c.isCompleted())) {
                completed = false;
            } else {
                completed = true;
            }
        }
        return completed;
    }

    //EFFECTS: forces the set to be complete, regardless of the status of individual cards
    public void forceComplete() {
        completed = true;
    }

    //EFFECTS: return the size of the set
    public Integer length() {
        return flashcards.size();
    }

    //EFFECTS: returns true if the set is empty, otherwise return false
    public boolean isEmpty() {
        return flashcards.isEmpty();
    }


}
