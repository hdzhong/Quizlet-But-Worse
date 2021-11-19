package ui;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

// A ScrollPane that views into the display of all sets

public class FlashcardSetPanel extends JPanel implements Serializable {
    private final FlashcardLibraryGUI flashcardLibraryGUI;
    JPanel buttons;
    JScrollPane scroll;

    public FlashcardSetPanel(FlashcardLibraryGUI flashcardLibraryGUI) {
        this.flashcardLibraryGUI = flashcardLibraryGUI;
    }

    //MODIFIES: this
    //EFFECTS: creates ScrollPane that views into the display of all sets
    void createScrollPane() {
        scroll = new JScrollPane(
                buttons, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.56), FlashcardLibraryGUI.HEIGHT));
        flashcardLibraryGUI.desktop.add(scroll);
    }

    // MODIFIES: this
    // EFFECTS: creates panel that displays all flashcard sets as a 3x3 grid
    protected JPanel flashcardSetDisplay() {
        buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        List<String> setList = flashcardLibraryGUI.lib.viewLibrary();

        // Adds button with set names
        for (String set : setList) {
            JButton button = new JButton(set);
            if (flashcardLibraryGUI.lib.getSet(set).markCompleted()) {
                button.setBackground(new Color(34, 254, 148, 46));
            }
            buttons.add(button);
            setButtonSize(button);
            button.addActionListener(flashcardLibraryGUI);
        }

        // Adds 9th button to for an Add Set button
        JButton addSetButton = new JButton("Add Set");
        addSetButton.addActionListener(flashcardLibraryGUI);
        setButtonSize(addSetButton);
        buttons.add(addSetButton);
        buttons.setPreferredSize(
                new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.56), FlashcardLibraryGUI.HEIGHT * 2));
        buttons.setVisible(true);
        return buttons;
    }

    // MODIFIES: this
    // EFFECTS: forces buttons representing flashcard sets to be a certain size
    void setButtonSize(JButton button) {
        button.setMinimumSize(cardSize());
        button.setPreferredSize(cardSize());
        button.setMaximumSize(cardSize());
    }

    // MODIFIES: this
    // EFFECTS: establishes dimensions for the cards
    private Dimension cardSize() {
        return new Dimension(
                (int) (FlashcardLibraryGUI.WIDTH * 0.18), (int) (FlashcardLibraryGUI.HEIGHT * 0.2));
    }

    // MODIFIES: this
    // EFFECTS: refreshes the panel that displays the flashcard sets
    protected void refreshButtons() {
        scroll.remove(buttons);
        buttons = flashcardSetDisplay();
        flashcardLibraryGUI.desktop.remove(scroll);
        createScrollPane();
        flashcardLibraryGUI.desktop.revalidate();
        flashcardLibraryGUI.desktop.repaint();
    }
}