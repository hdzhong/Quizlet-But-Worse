package ui;

import model.Flashcard;
import model.FlashcardLibrary;
import model.FlashcardSet;

import java.util.Scanner;

public class FlashcardApp {
    private FlashcardLibrary library;
    private Scanner input;

    // EFFECTS: runs the teller application
    public FlashcardApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("v")) {
            viewSets();
        } else if (command.equals("c")) {
            viewCompletedSets();
        } else if (command.equals("m")) {
            makeSet();
        } else if (command.equals("d")) {
            removeSet();
        } else if (command.equals("e")) {
            viewSet();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    //MODIFIES: this
    //EFFECTS: displays all sets
    private void viewSets() {
        System.out.println(library.viewLibrary());
    }

    //MODIFIES: this and library (?)
    //EFFECTS: displays all sets that are marked as complete (not implemented yet)
    private void viewCompletedSets() {
        System.out.println(library.completedSets());
    }

    //MODIFIES: this
    //EFFECTS: creates set based on user input
    private void makeSet() {
        boolean makingSet = true;
        String command;
        System.out.print("Enter set name: ");
        String name = input.next();
        FlashcardSet set = new FlashcardSet(name);

        while (makingSet) {
            System.out.println("Add the front of the card: ");
            String front = input.next();
            System.out.println("Add the back of the card: ");
            String back = input.next();
            set.addCard(new Flashcard(front, back));
            library.addSet(set);
            System.out.println("Would you like to add more cards? (x to exit)");
            command = input.next();
            makingSet = returnToMenu(true, command);
        }
    }
    //MODIFIES: this
    //EFFECTS: allows user to view individual cards within a chosen set

    private void viewSet() {
        boolean viewingSet = true;
        String command;

        while (viewingSet) {
            Flashcard card;
            FlashcardSet current;
            System.out.print("Enter set you would like to view: ");
            String name = input.next();
            if (library.getSet(name).getSetName().equals(name)) {
                current = library.getSet(name);
            } else {
                System.out.println("Set does not exist, please try again");
                continue;
            }

            viewCard(true, current);

            System.out.println("Would you like to view a different set? (press x to exit)");
            command = input.next();
            viewingSet = returnToMenu(true, command);
        }
    }

    //MODIFIES: this
    //EFFECTS: user can choose to return to menu or continue with the current operation
    private boolean returnToMenu(boolean menu, String command) {
        if (command.equals("x")) {
            menu = false;
        } else if (command.equals("yes")) {
            return menu;
        } else {
            System.out.println("What would like to do next?");
        }
        return menu;
    }

    //MODIFIES: this?
    //EFFECTS: displays the front and back of the current card and can also go to the next card.
    private void viewCard(boolean viewingCards, FlashcardSet current) {
        Flashcard card;
        while (viewingCards) {
            card = current.getNextCard();
            System.out.println("Front: " + card.getFront());
            System.out.println("Back: " + card.getBack());
            System.out.println("Press n to go to the next card, x to exit back to set selection");
            String action = input.next();
            if (action.equals("n")) {
                continue;
            } else {
                viewingCards = false;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: user input to remove specific sets

    private void removeSet() {
        boolean removingSet = true;
        String command;

        while (removingSet) {
            System.out.print("Enter set you would like to remove: ");
            String name = input.next();
            if (library.getSet(name).getSetName().equals(name)) {
                System.out.println("Are you sure you want to remove this?");
                String text = input.next();
                if (text.equals("yes")) {
                    library.removeSet(name);
                } else {
                    System.out.println("Would you like to remove a different set?");
                    continue;
                }
            } else {
                System.out.println("Sorry, this set does not exist");
                System.out.println("Please try another set");
                continue;
            }
            System.out.println("Would you like to remove another set? (press x to exit)");
            command = input.next();
            removingSet = returnToMenu(true, command);
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes accounts
    private void init() {
        library = new FlashcardLibrary();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nWelcome to Flashcards!:");
        System.out.println("\tv -> view existing flashcard sets");
        System.out.println("\tc -> list of completed sets");
        System.out.println("\te -> view a set");
        System.out.println("\tm -> make a new set");
        System.out.println("\td -> delete a new set");
        System.out.println("\tq -> quit");
    }
}


