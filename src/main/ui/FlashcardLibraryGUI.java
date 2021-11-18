package ui;

import com.formdev.flatlaf.*;
import model.Flashcard;
import model.FlashcardLibrary;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.ArrayList;
import java.util.List;


public class FlashcardLibraryGUI extends JFrame implements ActionListener {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    private final JFrame desktop;
    private JPanel buttons;
    protected FlashcardLibrary lib;
    private JsonReader jsonReader;
    private static String JSON_STORE;


    public FlashcardLibraryGUI() {
        FlatLaf.setup(new FlatIntelliJLaf());
        lib = new FlashcardLibrary();
        desktop = new JFrame();
        GroupLayout layout = new GroupLayout(desktop);
        desktop.setLayout(layout);
        desktop.setLayout(new GridLayout(1, 2));
        desktop.setTitle("Quizlet But Worse");

        titlePane();
        buttons = flashcardSetDisplay();
        desktop.add(buttons);

        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setSize(WIDTH, HEIGHT);
        desktop.setVisible(true);
    }


    private JPanel addMenuButtons() {
        JPanel menuOptions = new JPanel();
        List<JButton> menu = new ArrayList<>();
        menuOptions.setLayout(new GridLayout(4, 2));

        JButton jb1 = new JButton("Load Library");
        JButton jb2 = new JButton("Save Library");
        JButton jb3 = new JButton("Search Sets");
        JButton jb4 = new JButton("Exit");

        menuOptions.add(jb1);
        menu.add(jb1);
        menuOptions.add(jb2);
        menu.add(jb2);
        menuOptions.add(jb3);
        menu.add(jb3);
        menuOptions.add(jb4);
        menu.add(jb4);

        for (JButton button: menu) {
            button.addActionListener(this::actionPerformed);
        }
        return menuOptions;
    }

    private void titlePane() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(addTitle());
        panel.add(addMenuButtons());
        desktop.add(panel);
    }

    protected JPanel flashcardSetDisplay() {
        GridLayout buttonLayout = new GridLayout(3, 3);
        buttons = new JPanel(buttonLayout);
        List<String> setList = lib.viewLibrary();

        // Adds button with set names
        for (String set : setList) {
            JButton button = new JButton(set);
            if (lib.getSet(set).markCompleted()) {
                button.setBackground(Color.PINK);
            }
            buttons.add(button);
            button.addActionListener(this::actionPerformed);
        }

        // Adds 9th button to for an Add Set button
        JButton addSetButton = new JButton("Add Set");
        addSetButton.addActionListener(this::actionPerformed);
        buttons.add(addSetButton);

        // Add buttons to fill up slots to 7 buttons
        for (int i = 0; i < 8 - setList.size(); i++) {
            JButton button = new JButton();
            buttons.add(button);
        }
        return buttons;
    }

    private JLabel addTitle() {
        JLabel title = new JLabel();
        title.setText("Flashcards!");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.TOP);
        Font titleFont = null;
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/RobotoSlab-ExtraBold.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font sizedFont = titleFont.deriveFont(30f);
        title.setFont(sizedFont);
        return title;
    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        if (src.getText().equals("Add Set")) {
            String name = JOptionPane.showInputDialog(desktop, "Enter new set name:");
            if (name != null) {
                createCard(name);
            }
        } else if (lib.getSet(src.getText()) != null) {
            new FlashcardUI(this, lib.getSet(src.getText()));
        }
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

    private void createCard(String name) {
        lib.addSet(new FlashcardSet(name));
        lib.getSet(name).addCard(new Flashcard("", ""));
        new FlashcardUI(this, lib.getSet(name));
    }

    private void searchSets() {
        String name = JOptionPane.showInputDialog(desktop, "Enter the set you are looking for: ");
        if (lib.getSet(name) != null) {
            new FlashcardUI(this, lib.getSet(name));
        } else {
            JOptionPane.showMessageDialog(desktop, "Set cannot be found");
        }
    }

    protected void refreshButtons() {
        desktop.remove(buttons);
        buttons = flashcardSetDisplay();
        desktop.add(buttons);
        desktop.revalidate();
        desktop.repaint();
    }

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
