package ui;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import model.Flashcard;
import model.FlashcardLibrary;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.ArrayList;
import java.util.List;


public class FlashcardLibraryGUI extends JFrame implements ActionListener {
    protected static int WIDTH;
    protected static int HEIGHT;
    public final Dimension screenSize;
    private final JFrame desktop;
    private JPanel buttons;
    protected FlashcardLibrary lib;
    private JsonReader jsonReader;
    private static String JSON_STORE;


    public FlashcardLibraryGUI() {
        FlatLaf.setup(new FlatArcDarkContrastIJTheme());
        lib = new FlashcardLibrary();
        desktop = new JFrame();
        GroupLayout layout = new GroupLayout(desktop);
        desktop.setLayout(layout);
        desktop.setLayout(new GridLayout(1, 2));
        desktop.setTitle("Quizlet But Worse");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int) (screenSize.width / 1.5);
        HEIGHT = (int) (screenSize.height / 1.5);

        titlePane();
        buttons = flashcardSetDisplay();
        desktop.add(buttons);

        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setSize(WIDTH, HEIGHT);
        desktop.setVisible(true);

    }

    // MODIFIES: this
    // EFFECTS: creates a new JPanel that holds 4 menu buttons that can respond to clicks
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
            button.addActionListener(this);
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
        GridLayout buttonLayout = new GridLayout(3,3);
        buttons = new JPanel(buttonLayout);
        List<String> setList = lib.viewLibrary();

        // Adds button with set names
        for (String set : setList) {
            JButton button = new JButton(set);
            if (lib.getSet(set).markCompleted()) {
                button.setBackground(new Color(34, 254, 148, 46));
            }
            buttons.add(button);
            button.addActionListener(this);
        }

        // Adds 9th button to for an Add Set button
        JButton addSetButton = new JButton("Add Set");
        addSetButton.addActionListener(this);
        buttons.add(addSetButton);

        // Add buttons to fill up slots to 7 buttons
        for (int i = 0; i < 8 - setList.size(); i++) {
            JButton button = new JButton();
            buttons.add(button);
        }
        return buttons;
    }

    private JLabel addTitle() {
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("./img/quizlet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel title = new JLabel(new ImageIcon(logo));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.TOP);
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
        selectSet(src);
        selectMenuOptions(src);
    }

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
