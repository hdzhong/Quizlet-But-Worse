package ui;

import com.formdev.flatlaf.FlatIntelliJLaf;
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
    private final FlashcardLibraryGUI libraryGUI;
    private final FlashcardSet set;
    private Flashcard currentCard;
    private final JFrame cardUI;
    private JButton card;
    private final JPanel controls;
    private final List<JButton> buttons;
    private final ClickHandler keyHandler;

    public FlashcardUI(FlashcardLibraryGUI ui, FlashcardSet set) {
        this.libraryGUI = ui;
        this.set = set;
        this.currentCard = set.getNextCard();
        FlatLaf.setup(new FlatIntelliJLaf());
        cardUI = new JFrame();
        cardUI.setSize(1000, 600);
        cardUI.setResizable(false);
        cardUI.setLayout(new FlowLayout());
        controls = new JPanel(new FlowLayout());
        keyHandler = new ClickHandler();
        buttons = new ArrayList<>();

        displayCard();
        cardUI.add(card);
        addToolButtons();

        cardUI.setVisible(true);

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
        controls.setPreferredSize(new Dimension(cardUI.getWidth(), 50));
        cardUI.add(controls);
    }


    private JButton displayCard() {
        if (currentCard == null) {
            card = new JButton("");
        } else if (currentCard.getSide()) {
            card = new JButton("Front: " + currentCard.getFront());
        } else {
            card = new JButton("Back: " + currentCard.getBack());
        }

        if (currentCard.isCompleted()) {
            card.setBackground(Color.PINK);
        }
        card.setFont(new Font("Calibri", Font.BOLD, cardUI.getWidth() / 35));
        card.setPreferredSize(new Dimension(
                cardUI.getWidth() - 100, cardUI.getHeight() - 115));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.addActionListener(keyHandler);
        return card;
    }


    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            toolbarOptions(src);
            if (src.getText().equals("Front: " + currentCard.getFront())) {
                currentCard.changeSide();
                card.setText("Back: " + currentCard.getBack());
            } else if (src.getText().equals("Back: " + currentCard.getBack())) {
                currentCard.changeSide();
                card.setText("Front: " + currentCard.getFront());
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
                addCard();
                break;
            case "Delete Card":
                deleteCard();
                break;
            case "Edit Card":
                editCard();
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
                libraryGUI.refreshButtons();
        }
    }

    private void deleteCard() {
        Flashcard temp = currentCard;
        if (set.length() == 1) {
            removeSet();
        }
        currentCard = set.getNextCard();
        set.removeCard(temp.getFront());
        refreshCard();
    }

    private void removeSet() {
        int r = JOptionPane.showConfirmDialog(
                cardUI,
                "Would you like to delete this set?");
        if (r == JOptionPane.YES_OPTION) {
            libraryGUI.lib.removeSet(set.getSetName());
            cardUI.dispose();
        }
    }

    private void editCard() {
        String front = JOptionPane.showInputDialog(
                cardUI, "Enter the front of the card", currentCard.getFront());
        String back = JOptionPane.showInputDialog(
                cardUI, "Enter the back of the card", currentCard.getBack());
        if (front != null || back != null) {
            currentCard.setFront(front);
            currentCard.setBack(back);
            refreshCard();
        }
    }

    private void addCard() {
        String front = JOptionPane.showInputDialog(cardUI, "Enter the front of the card");
        String back = JOptionPane.showInputDialog(cardUI, "Enter the back of the card");
        currentCard = new Flashcard(front, back);
        set.addCard(currentCard);
        refreshCard();
    }
}
