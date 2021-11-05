package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements ActionListener {
    private static GridLayout layout = new GridLayout(4, 1);
    private JPanel panel;
    private JButton jb1 = new JButton("Load Library");
    private JButton jb2 = new JButton("Save Library");
    private JButton jb3 = new JButton("Search Sets");
    private JButton jb4 = new JButton("Exit");

    public MenuPanel() {
        this.panel = new JPanel();
        panel.setLayout(layout);
        panel.add(jb1);
        panel.add(jb2);
        panel.add(jb3);
        panel.add(jb4);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //stub
    }
}
