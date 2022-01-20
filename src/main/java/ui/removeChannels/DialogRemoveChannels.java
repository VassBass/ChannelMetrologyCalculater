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

public class DialogRemoveChannels extends JDialog {
    private static final String REMOVE_CHANNEL = "Видалити канал";
    private static final String CHOOSE_CHANNEL_TO_REMOVE = "Виберіть канал для видалення: ";
    private static final String REMOVE_ALL = "Видалити всі";
    private static final String REMOVE = "Видалити";
    private static final String CANCEL = "Відміна";

    private final MainScreen mainScreen;
    private final JDialog current;

    private JLabel text;

    private JButton removeAll, positiveButton, negativeButton;

    private JComboBox<String> channelsList = null;

    public DialogRemoveChannels(MainScreen mainScreen){
        super(mainScreen, REMOVE_CHANNEL, true);
        this.mainScreen = mainScreen;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        int selectedIndex = this.mainScreen.mainTable.getSelectedRow();
        if (selectedIndex == -1){
            this.text = new JLabel(CHOOSE_CHANNEL_TO_REMOVE);

            String[]channelsList = new String[this.mainScreen.channelsList.size()];
            for (int x=0;x<channelsList.length;x++) {
                channelsList[x] = this.mainScreen.channelsList.get(x).getName();
            }
            this.channelsList = new JComboBox<>(channelsList);
            this.channelsList.setBackground(Color.white);
        }else {
            String channelName = this.mainScreen.channelsList.get(selectedIndex).getName();
            this.text = new JLabel(REMOVE_CHANNEL + " \"" + channelName + "\"?");
        }

        this.removeAll = new DefaultButton(REMOVE_ALL);
        this.positiveButton = new DefaultButton(REMOVE);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.removeAll.addActionListener(clickRemoveAll);
        this.positiveButton.addActionListener(clickPositiveButton);
        this.negativeButton.addActionListener(clickNegativeButton);
    }

    private void build() {
        this.setSize(800, 120);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));
        this.setResizable(true);

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickRemoveAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    current.dispose();
                    new DialogRemoveAllChannels(mainScreen).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(current)) return;
            int selectedIndex = mainScreen.mainTable.getSelectedRow();
            final Channel channel;
            if (selectedIndex == -1){
                channel = mainScreen.channelsList.get(channelsList.getSelectedIndex());
            }else {
                channel = mainScreen.channelsList.get(selectedIndex);
            }
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    current.dispose();
                    ArrayList<Channel>channels = Application.context.channelsController.remove(channel);
                    if (Application.context.channelSorter.isOn()){
                        mainScreen.setChannelsList(Application.context.channelSorter.getCurrent());
                    }else {
                        mainScreen.setChannelsList(channels);
                    }
                }
            });
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
        }
    };

    private class MainPanel extends JPanel {

        protected MainPanel() {
            super(new GridBagLayout());
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(removeAll);
            buttonsPanel.add(negativeButton);
            buttonsPanel.add(positiveButton);

            this.add(text, new Cell(0,0));
            if (channelsList != null) {
                this.add(channelsList, new Cell(1,0));
                this.add(buttonsPanel, new Cell(1,1));
            }else {
                this.add(buttonsPanel, new Cell(0,1));
            }

        }

        private class Cell extends GridBagConstraints {

            protected Cell(int x, int y) {
                super();

                this.fill = HORIZONTAL;

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}