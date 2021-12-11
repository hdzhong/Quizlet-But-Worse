package ui;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

// A ScrollPane that views into the display of all sets

public class FlashcardSetPanel extends JScrollPane implements Serializable {
    private final FlashcardLibraryGUI libraryGUI;

    public FlashcardSetPanel(FlashcardLibraryGUI gui) {
        super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.libraryGUI = gui;
        flashcardSetDisplay();
        setPreferredSize(new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.70), FlashcardLibraryGUI.HEIGHT));
    }


    // MODIFIES: this
    // EFFECTS: creates panel that displays all flashcard sets as a 3x3 grid
    protected void flashcardSetDisplay() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        List<String> setList = libraryGUI.lib.viewLibrary();

        // Adds button with set names
        for (String set : setList) {
            JButton button = new JButton(set);
            if (libraryGUI.lib.getSet(set).markCompleted()) {
                button.setBackground(new Color(34, 254, 148, 46));
            }
            buttons.add(button);
            button.setFont(libraryGUI.font.deriveFont(Font.PLAIN,18f));
            setButtonSize(button);
            button.addActionListener(libraryGUI.selectSet);
        }

        // Adds 9th button to for an Add Set button
        JButton addSetButton = new JButton("Add Set");
        addSetButton.addActionListener(libraryGUI.selectSet);
        addSetButton.setFont(libraryGUI.font.deriveFont(Font.PLAIN,15f));
        setButtonSize(addSetButton);
        buttons.add(addSetButton);
        buttons.setPreferredSize(
                new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.56), FlashcardLibraryGUI.HEIGHT * 2));
        buttons.setVisible(true);
        this.setViewportView(buttons);
    }

    // MODIFIES: this
    // EFFECTS: forces buttons representing flashcard sets to be a certain size
    private void setButtonSize(JButton button) {
        button.setMinimumSize(cardSize());
        button.setPreferredSize(cardSize());
        button.setMaximumSize(cardSize());
    }

    // MODIFIES: this
    // EFFECTS: establishes dimensions for the cards
    private Dimension cardSize() {
        return new Dimension(
                (int) (FlashcardLibraryGUI.WIDTH * 0.13), (int) (FlashcardLibraryGUI.HEIGHT * 0.2));
    }

    // MODIFIES: this
    // EFFECTS: refreshes the panel that displays the flashcard sets
    protected void refreshButtons() {
        flashcardSetDisplay();
        repaint();
        revalidate();
    }
}