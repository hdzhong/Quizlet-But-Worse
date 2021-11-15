//package ui;
//
//import com.formdev.flatlaf.FlatDarkLaf;
//import com.formdev.flatlaf.FlatLaf;
//import model.Flashcard;
//import model.FlashcardSet;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FlashcardGUI2 extends JFrame implements ActionListener {
//    private FlashcardLibraryGUI libraryGUI;
//    private FlashcardSet set;
//    private Flashcard currentCard;
//    private JFrame cardUI;
//    private JButton card;
//    private JToolBar controls;
//    private List<JButton> buttons;
//    private ClickHandler keyHandler;
//
//    public FlashcardGUI2(FlashcardLibraryGUI ui,FlashcardSet set) {
//        this.libraryGUI = ui;
//        this.set = set;
//        this.currentCard = set.getNextCard();
//        FlatLaf.setup(new FlatDarkLaf());
//        cardUI = new JFrame();
//        cardUI.setLayout(new CardLayout());
//        controls = new JToolBar();
//        keyHandler = new ClickHandler();
//        buttons = new ArrayList<JButton>();
//
//        createCards();
//        cardUI.add(card);
//        addToolButtons();
//
//        cardUI.setVisible(true);
//        cardUI.setSize(libraryGUI.WIDTH, libraryGUI.HEIGHT);
//    }
//
//    private void addToolButtons() {
//        JButton addCard = new JButton("Add Card");
//        JButton deleteCard = new JButton("Delete Card");
//        JButton editCard = new JButton("Edit Card");
//        JButton nextCard = new JButton("Next Card");
//        JButton exitSet = new JButton("Exit");
//
//        buttons.add(addCard);
//        buttons.add(deleteCard);
//        buttons.add(editCard);
//        buttons.add(nextCard);
//        buttons.add(exitSet);
//
//        for (JButton button : buttons) {
//            button.addActionListener(keyHandler);
//            controls.add(button);
//        }
//
//        cardUI.add(controls);
//    }
//
//
//    private void createCards() {
//        for (Flashcard fc: set) {
//            JPanel card = new JPanel();
//            JButton text = new JButton(fc.getFront());
//            text.addActionListener(keyHandler);
//            card.add(text);
//            cardUI.add(card);
//        }
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//    }
//
//
//    private class ClickHandler implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            JButton src = (JButton) e.getSource();
//
//            switch (src.getText()) {
//                case "Add Card":
//                    set.addCard(new Flashcard("", ""));
//                case "Delete Card":
//                    set.removeCard(currentCard.getFront());
//                case "Edit Card":
//                    ;
//                case "Next Card":
//                    currentCard = set.getNextCard();
//                    cardUI.repaint();
//                case "Exit":
//                    cardUI.dispose();
//            }
//            if (src.getText().equals(currentCard.getFront()) || src.getText().equals(currentCard.getBack())) {
//                currentCard.changeSide();
//                cardUI.repaint();
//            }
//        }
//    }
//}
