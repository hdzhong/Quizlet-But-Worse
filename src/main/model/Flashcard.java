package model;

import org.json.JSONObject;
import persistence.Writable;

// a flashcard that contains a front and back side
public class Flashcard implements Writable {
    private String front;
    private String back;
    private boolean completed;
    private boolean side;

    public Flashcard(String f, String b) {
        front = f;
        back = b;
        completed = false;
        side = true;
    }

    //getter
    public String getFront() {
        return front;
    }

    //getter
    public String getBack() {
        return back;
    }

    //getter
    public boolean isCompleted() {
        return completed;
    }

    //setter
    public void setCompleted(Boolean b) {
        this.completed = b;
    }

    //MODIFIES: this
    //EFFECTS: if completed is false, set it to true. If completed is already true, return false
    public boolean markCompleted() {
        if (!completed) {
            completed = true;
        }
        return true;
    }

    //setter
    public void setFront(String front) {
        this.front = front;
    }

    //setter
    public void setBack(String back) {
        this.back = back;
    }

    //MODIFIES: this
    //EFFECTS: changes the side (true = front, false = back)
    public void changeSide() {
        side = !(side);
    }

    //getter
    public boolean getSide() {
        return side;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("front", front);
        json.put("back", back);
        json.put("completed", completed);
        return json;
    }
}
