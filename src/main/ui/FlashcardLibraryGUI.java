package ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import model.Event;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Graphical interface for an interactive quizlet-like program.

public class FlashcardLibraryGUI extends JFrame {
    protected static int WIDTH = 1280;
    protected static int HEIGHT = 750;
    private final FlashcardSetPanel sets;
    protected FlashcardLibrary lib;
    protected SelectMenu selectMenu = new SelectMenu();
    protected SelectSet selectSet = new SelectSet();
    private final TitlePanel title;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private static String JSON_STORE;
    protected Font font;


    public FlashcardLibraryGUI() {
        UIManager.put("Button.arc", 10);
        lib = new FlashcardLibrary();
        setLayout(new FlowLayout());
        setTitle("Quizlet But Worse");
        initFont();

        title = new TitlePanel(this);
        add(title);
        sets = new FlashcardSetPanel(this);
        add(sets);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setVisible(true);
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
                e.getWindow().dispose();
            }
        });
    }


    // MODIFIES: this, library
    // EFFECTS: opens up file select to allow user to choose library to load. Then loads selected data
    private void loadLibrary() {
        JFileChooser fc = new JFileChooser("./data");
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
            jsonReader = new JsonReader(filepath);
        }
        try {
            if (jsonReader != null) {
                lib = jsonReader.read();
                System.out.println("Loaded " + lib.getName() + " from " + JSON_STORE);
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to interact with buttons on FlashcardLibraryGUI
    private class SelectSet extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            selectSet(src);
        }
    }


    // MODIFIES: this
    // EFFECTS: allows user to interact with buttons on FlashcardLibraryGUI
    private class SelectMenu extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            selectMenuOptions(src);
        }
    }

    // MODIFIES: this
    // EFFECTS: if Add Set is selected, prompt user to enter a set name and display new flashcard set.
    // If an existing set is selected, display that set.
    private void selectSet(JButton src) {
        if (src.getText().equals("Add Set")) {
            String name = JOptionPane.showInputDialog(this, "Enter new set name:");
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
                sets.refreshButtons();
                break;
            case "Save Library":
                saveLibrary();
                break;
            case "Search Sets":
                searchSets();
                break;
            case "Exit":
                printLog(EventLog.getInstance());
                System.exit(0);
        }
    }

    // MODIFIES: this, library
    // EFFECTS: adds set to the FlashcardLibrary and then displays new set of cards
    private void createCard(String name) {
        lib.addSet(new FlashcardSet(name));
        FlashcardSet newSet = lib.getSet(name);
        newSet.addCard(new Flashcard("Empty Card", "Please Edit"));
        new FlashcardUI(this, newSet);
    }

    // MODIFIES: this
    // EFFECTS: loads and initializes the custom font
    private void initFont() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/RobotoSlab-Light.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: searches for set given set name. Returns set if found, otherwise returns error message
    private void searchSets() {
        String name = JOptionPane.showInputDialog(this, "Enter the set you are looking for: ");
        if (lib.getSet(name) != null) {
            new FlashcardUI(this, lib.getSet(name));
        } else {
            JOptionPane.showMessageDialog(this, "Set cannot be found");
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes the panel that displays the flashcard sets
    protected void refreshButtons() {
        sets.refreshButtons();
    }

    // MODIFIES: this
    // EFFECTS: allows user to save file with given name, storing all information as JSON
    private void saveLibrary() {
        String filename = JOptionPane.showInputDialog(this, "Please give your library a name");
        if (filename != null) {
            JSON_STORE = String.format("./data/%s.json", filename);
            jsonWriter = new JsonWriter(JSON_STORE);
            try {
                jsonWriter.open();
                jsonWriter.write(lib);
                jsonWriter.close();
                JOptionPane.showMessageDialog(this, "Saved to " + JSON_STORE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE);
            }
        }
    }

    //MODIFIES: nothing?
    //EFFECTS: prints out the log of the events that happened during the application to the console
    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString() + "\n");
        }
    }
}
