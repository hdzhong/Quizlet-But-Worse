package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// A collection of individual flashcards. Keeps track of whether the set
// is completed and what the current flashcard set is.
public class FlashcardSet implements Writable {
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

//    public List<String> getListSets() {
//        List<String> setList = new ArrayList<>();
//        for (Flashcard c: flashcards) {
//            setList.add(c.getFront());
//        }
//        return setList;
//    }

    //EFFECTS: if the flashcard with name card exists, return card. Otherwise return null
    public Flashcard getCard(String card) {
        for (Flashcard c : flashcards) {
            if (c.getFront().equals(card)) {
                return c;
            }
        }
        return null;
    }

    public Flashcard getCurrent() {
        current = flashcards.peekFirst();
        return current;
    }

    //MODIFIES: this
    //EFFECTS: pops and returns the first flashcard in the list then adds that card back to the end of the set.
    public Flashcard getNextCard() {
        current = flashcards.pollLast();
        flashcards.addFirst(current);
//        System.out.println(getListSets());
        return current;
    }

    //MODIFIES: this
    //EFFECTS: returns the last flashcard in the list then adds that card back to the front of the set.
    public Flashcard getPrevCard() {
        current = flashcards.pollFirst();
        flashcards.offer(current);
        current = flashcards.peekFirst();
//        System.out.println(getListSets());
        return current;
    }

    //MODIFIES: this
    //EFFECTS: adds new card to FlashcardSet
    public void addCard(Flashcard card) {
        flashcards.addFirst(card);
//        System.out.println(getListSets());
        EventLog.getInstance().logEvent(new Event(String.format("Added %s to %s", card.getFront(), setName)));
    }

    //MODIFIES: this
    //EFFECTS: if a card with given name exists, remove it from the set and return true.
    //otherwise return false
    public boolean removeCard(String name) {
        if (flashcards.contains(this.getCard(name))) {
            EventLog.getInstance().logEvent(
                    new Event(String.format("Removed %s from %s", this.getCard(name).getFront(), setName)));
            flashcards.remove(this.getCard(name));
            return true;
        }
        return false;
    }


    //EFFECTS: if all the cards within the set are completed, mark the set as completed.
    //Otherwise return false.
    public boolean markCompleted() {
        for (Flashcard c : flashcards) {
            if (!(c.isCompleted())) {
                this.completed = false;
                return false;
            } else {
                this.completed = true;
                EventLog.getInstance().logEvent(
                        new Event(String.format("%s is completed", setName)));

            }
        }
        return true;
    }

    //getter
    public boolean isCompleted() {
        return completed;
    }

    //setter
    public void setCompleted(Boolean b) {
        this.completed = b;
    }

    //EFFECTS: return the size of the set
    public Integer length() {
        return flashcards.size();
    }

    //EFFECTS: returns true if the set is empty, otherwise return false
    public boolean isEmpty() {
        return flashcards.isEmpty();
    }

    // These methods reference code from this CPSC 210 GitHub repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("set name", setName);
        json.put("category", category);
        json.put("completed", completed);
        json.put("cards", cardsToJson());
        return json;
    }

    // EFFECTS: returns sets in this library as a JSON array
    private JSONArray cardsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Flashcard card : flashcards) {
            jsonArray.put(card.toJson());
        }
        return jsonArray;
    }
}
