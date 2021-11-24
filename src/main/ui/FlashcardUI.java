package ui;

import model.Flashcard;
import model.FlashcardSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

// GUI for interactive flashcard that can flip sides. ALso includes multiple buttons that can navigate
// the current flashcard set

public class FlashcardUI extends JDialog {
    private final FlashcardLibraryGUI libraryGUI;
    private final FlashcardSet set;
    private Flashcard currentCard;
    private final JButton card = new JButton();
    private final JPanel controls;
    private final List<JButton> buttons;



    public FlashcardUI(FlashcardLibraryGUI ui, FlashcardSet set) {
        super(ui, "Flashcard", true);
        this.libraryGUI = ui;
        this.set = set;
        this.currentCard = set.getCurrent();

        setSize(FlashcardLibraryGUI.WIDTH, FlashcardLibraryGUI.HEIGHT);
        setResizable(false);
        setLayout(new FlowLayout());
        controls = new JPanel(new FlowLayout());
        buttons = new ArrayList<>();

        displayCard(card);
        add(card);
        addToolButtons();
        setVisible(true);
        pack();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds menu buttons to control the flashcards
    private void addToolButtons() {
        JButton addCard = new JButton("Add Card");
        JButton deleteCard = new JButton("Delete Card");
        JButton editCard = new JButton("Edit Card");
        JButton prevCard = new JButton("Previous Card");
        JButton nextCard = new JButton("Next Card");
        JButton markComplete = new JButton("Mark Completed");
        JButton deleteSet = new JButton("Delete Set");
        JButton exitSet = new JButton("Return to Menu");

        buttons.add(addCard);
        buttons.add(deleteCard);
        buttons.add(editCard);
        buttons.add(prevCard);
        buttons.add(nextCard);
        buttons.add(markComplete);
        buttons.add(deleteSet);
        buttons.add(exitSet);

        for (JButton button : buttons) {
            button.addActionListener(new NavAction());
            button.setFont(libraryGUI.font.deriveFont(Font.PLAIN,18f));
            controls.add(button);
        }
        controls.setMinimumSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.2)));
        this.add(controls);
    }

    // MODIFIES: this
    // EFFECTS: creates JButton that contains the flashcard text. Can be a new card, the front, or the back.
    private void displayCard(JButton card) {
        if (currentCard == null) {
            card.setText("");
        } else if (currentCard.getSide()) {
            card.setText("Front: " + currentCard.getFront());
        } else {
            card.setText("Back: " + currentCard.getBack());
        }

        colorCompleted(card);

        cardProperties(card);
    }

    // MODIFIES: this
    // EFFECTS: if card is completed, colour the card green, otherwise leave it as default colour
    private void colorCompleted(JButton card) {
        if (currentCard.isCompleted()) {
            card.setBackground(new Color(34, 254, 148, 46));
        } else {
            card.setBackground(null);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the various properties of the Card object
    private void cardProperties(JButton panel) {
        panel.setFont(libraryGUI.font.deriveFont(Font.BOLD,50f));
        panel.setPreferredSize(new Dimension(
                this.getWidth() - 100, (int) (this.getHeight() * 0.84)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.addActionListener(new FlipAction());
    }

    // MODIFIES: this
    // EFFECTS: allows user to interact with UI elements in FlashcardUI
    private class NavAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton card = (JButton) e.getSource();
            toolbarOptions(card);
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to interact with UI elements in FlashcardUI
    private class FlipAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton card = (JButton) e.getSource();
            flipSide(card);
        }
    }


    // MODIFIES: this
    // EFFECTS: flips the display text of the card (front --> back, back --> front)
    private void flipSide(JButton card) {
        if (card.getText().equals("Front: " + currentCard.getFront())) {
            currentCard.changeSide();
            card.setText("Back: " + currentCard.getBack());
        } else if (card.getText().equals("Back: " + currentCard.getBack())) {
            currentCard.changeSide();
            card.setText("Front: " + currentCard.getFront());
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes all UI components in FlashcardUI
    private void refreshCard() {
        displayCard(card);
        card.repaint();
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: performs action based on which toolbar button is selected
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void toolbarOptions(JButton src) {
        switch (src.getText()) {
            case "Add Card":
                addCard();
                break;
            case "Delete Card":
                deleteCard();
                break;
            case "Edit Card":
                editCard();
                break;
            case "Previous Card":
                previousCard();
                break;
            case "Next Card":
                nextCard();
                break;
            case "Delete Set":
                removeSet();
                break;
            case "Mark Completed":
                markCard();
                break;
            case "Return to Menu":
                returnMenu();
                break;
        }
    }

    //MODIFIES: this, currentCard, set
    //EFFECTS: marks the current flashcard as completed
    private void markCard() {
        currentCard.markCompleted();
        refreshCard();
    }

    //MODIFIES: this, currentCard, set
    //EFFECTS: sets currentCards as the next card in the set
    private void nextCard() {
        currentCard = set.getNextCard();
        refreshCard();
    }

    //MODIFIES: this, currentCard, set
    //EFFECTS: sets the current flashcard as the previous card in the set.
    private void previousCard() {
        currentCard = set.getPrevCard();
        refreshCard();
    }

    // MODIFIES: this, libraryGUI
    // EFFECTS: closes the flashcard and returns to libraryGUI.
    // Also refreshes libraryGUI to reflect any changes
    private void returnMenu() {
        this.dispose();
        libraryGUI.refreshButtons();
    }

    // MODIFIES: this, libraryGUI
    // EFFECTS: deletes current flashcard. If the set length is equal to 1, then prompts user if they wish
    // to delete the set. If yes, exits the card menu and removes the set from libraryGUI and updates it
    private void deleteCard() {
        Flashcard temp = currentCard;
        if (set.length() == 1) {
            removeSet();
        }
        currentCard = set.getNextCard();
        set.removeCard(temp.getFront());
        refreshCard();
    }

    // MODIFIES: this, libraryGUI
    // EFFECTS: Prompts user if they wish to delete the set.
    // If yes, exits the card menu and removes the set from libraryGUI and updates it
    private void removeSet() {
        int r = JOptionPane.showConfirmDialog(
                this,
                "Would you like to delete this set?");
        if (r == JOptionPane.YES_OPTION) {
            libraryGUI.lib.removeSet(set);
            libraryGUI.refreshButtons();
            this.dispose();
        }
    }

    // MODIFIES: this, libraryGUI, lib
    // EFFECTS: allows user to edit the front and back of the current flashcard
    private void editCard() {
        String front = JOptionPane.showInputDialog(
                this, "Enter the front of the card", currentCard.getFront());
        String back = JOptionPane.showInputDialog(
                this, "Enter the back of the card", currentCard.getBack());
        if (front != null || back != null) {
            currentCard.setFront(front);
            currentCard.setBack(back);
            refreshCard();
        }
    }

    // MODIFIES: this, libraryGUI, lib
    // EFFECTS: adds new flashcards to the given set
    private void addCard() {
        String front = JOptionPane.showInputDialog(this, "Enter the front of the card");
        String back = JOptionPane.showInputDialog(this, "Enter the back of the card");
        currentCard = new Flashcard(front, back);
        set.addCard(currentCard);
        refreshCard();
    }
}
