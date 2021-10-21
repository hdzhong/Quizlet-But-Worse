package ui;

import model.Flashcard;
import model.FlashcardLibrary;
import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// This class references code from this CPSC 210 GitHub repo
// Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp

public class FlashcardApp {
    private static final String JSON_STORE = "./data/library.json";
    private FlashcardLibrary library;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the teller application
    public FlashcardApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        System.out.println("Please give the library a name");
        String name = input.next();
        library = new FlashcardLibrary();
        library.setName(name);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;

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
        } else if (command.equals("vc")) {
            viewCompletedSets();
        } else if (command.equals("c")) {
            createSet();
        } else if (command.equals("d")) {
            removeSet();
        } else if (command.equals("e")) {
            viewSet();
        } else if (command.equals("m")) {
            matchCard();
        } else if (command.equals("s")) {
            saveLibrary();
        } else if (command.equals("l")) {
            loadLibrary();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    private void loadLibrary() {
        try {
            library = jsonReader.read();
            System.out.println("Loaded " + library.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
            System.out.println("Saved " + library.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: displays all sets
    private void viewSets() {
        System.out.println(library.viewLibrary());
    }

    //MODIFIES: this and library (?)
    //EFFECTS: displays all sets that are marked as complete
    private void viewCompletedSets() {
        System.out.println(library.completedSets());
    }

    //MODIFIES: this
    //EFFECTS: creates set based on user input
    private void createSet() {
        boolean makingSet = true;
        String command;

        System.out.print("Enter set name: ");
        String name = input.next();
        FlashcardSet set = new FlashcardSet(name);
        System.out.println("Enter category: ");
        set.setCategory(input.next());

        while (makingSet) {

            System.out.println("Add the front of the card: ");
            String front = input.next();
            System.out.println("Add the back of the card: ");
            String back = input.next();

            set.addCard(new Flashcard(front, back));

            System.out.println("Would you like to add more cards? (x to exit, yes to continue)");
            command = input.next();
            makingSet = returnToMenu(true, command);
        }
        library.addSet(set);
    }

    //MODIFIES: this
    //EFFECTS: allows user to view individual cards within a chosen set
    private void viewSet() {
        boolean viewingSet = true;
        String command;

        while (viewingSet) {
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
            System.out.println("Do you want to see if the set is completed?");
            String check = input.next();
            checkComplete(current, check);
            System.out.println("Would you like to view a different set? (press x to exit, yes to continue)");
            command = input.next();
            viewingSet = returnToMenu(true, command);
        }
    }

    //EFFECTS: prints out message telling user if the set is completed or not
    private void checkComplete(FlashcardSet current, String check) {
        if (check.equals("yes")) {
            if (current.isCompleted()) {
                System.out.println("Set is completed!");
            } else {
                System.out.println("Set is not completed");
            }
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
            System.out.println("Please try again");
            command = input.next();
            returnToMenu(menu, command);
        }
        return menu;
    }

    //MODIFIES: this?
    //EFFECTS: displays the current card and allows users to interact with card.
    private void viewCard(boolean viewingCards, FlashcardSet current) {
        Flashcard card;
        while (viewingCards) {
            card = current.getNextCard();
            printSide(card);
            System.out.println("f - flip, n - next, c - mark complete, x - exit");
            boolean viewingCard = true;
            while (viewingCard) {
                String action = input.next();
                if (action.equals("n")) {
                    break;
                } else if (action.equals("f")) {
                    card.changeSide();
                    printSide(card);
                    continue;
                } else if (action.equals("c")) {
                    card.markCompleted();
                    System.out.println("Next card:");
                    break;
                } else {
                    viewingCard = false;
                    viewingCards = false;
                }
            }
        }
    }

    //EFFECT: prints out the currents side of the card
    private void printSide(Flashcard card) {
        if (card.getSide()) {
            System.out.println("Front: " + card.getFront());
        } else if (!card.getSide()) {
            System.out.println("Back: " + card.getBack());
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
                library.removeSet(name);
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

    //MODIFIES: this
    //EFFECTS: initializes game where user tries to give the correct definition for the given term
    private void matchCard() {
        boolean matching = true;
        FlashcardSet current = null;
        Flashcard card;
        System.out.print("Enter set you would like to view: ");
        String name = input.next();

        if (library.getSet(name).getSetName().equals(name)) {
            current = library.getSet(name);
        } else {
            System.out.println("Set does not exist, please try again");
        }

        while (matching) {
            card = current.getNextCard();
            printSide(card);
            System.out.println("What is the definition?");
            boolean answering = true;
            while (answering) {
                String answer = input.next();
                String cleaned = answer.toLowerCase().replaceAll("\\p{P}", "");
                if (cleaned.equals(card.getBack().toLowerCase().replaceAll("\\p{P}", ""))) {
                    System.out.println("That's correct!");
                    card.markCompleted();
                    answering = false;
                } else {
                    System.out.println("That is incorrect, please try again");
                    continue;
                }
            }
            if (current.isCompleted()) {
                matching = false;
            } else {
                continue;
            }
        }
    }


    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nWelcome to Flashcards!:");
        System.out.println("\tv -> View Existing Flashcard Sets");
        System.out.println("\tvc -> List of Completed Sets");
        System.out.println("\te -> View a Set");
        System.out.println("\tc-> Create a New Set");
        System.out.println("\tm-> Matching Game");
        System.out.println("\td -> Delete a Set");
        System.out.println("\ts -> Save Library");
        System.out.println("\tl -> Load Library");
        System.out.println("\tq -> Quit");
    }
}


