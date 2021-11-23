package ui;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

// A ScrollPane that views into the display of all sets

public class FlashcardSetPanel extends JScrollPane implements Serializable {
    private final FlashcardLibraryGUI libraryGUI;
    JPanel buttons;
    JScrollPane scroll;
    List<String> setList;

    public FlashcardSetPanel(FlashcardLibraryGUI gui) {
        this.libraryGUI = gui;
        createScrollPane();
    }

    //MODIFIES: this
    //EFFECTS: creates ScrollPane that views into the display of all sets
    void createScrollPane() {
        buttons = flashcardSetDisplay();
        scroll = new JScrollPane(
                buttons, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.70), FlashcardLibraryGUI.HEIGHT));
        libraryGUI.desktop.add(scroll);
    }

    // MODIFIES: this
    // EFFECTS: creates panel that displays all flashcard sets as a 3x3 grid
    protected JPanel flashcardSetDisplay() {
        buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setList = libraryGUI.lib.viewLibrary();

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
                (int) (FlashcardLibraryGUI.WIDTH * 0.13), (int) (FlashcardLibraryGUI.HEIGHT * 0.2));
    }

    // MODIFIES: this
    // EFFECTS: refreshes the panel that displays the flashcard sets
    protected void refreshButtons() {
        libraryGUI.desktop.remove(scroll);
        createScrollPane();
        libraryGUI.desktop.revalidate();
        libraryGUI.desktop.repaint();
    }
}