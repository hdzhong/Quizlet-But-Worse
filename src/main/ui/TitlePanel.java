package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Creates title panel that contains the logo and menu buttons

public class TitlePanel extends JPanel implements Serializable {
    private final FlashcardLibraryGUI flashcardLibraryGUI;

    public TitlePanel(FlashcardLibraryGUI flashcardLibraryGUI) {
        this.flashcardLibraryGUI = flashcardLibraryGUI;
    }

    // MODIFIES: this
    // EFFECTS: creates a new JPanel that holds 4 menu buttons that can respond to clicks
    private JPanel addMenuButtons() {
        JPanel menuOptions = new JPanel();
        List<JButton> menu = new ArrayList<>();
        menuOptions.setLayout(new GridLayout(4, 2));

        createMenuButtons(menuOptions, menu);

        for (JButton button : menu) {
            button.addActionListener(flashcardLibraryGUI);
        }
        return menuOptions;
    }

    // MODIFIES: this
    // EFFECTS: creates menu buttons
    private void createMenuButtons(JPanel menuOptions, List<JButton> menu) {
        JButton jb1 = new JButton("Load Library");
        JButton jb2 = new JButton("Save Library");
        JButton jb3 = new JButton("Search Sets");
        JButton jb4 = new JButton("Exit");

        menu.add(jb1);
        menu.add(jb2);
        menu.add(jb3);
        menu.add(jb4);

        for (JButton jb : menu) {
            setButtonSize(jb);
            jb.setFont(flashcardLibraryGUI.font.deriveFont(Font.BOLD,30f));
            jb.setHorizontalAlignment(SwingConstants.LEFT);
            menuOptions.add(jb);
        }
    }

    private void setButtonSize(JButton jb) {
        Dimension buttonSize = new Dimension(
                (int) (FlashcardLibraryGUI.WIDTH * 0.275), (int) (FlashcardLibraryGUI.HEIGHT * 0.155));
        jb.setMinimumSize(buttonSize);
        jb.setPreferredSize(buttonSize);
        jb.setMaximumSize(buttonSize);
    }

    // MODIFIES: this
    // EFFECTS: groups together the logo and the menu buttons into one pane
    void titlePane() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(addTitle());
        panel.add(addMenuButtons());
        panel.setPreferredSize(new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.275), FlashcardLibraryGUI.HEIGHT));
        flashcardLibraryGUI.desktop.add(panel);
    }

    // MODIFIES: this
    // EFFECTS: reads logo image and creates logo as JLabel
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

}