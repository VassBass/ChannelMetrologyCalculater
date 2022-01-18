package ui.importData;

import converters.ConverterUI;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BreakImportDialog extends JDialog {
    private static final String BREAK_IMPORT = "Припинити імпорт?";
    private static final String YES = "Так";
    private static final String NO = "Ні";

    private final MainScreen mainScreen;
    private final JDialog parent;

    private JPanel content;

    private JButton positiveButton, negativeButton;

    public BreakImportDialog(MainScreen mainScreen, JDialog parent){
        super(mainScreen, BREAK_IMPORT, true);
        this.mainScreen = mainScreen;
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.positiveButton = new DefaultButton(YES);
        this.negativeButton = new DefaultButton(NO);

        this.content = new JPanel();
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(this.clickNegativeButton);
    }

    private void build() {
        this.setSize(250, 60);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.content.add(this.positiveButton);
        this.content.add(this.negativeButton);

        this.setContentPane(this.content);
    }

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            parent.dispose();
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            parent.setVisible(true);
        }
    };
}