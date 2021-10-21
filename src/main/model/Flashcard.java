package model;

// a flashcard that contains a front and back side
public class Flashcard {
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

    //MODIFIES: this
    //EFFECTS: if completed is false, set it to true. If completed is already true, return false
    public boolean markCompleted() {
        if (completed == false) {
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
}
