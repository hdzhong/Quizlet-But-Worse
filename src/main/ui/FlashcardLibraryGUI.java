package ui;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import model.FlashcardLibrary;
import model.FlashcardSet;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.IOException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.Scanner;


public class FlashcardLibraryGUI extends JFrame{
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private JFrame desktop;
    private MenuPanel menu;
    private FlashcardLibrary lib;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/library.json";


    public FlashcardLibraryGUI() {
        FlatLaf.setup(new FlatDarkLaf());

        lib = new FlashcardLibrary();
        lib.setName("Douglas' Library");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        desktop = new JFrame();
        desktop.setLayout(new GridBagLayout());
        desktop.setTitle("Flashcards!");

        loadLibrary();
        addButtonPanel();
        flashcardDisplay();


        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setSize(WIDTH, HEIGHT);
        desktop.setVisible(true);
    }

    private void addButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,2));
        JButton jb1 = new JButton("Load Library");
        JButton jb2 = new JButton("Save Library");
        JButton jb3 = new JButton("Search Sets");
        JButton jb4 = new JButton("Exit");
        panel.add(jb1);
        panel.add(jb2);
        panel.add(jb3);
        panel.add(jb4);

        desktop.add(panel);
    }

    private void flashcardDisplay() {
        JList<FlashcardSet> flashcards = new JList<>(lib.getLibrary().toArray(new FlashcardSet[0]));
        JScrollPane scrollPane = new JScrollPane(flashcards);
        desktop.add(scrollPane);
    }

    private void loadLibrary() {
        try {
            lib = jsonReader.read();
            System.out.println("Loaded " + lib.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
