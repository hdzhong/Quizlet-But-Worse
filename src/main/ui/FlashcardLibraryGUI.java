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

import java.util.List;

// Graphical interface for an interactive quizlet-like program.

public class FlashcardLibraryGUI extends JFrame implements ActionListener {
    protected static int WIDTH;
    protected static int HEIGHT;
    public final Dimension screenSize;
    protected final JFrame desktop;
    private JPanel buttons;
    private JScrollPane scroll;
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

        TitlePane titlePane = new TitlePane(this);
        titlePane.titlePane();
        buttons = flashcardSetDisplay();
        createScrollPane();

        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setSize(WIDTH, HEIGHT + 85);
        desktop.setMinimumSize(new Dimension(WIDTH, HEIGHT + 85));
        desktop.setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: creates ScrollPane that views into the display of all sets
    private void createScrollPane() {
        scroll = new JScrollPane(
                buttons, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension((int) (WIDTH * 0.56), HEIGHT));
        desktop.add(scroll);
    }


    // MODIFIES: this
    // EFFECTS: creates panel that displays all flashcard sets as a 3x3 grid
    protected JPanel flashcardSetDisplay() {
        buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        List<String> setList = lib.viewLibrary();

        // Adds button with set names
        for (String set : setList) {
            JButton button = new JButton(set);
            if (lib.getSet(set).markCompleted()) {
                button.setBackground(new Color(34, 254, 148, 46));
            }
            buttons.add(button);
            setButtonSize(button);
            button.addActionListener(this);
        }

        // Adds 9th button to for an Add Set button
        JButton addSetButton = new JButton("Add Set");
        addSetButton.addActionListener(this);
        setButtonSize(addSetButton);
        buttons.add(addSetButton);
        buttons.setPreferredSize(new Dimension((int) (WIDTH * 0.56), HEIGHT * 3));
        buttons.setVisible(true);
        return buttons;
    }

    // MODIFIES: this
    // EFFECTS: forces buttons representing flashcard sets to be a certain size
    private void setButtonSize(JButton button) {
        button.setMinimumSize(new Dimension((int) (WIDTH * 0.18), (int)(HEIGHT * 0.2)));
        button.setPreferredSize(new Dimension((int) (WIDTH * 0.18), (int)(HEIGHT * 0.2)));
        button.setMaximumSize(new Dimension((int) (WIDTH * 0.18), (int)(HEIGHT * 0.2)));
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
                refreshButtons();
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
        scroll.remove(buttons);
        buttons = flashcardSetDisplay();
        desktop.remove(scroll);
        createScrollPane();
        desktop.revalidate();
        desktop.repaint();
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
