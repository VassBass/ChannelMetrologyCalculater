package ui.model;

import converters.ConverterUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogExit extends JDialog {
    public static final String CLOSE_PROGRAM_QUESTION = "Закрити програму?";
    public static final String YES = "Так";
    public static final String NO = "Ні";

    private final JFrame parent;
    private final JDialog current;

    private JPanel content;

    private JButton positiveButton, negativeButton;

    public DialogExit(JFrame parent){
        super(parent, CLOSE_PROGRAM_QUESTION, true);
        this.parent = parent;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }
    public void createElements() {
        this.positiveButton = new DefaultButton(YES);
        this.negativeButton = new DefaultButton(NO);

        this.content = new JPanel();
    }

    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(clickNegativeButton);
    }

    public void build() {
        this.setSize(250, 75);
        this.setResizable(false);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.content.add(this.positiveButton);
        this.content.add(this.negativeButton);

        this.setContentPane(this.content);
    }

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
        }
    };
}