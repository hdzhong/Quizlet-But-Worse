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

public class FlashcardUI extends JFrame implements ActionListener {
    private FlashcardLibraryGUI libraryGUI;
    private FlashcardSet set;
    private Flashcard currentCard;
    private JFrame cardUI;
    private JButton card;
    private JToolBar controls;
    private List<JButton> buttons;
    private ClickHandler keyHandler;

    public FlashcardUI(FlashcardLibraryGUI ui,FlashcardSet set) {
        this.libraryGUI = ui;
        this.set = set;
        this.currentCard = set.getNextCard();
        FlatLaf.setup(new FlatDarkLaf());
        cardUI = new JFrame();
        cardUI.setLayout(new GridLayout(2, 1));
        controls = new JToolBar();
        keyHandler = new ClickHandler();
        buttons = new ArrayList<>();

        displayCard();
        cardUI.add(card);
        addToolButtons();

        cardUI.setVisible(true);
        cardUI.setSize(libraryGUI.WIDTH, libraryGUI.HEIGHT);
    }

    private void addToolButtons() {
        JButton addCard = new JButton("Add Card");
        JButton deleteCard = new JButton("Delete Card");
        JButton editCard = new JButton("Edit Card");
        JButton nextCard = new JButton("Next Card");
        JButton exitSet = new JButton("Exit");

        buttons.add(addCard);
        buttons.add(deleteCard);
        buttons.add(editCard);
        buttons.add(nextCard);
        buttons.add(exitSet);

        for (JButton button : buttons) {
            button.addActionListener(keyHandler);
            controls.add(button);
        }

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
        card.addActionListener(keyHandler);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            switch (src.getText()) {
                case "Add Card":
                    currentCard = new Flashcard("", "");
                    set.addCard(currentCard);
                    card.setText(currentCard.getFront());
                    break;
                case "Delete Card":
                    Flashcard temp = currentCard;
                    if (set.length() <= 1) {
                        currentCard = new Flashcard("", "");
                        break;
                    }
                    currentCard = set.getNextCard();
                    set.removeCard(temp.getFront());
                    card.setText(currentCard.getFront());
                    break;
                case "Edit Card":
                    String front = JOptionPane.showInputDialog(cardUI, "Enter the front of the card");
                    String back = JOptionPane.showInputDialog(cardUI, "Enter the back of the card");
                    currentCard.setFront(front);
                    currentCard.setBack(back);
                    card.setText(currentCard.getFront());
                    break;
                case "Next Card":
                    currentCard = set.getNextCard();
                    card.setText(currentCard.getFront());
                    break;
                case "Exit":
                    cardUI.dispose();
            }
            if (src.getText().equals(currentCard.getFront())) {
                currentCard.changeSide();
                card.setText(currentCard.getBack());
            } else if (src.getText().equals(currentCard.getBack())) {
                currentCard.changeSide();
                card.setText(currentCard.getFront());
            }
        }
    }
}
