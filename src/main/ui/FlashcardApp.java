package ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;

public class FlashcardApp {
    public static void main(String[] args) {
        FlatLaf.setup(new FlatArcDarkContrastIJTheme());
        new FlashcardLibraryGUI();
    }
}
