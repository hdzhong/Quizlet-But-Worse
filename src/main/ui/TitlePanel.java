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
        super(new FlowLayout());
        this.flashcardLibraryGUI = flashcardLibraryGUI;
        JPanel menuOptions = new JPanel();
        addMenuButtons(menuOptions);

        addTitle(this);
        add(menuOptions);
        setPreferredSize(
                new Dimension((int) (FlashcardLibraryGUI.WIDTH * 0.275), FlashcardLibraryGUI.HEIGHT));
        setVisible(true);
    }


    // MODIFIES: this
    // EFFECTS: creates a new JPanel that holds 4 menu buttons that can respond to clicks
    private void addMenuButtons(JPanel panel) {
        List<JButton> menu = new ArrayList<>();
        panel.setLayout(new GridLayout(4, 2));

        createMenuButtons(panel, menu);

        for (JButton button : menu) {
            button.addActionListener(flashcardLibraryGUI.selectMenu);
        }
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
    // EFFECTS: reads logo image and creates logo as JLabel
    private void addTitle(JPanel panel) {
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("./img/quizlet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel title = new JLabel(new ImageIcon(logo));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.TOP);
        panel.add(title);
    }

}