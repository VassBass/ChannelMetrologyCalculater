package ui;

import support.Converter;
import constants.Strings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogExit extends JDialog implements UI_Container {
    private final JFrame parent;
    private final JDialog current;

    private JPanel content;

    private JButton positiveButton, negativeButton;

    public DialogExit(JFrame parent){
        super(parent, Strings.CLOSE_PROGRAM_QUESTION, true);
        this.parent = parent;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }
    @Override
    public void createElements() {
        this.positiveButton = new JButton(Strings.YES);
        this.positiveButton.setBackground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.NO);
        this.negativeButton.setBackground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);

        this.content = new JPanel();
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(clickNegativeButton);

        this.positiveButton.addChangeListener(this.push);
        this.negativeButton.addChangeListener(this.push);
    }

    @Override
    public void build() {
        this.setSize(250, 75);
        this.setResizable(false);
        this.setLocation(Converter.POINT_CENTER(this.parent, this));

        this.content.add(this.positiveButton);
        this.content.add(this.negativeButton);

        this.setContentPane(this.content);
    }

    private final ChangeListener push = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                setBackground(Color.white.darker());
            }else {
                setBackground(Color.white);
            }
        }
    };

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
