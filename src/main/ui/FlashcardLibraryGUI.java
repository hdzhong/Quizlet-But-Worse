package ui;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import model.Flashcard;
import model.FlashcardLibrary;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

// Graphical interface for an interactive quizlet-like program.

public class FlashcardLibraryGUI extends JFrame implements ActionListener {
    protected static int WIDTH;
    protected static int HEIGHT;
    public final Dimension screenSize;
    protected final JFrame desktop;
    private final FlashcardSetPanel flashcardSetPanel = new FlashcardSetPanel(this);
    protected FlashcardLibrary lib;
    private JsonReader jsonReader;
    private static String JSON_STORE;


    public FlashcardLibraryGUI() {
        FlatLaf.setup(new FlatArcDarkContrastIJTheme());
        lib = new FlashcardLibrary();
        desktop = new JFrame();
        desktop.setLayout(new FlowLayout());
        desktop.setTitle("Quizlet But Worse");

        // Gets screen size to make sure app displays similarly across different screens
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int) (screenSize.width / 1.5);
        HEIGHT = (int) (screenSize.height / 1.5);

        TitlePanel titlePane = new TitlePanel(this);
        titlePane.titlePane();
        flashcardSetPanel.buttons = flashcardSetPanel.flashcardSetDisplay();
        flashcardSetPanel.createScrollPane();

        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setSize(WIDTH, (int) (HEIGHT * 1.1));
        desktop.setMinimumSize(new Dimension(WIDTH, (int) (HEIGHT * 1.1)));
        desktop.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: opens up file select to allow user to choose library to load. Then loads selected data
    private void loadLibrary() {
        JFileChooser fc = new JFileChooser("./data");
        int res = fc.showOpenDialog(desktop);
        if (res == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
            jsonReader = new JsonReader(filepath);
        }
        try {
            lib = jsonReader.read();
            System.out.println("Loaded " + lib.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to interact with buttons on FlashcardLibraryGUI
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        selectSet(src);
        selectMenuOptions(src);
    }

    // MODIFIES: this
    // EFFECTS: if Add Set is selected, prompt user to enter a set name and display new flashcard set.
    // If an existing set is selected, display that set.
    private void selectSet(JButton src) {
        if (src.getText().equals("Add Set")) {
            String name = JOptionPane.showInputDialog(desktop, "Enter new set name:");
            if (name != null) {
                createCard(name);
            }
        } else if (lib.getSet(src.getText()) != null) {
            new FlashcardUI(this, lib.getSet(src.getText()));
        }
    }

    // MODIFIES: this
    // EFFECTS: selects one of four menu options
    private void selectMenuOptions(JButton src) {
        switch (src.getText()) {
            case "Load Library":
                loadLibrary();
                flashcardSetPanel.refreshButtons();
                break;
            case "Save Library":
                saveLibrary();
                break;
            case "Search Sets":
                searchSets();
                break;
            case "Exit":
                System.exit(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds set to the FlashcardLibrary and then displays new set of cards
    private void createCard(String name) {
        lib.addSet(new FlashcardSet(name));
        lib.getSet(name).addCard(new Flashcard("", ""));
        new FlashcardUI(this, lib.getSet(name));
    }

    // MODIFIES: this
    // EFFECTS: searches for set given set name. Returns set if found, otherwise returns error message
    private void searchSets() {
        String name = JOptionPane.showInputDialog(desktop, "Enter the set you are looking for: ");
        if (lib.getSet(name) != null) {
            new FlashcardUI(this, lib.getSet(name));
        } else {
            JOptionPane.showMessageDialog(desktop, "Set cannot be found");
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes the panel that displays the flashcard sets
    protected void refreshButtons() {
        flashcardSetPanel.refreshButtons();
    }

    // MODIFIES: this
    // EFFECTS: allows user to save file with given name, storing all information as JSON
    private void saveLibrary() {
        String filename = JOptionPane.showInputDialog(desktop, "Please give your library a name");
        JSON_STORE = String.format("./data/%s.json", filename);
        JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(lib);
            jsonWriter.close();
            JOptionPane.showMessageDialog(desktop, "Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(desktop, "Unable to write to file: " + JSON_STORE);
        }
    }
}
