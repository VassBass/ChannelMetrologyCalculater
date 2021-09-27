package ui.importChannels.compareChannels;

import support.Converter;
import constants.Strings;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BreakImportDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog parent;

    private JPanel content;

    private JButton positiveButton, negativeButton;

    public BreakImportDialog(MainScreen mainScreen, JDialog parent){
        super(mainScreen, Strings.BREAK_IMPORT, true);
        this.mainScreen = mainScreen;
        this.parent = parent;

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
        this.setSize(250, 60);
        this.setLocation(Converter.POINT_CENTER(this.mainScreen, this));

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
