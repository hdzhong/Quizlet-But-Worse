package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import model.Flashcard;
import model.FlashcardSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FlashcardUI extends JFrame {
    private FlashcardLibraryGUI libraryGUI;
    private FlashcardSet set;
    private Flashcard currentCard;
    private JFrame cardUI;
    private JButton card;
    private JPanel controls;
    private List<JButton> buttons;
    private ClickHandler keyHandler;

    public FlashcardUI(FlashcardLibraryGUI ui, FlashcardSet set) {
        this.libraryGUI = ui;
        this.set = set;
        this.currentCard = set.getNextCard();
        FlatLaf.setup(new FlatDarkLaf());
        cardUI = new JFrame();
        cardUI.setLayout(new BoxLayout(cardUI.getContentPane(), BoxLayout.Y_AXIS));
        controls = new JPanel(new FlowLayout());
        keyHandler = new ClickHandler();
        buttons = new ArrayList<>();

        displayCard();
        cardUI.add(card);
        addToolButtons();

        cardUI.setVisible(true);
        cardUI.setSize(1000, 600);
    }

    private void addToolButtons() {
        JButton addCard = new JButton("Add Card");
        JButton deleteCard = new JButton("Delete Card");
        JButton editCard = new JButton("Edit Card");
        JButton nextCard = new JButton("Next Card");
        JButton markComplete = new JButton("Mark Completed");
        JButton exitSet = new JButton("Exit");

        buttons.add(addCard);
        buttons.add(deleteCard);
        buttons.add(editCard);
        buttons.add(nextCard);
        buttons.add(markComplete);
        buttons.add(exitSet);

        for (JButton button : buttons) {
            button.addActionListener(keyHandler);
            controls.add(button);
        }
        controls.setBackground(Color.lightGray);
        controls.setPreferredSize(new Dimension(600, 35));
        cardUI.add(Box.createVerticalGlue());
        cardUI.add(controls);
    }


    private JButton displayCard() {
        if (currentCard == null) {
            card = new JButton("");
        } else if (currentCard.getSide()) {
            card = new JButton(currentCard.getFront());
        } else {
            card = new JButton(currentCard.getBack());
        }

        if (currentCard.isCompleted()) {
            card.setBackground(Color.PINK);
        }
        card.setPreferredSize(new Dimension(1000, 565));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.addActionListener(keyHandler);
        return card;
    }


    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            toolbarOptions(src);
            if (src.getText().equals(currentCard.getFront())) {
                currentCard.changeSide();
                card.setText(currentCard.getBack());
            } else if (src.getText().equals(currentCard.getBack())) {
                currentCard.changeSide();
                card.setText(currentCard.getFront());
            }
        }
    }

    private void refreshCard() {
        cardUI.remove(card);
        card = displayCard();
        cardUI.add(card);
        cardUI.remove(controls);
        cardUI.add(controls);
        cardUI.revalidate();
        cardUI.repaint();
    }

    private void toolbarOptions(JButton src) {
        switch (src.getText()) {
            case "Add Card":
                String front = JOptionPane.showInputDialog(cardUI, "Enter the front of the card");
                String back = JOptionPane.showInputDialog(cardUI, "Enter the back of the card");
                currentCard = new Flashcard(front, back);
                set.addCard(currentCard);
                refreshCard();
                break;
            case "Delete Card":
                Flashcard temp = currentCard;
                if (set.length() <= 1) {
                    currentCard = new Flashcard("", "");
                    break;
                }
                currentCard = set.getNextCard();
                set.removeCard(temp.getFront());
                refreshCard();
                break;
            case "Edit Card":
                front = JOptionPane.showInputDialog(cardUI, "Enter the front of the card");
                back = JOptionPane.showInputDialog(cardUI, "Enter the back of the card");
                currentCard.setFront(front);
                currentCard.setBack(back);
                refreshCard();
                break;
            case "Next Card":
                currentCard = set.getNextCard();
                refreshCard();
                break;
            case "Mark Completed":
                currentCard.markCompleted();
                refreshCard();
                break;
            case "Exit":
                cardUI.dispose();
        }
    }
}
