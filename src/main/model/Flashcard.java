package model;

public class Flashcard {
    private String front;
    private String back;
    private boolean completed;

    public Flashcard(String f, String b) {
        front = f;
        back = b;
        completed = false;
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

    //MODIFIES: this
    //EFFECTS: if completed is false, set it to true. If completed is already true, return false
    public boolean markCompleted() {
        return false;
    }

    //setter
    public void setFront(String front) {
        this.front = front;
    }

    //setter
    public void setBack(String back) {
        this.back = back;
    }
}
