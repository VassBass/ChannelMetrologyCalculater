package ui.removeChannels;

import application.Application;
import converters.ConverterUI;
import model.Channel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DialogRemoveAllChannels extends JDialog {
    private static final String REMOVE_ALL = "Видалити всі";
    private static final String REMOVE_ALL_QUESTION = "Ви впевнені що хочете видалити всі канали? Загальна кількість: ";
    private static final String CANCEL = "Відміна";

    private final MainScreen mainScreen;
    private final JDialog current;

    private JLabel message;

    private JButton positiveButton, negativeButton;

    public DialogRemoveAllChannels(MainScreen mainScreen){
        super(mainScreen, REMOVE_ALL, true);
        this.current = this;
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        String message = REMOVE_ALL_QUESTION + this.mainScreen.channelsList.size();
        this.message = new JLabel(message);

        this.positiveButton = new DefaultButton(REMOVE_ALL);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(this.clickNegativeButton);

    }

    private void build() {
        this.setSize(500,150);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(current)) return;
            current.dispose();
            mainScreen.setChannelsList(new ArrayList<Channel>());
            Application.context.channelsController.clear();
            if (Application.context.channelSorter.isOn()) {
                Application.context.channelSorter.setOff();
                mainScreen.searchPanel.buttonSearch.doClick();
            }
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
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