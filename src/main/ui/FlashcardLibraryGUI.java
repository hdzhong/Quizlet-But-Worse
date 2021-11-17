package ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
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

import java.util.ArrayList;
import java.util.List;


public class FlashcardLibraryGUI extends JFrame implements ActionListener {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    private JFrame desktop;
    private JPanel buttons;
    private FlashcardLibrary lib;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private FlashcardUI flashcard;
    private static String JSON_STORE;


    public FlashcardLibraryGUI() {
        FlatLaf.setup(new FlatDarculaLaf());

        lib = new FlashcardLibrary();
        desktop = new JFrame();
        GroupLayout layout = new GroupLayout(desktop);
        desktop.setLayout(layout);
        desktop.setLayout(new GridLayout(1, 2));
        desktop.setTitle("Flashcards!");

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
        List<JButton> buttonList = new ArrayList<>();
        List<String> setList = lib.viewLibrary();

        // Adds button with set names
        for (String set : setList) {
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
        for (int i = 0; i < 8 - setList.size(); i++) {
            JButton button = new JButton();
            buttonList.add(button);
            buttons.add(button);
        }
        return buttons;
    }

    private JLabel addTitle() {
        JLabel title = new JLabel();
        title.setText("Flashcards!");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.TOP);
        title.setFont(new Font("default", Font.PLAIN, 20));
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
            String name = popup();
            if (name != null) {
                lib.addSet(new FlashcardSet(name));
                lib.getSet(name).addCard(new Flashcard("", ""));
                flashcard = new FlashcardUI(this, lib.getSet(name));
            }
        } else if (lib.getSet(src.getText()) != null) {
            flashcard = new FlashcardUI(this, lib.getSet(src.getText()));
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
                String name = JOptionPane.showInputDialog(desktop, "Enter the set you are looking for: ");
                if (lib.getSet(name) != null) {
                    flashcard = new FlashcardUI(this, lib.getSet(name));
                } else {
                    JOptionPane.showMessageDialog(desktop, "Set cannot be found");
                }
                break;
            case "Exit":
                desktop.dispose();
                System.exit(0);
        }
        refreshButtons();
    }

    private void refreshButtons() {
        desktop.remove(buttons);
        buttons = flashcardSetDisplay();
        desktop.add(buttons);
        desktop.revalidate();
        desktop.repaint();
    }

    private void saveLibrary() {
        String filename = JOptionPane.showInputDialog(desktop, "Please give your library a name");
        JSON_STORE = String.format("./data/%s.json", filename);
        jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(lib);
            jsonWriter.close();
            JOptionPane.showMessageDialog(desktop, "Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(desktop, "Unable to write to file: " + JSON_STORE);
        }
    }

    public String popup() {
        String result = JOptionPane.showInputDialog(desktop, "Enter new set name:");
        return result;
    }

}
