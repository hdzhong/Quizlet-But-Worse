package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import model.FlashcardLibrary;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.ArrayList;
import java.util.List;



public class FlashcardLibraryGUI extends JFrame implements ActionListener {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    private JFrame desktop;
    private FlashcardLibrary lib;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private FlashcardUI flashcard;
    private static final String JSON_STORE = "./data/library.json";


    public FlashcardLibraryGUI() {
        FlatLaf.setup(new FlatDarkLaf());

        lib = new FlashcardLibrary();
        lib.setName("Douglas' Library");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        desktop = new JFrame();
        GroupLayout layout = new GroupLayout(desktop);
        desktop.setLayout(layout);
        desktop.setLayout(new GridLayout(1,2));
        desktop.setTitle("Flashcards!");

        loadLibrary();
        titlePane();
        flashcardSetDisplay();


        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setSize(WIDTH, HEIGHT);
        desktop.setVisible(true);
    }


    private JPanel addMenuButtons() {
        JPanel menuOptions = new JPanel();
        menuOptions.setLayout(new GridLayout(4,2));
        JButton jb1 = new JButton("Load Library");
        JButton jb2 = new JButton("Save Library");
        JButton jb3 = new JButton("Search Sets");
        JButton jb4 = new JButton("Exit");
        menuOptions.add(jb1);
        menuOptions.add(jb2);
        menuOptions.add(jb3);
        menuOptions.add(jb4);

        return menuOptions;
    }

    private void titlePane() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(addTitle());
        panel.add(addMenuButtons());
        desktop.add(panel);
    }

    private void flashcardSetDisplay() {
        GridLayout buttonLayout = new GridLayout(3,3);
        JPanel buttons = new JPanel(buttonLayout);
        List<JButton> buttonList = new ArrayList<>();
        List<String> setList = lib.viewLibrary();

        // Adds button with set names
        for (String set: setList) {
            JButton button = new JButton(set);
            buttonList.add(button);
            buttons.add(button);
            button.addActionListener(this::actionPerformed);
        }

        // Adds 9th button to for a Add Set button
        JButton addSetButton = new JButton("Add Set");
        addSetButton.addActionListener(this::actionPerformed);
        buttonList.add(addSetButton);
        buttons.add(addSetButton);

        // Adds buttons to fill up slots to 7 buttons
        for (int i = 0; i < 7 - setList.size(); i++) {
            JButton button = new JButton();
            buttonList.add(button);
            buttons.add(button);
        }

        desktop.add(buttons);
    }

//    private void displayAllSets() {
//        JTabbedPane setView = new JTabbedPane();
//        setView.addTab("Tab 1", flashcardSetDisplay());
//        desktop.add(setView);

    private JLabel addTitle() {
        JLabel title = new JLabel();
        title.setText("Quizlet Clone!");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.TOP);
        title.setFont(new Font("default", Font.PLAIN, 20));
        return title;
    }

    private void loadLibrary() {
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
            String name = popup();
            lib.addSet(new FlashcardSet(name));
            flashcard = new FlashcardUI(this, lib.getSet(name));
        } else if (lib.getSet(src.getText()) != null) {
            flashcard = new FlashcardUI(this, lib.getSet(src.getText()));
        }
    }

    public String popup() {
        String result = JOptionPane.showInputDialog(desktop, "Enter new set name:");
        return result;
    }
}
