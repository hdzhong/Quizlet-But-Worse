package model;

import org.json.JSONObject;
import persistence.Writable;

// a flashcard that contains a front and back side,
// as well as keeps track of the current side and the completion status

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
    //EFFECTS: if completed is false, set it to true. Regardless return true.
    public boolean markCompleted() {
        if (!completed) {
            completed = true;
            EventLog.getInstance().logEvent(
                    new Event(String.format("%s is now complete!", this.getFront())));
            return true;
        }
        EventLog.getInstance().logEvent(
                new Event(String.format("%s is already completed", this.getFront())));
        return true;
    }

    //setter
    public void setFront(String front) {
        String previous = this.front;
        this.front = front;
        EventLog.getInstance().logEvent(
                new Event(String.format("%s has been edited to %s", previous, this.front)));
    }

    //setter
    public void setBack(String back) {
        String previous = this.back;
        this.back = back;
        EventLog.getInstance().logEvent(
                new Event(String.format("%s has been edited to %s", previous, this.back)));
    }

    //MODIFIES: this
    //EFFECTS: changes the side (true = front, false = back)
    public void changeSide() {
        side = (!side);
        EventLog.getInstance().logEvent(
                new Event("Card has been flipped"));
    }

    //getter
    public boolean getSide() {
        return side;
    }

    // This method references code from this CPSC 210 GitHub repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("front", front);
        json.put("back", back);
        json.put("completed", completed);
        return json;
    }
}
