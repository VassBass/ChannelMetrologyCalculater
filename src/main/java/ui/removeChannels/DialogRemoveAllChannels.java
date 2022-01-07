package ui.removeChannels;

import backgroundTasks.controllers.RemoveChannels;
import constants.Strings;
import converters.ConverterUI;
import ui.UI_Container;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogRemoveAllChannels extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog current;

    private JLabel message;

    private JButton positiveButton, negativeButton;

    public DialogRemoveAllChannels(MainScreen mainScreen){
        super(mainScreen, Strings.REMOVE_ALL, true);
        this.current = this;
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }
    @Override
    public void createElements() {
        String message = Strings.REMOVE_ALL_QUESTION + this.mainScreen.channelsList.size();
        this.message = new JLabel(message);

        this.positiveButton = new JButton(Strings.REMOVE_ALL);
        this.positiveButton.setBackground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.positiveButton.addChangeListener(this.pushButton);
        this.negativeButton.addChangeListener(this.pushButton);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(this.clickNegativeButton);

    }

    @Override
    public void build() {
        this.setSize(500,150);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
            new RemoveChannels(mainScreen, mainScreen.channelsList).execute();
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
        }
    };

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(Color.white.darker());
            }else {
                button.setBackground(Color.white);
            }

        }
    };

    private class MainPanel extends JPanel{

        private MainPanel(){
            super(new GridBagLayout());

            this.add(message, new Cell(0));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(negativeButton);
            buttonsPanel.add(positiveButton);
            this.add(buttonsPanel, new Cell(1));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int y){
                super();

                this.fill = BOTH;
                this.insets = new Insets(20,20,20,20);

                this.gridx = 0;
                this.gridy = y;
            }
        }
    }
}
