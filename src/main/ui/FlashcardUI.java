package ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
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
    private final JDialog cardUI;
    private JButton card;
    private final JPanel controls;
    private final List<JButton> buttons;



    public FlashcardUI(FlashcardLibraryGUI ui, FlashcardSet set) {
        this.libraryGUI = ui;
        this.set = set;
        this.currentCard = set.getCurrent();
        FlatLaf.setup(new FlatArcDarkContrastIJTheme());
        cardUI = new JDialog(libraryGUI, "Flashcard", true);
        cardUI.setSize(FlashcardLibraryGUI.WIDTH, FlashcardLibraryGUI.HEIGHT);
        cardUI.setResizable(false);
        cardUI.setLayout(new FlowLayout());
        controls = new JPanel(new FlowLayout());
        buttons = new ArrayList<>();

        displayCard();
        cardUI.add(card);
        addToolButtons();
        cardUI.setVisible(true);

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
        controls.setMinimumSize(new Dimension(cardUI.getWidth(), (int) (cardUI.getHeight() * 0.2)));
        cardUI.add(controls);
    }

    // MODIFIES: this
    // EFFECTS: creates JButton that contains the flashcard text. Can be a new card, the front, or the back.
    private JButton displayCard() {
        if (currentCard == null) {
            card = new JButton("");
        } else if (currentCard.getSide()) {
            card = new JButton("Front: " + currentCard.getFront());
        } else {
            card = new JButton("Back: " + currentCard.getBack());
        }
        if (currentCard.isCompleted()) {
            card.setBackground(new Color(34, 254, 148, 46));
        }
        cardProperties();
        return card;
    }

    // MODIFIES: this
    // EFFECTS: sets the various properties of the Card object
    private void cardProperties() {
        card.setFont(libraryGUI.font.deriveFont(Font.BOLD,50f));
        card.setPreferredSize(new Dimension(
                cardUI.getWidth() - 100, (int) (cardUI.getHeight() * 0.84)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.addActionListener(new FlipAction());
    }

    // MODIFIES: this
    // EFFECTS: allows user to interact with UI elements in FlashcardUI
    private class NavAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            toolbarOptions(src);
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to interact with UI elements in FlashcardUI
    private class FlipAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            flipSide(src);
        }
    }



    // MODIFIES: this
    // EFFECTS: flips the display text of the card (front --> back, back --> front)
    private void flipSide(JButton src) {
        if (src.getText().equals("Front: " + currentCard.getFront())) {
            currentCard.changeSide();
            card.setText("Back: " + currentCard.getBack());
        } else if (src.getText().equals("Back: " + currentCard.getBack())) {
            currentCard.changeSide();
            card.setText("Front: " + currentCard.getFront());
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes all UI components in FlashcardUI
    private void refreshCard() {
        cardUI.remove(card);
        card = displayCard();
        cardUI.add(card);
        cardUI.remove(controls);
        cardUI.add(controls);
        cardUI.revalidate();
        cardUI.repaint();
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
                currentCard = set.getPrevCard();
                refreshCard();
                break;
            case "Next Card":
                currentCard = set.getNextCard();
                refreshCard();
                break;
            case "Delete Set":
                removeSet();
            case "Return to Menu":
                returnMenu();
            case "Mark Completed":
                currentCard.markCompleted();
                refreshCard();
                break;
        }
    }

    // MODIFIES: this, libraryGUI
    // EFFECTS: closes the flashcard and returns to libraryGUI.
    // Also refreshes libraryGUI to reflect any changes
    private void returnMenu() {
        cardUI.dispose();
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
                cardUI,
                "Would you like to delete this set?");
        if (r == JOptionPane.YES_OPTION) {
            libraryGUI.lib.removeSet(set.getSetName());
            libraryGUI.refreshButtons();
            cardUI.dispose();
        }
    }

    // MODIFIES: this, libraryGUI, lib
    // EFFECTS: allows user to edit the front and back of the current flashcard
    private void editCard() {
        String front = JOptionPane.showInputDialog(
                cardUI, "Enter the front of the card", currentCard.getFront());
        String back = JOptionPane.showInputDialog(
                cardUI, "Enter the back of the card", currentCard.getBack());
        if (front != null || back != null) {
            currentCard.setFront(front);
            currentCard.setBack(back);
            refreshCard();
        }
    }

    // MODIFIES: this, libraryGUI, lib
    // EFFECTS: adds new flashcards to the given set
    private void addCard() {
        String front = JOptionPane.showInputDialog(cardUI, "Enter the front of the card");
        String back = JOptionPane.showInputDialog(cardUI, "Enter the back of the card");
        currentCard = new Flashcard(front, back);
        set.addCard(currentCard);
        refreshCard();
    }
}
