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
        String command = null;

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
        } else if (command.equals("vs")) {
            viewSet();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    private void viewSets() {
        System.out.println(library.viewLibrary());
    }

    private void viewCompletedSets() {
        System.out.println(library.completedSets());
    }

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
            System.out.println("Would you like to add more cards? (x to exit)");
            command = input.next();
            if (command.equals("x")) {
                library.addSet(set);
                makingSet = false;
            } else if (command.equals("yes")) {
                continue;
            } else {
                System.out.println("What would like to do next?");
            }
        }
    }

    private void viewSet() {
        boolean viewingSet = true;
        String command;

        while (viewingSet) {
            boolean viewingCards = true;
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

            while (viewingCards) {
                card = current.getNextCard();
                System.out.println(card);
                System.out.println("Press n to go to the next card, x to exit back to set selection");
                String action = input.next();
                if (action.equals("n")) {
                    continue;
                } else {
                    viewingCards = false;
                }
            }

            System.out.println("Would you like to view a different set? (press x to exit)");
            command = input.next();
            if (command.equals("x")) {
                viewingSet = false;
            } else if (command.equals("yes")) {
                continue;
            } else {
                System.out.println("What would like to do next?");
            }
        }
    }

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
            if (command.equals("x")) {
                removingSet = false;
            } else if (command.equals("yes")) {
                continue;
            } else {
                System.out.println("What would like to do next?");
            }
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
        System.out.println("\te -> enter a set");
        System.out.println("\tm -> make a new set");
        System.out.println("\td -> delete a new set");
        System.out.println("\tq -> quit");
    }
}


