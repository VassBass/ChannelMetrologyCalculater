package ui.removeChannels;

import backgroundTasks.RemoveChannels;
import converters.ConverterUI;
import support.Channel;
import constants.Strings;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogRemoveChannels extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog current;

    private JLabel text;

    private JButton removeAll, positiveButton, negativeButton;

    private JComboBox<String> channelsList = null;

    public DialogRemoveChannels(MainScreen mainScreen){
        super(mainScreen, Strings.REMOVE_CHANNEL, true);
        this.mainScreen = mainScreen;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        int selectedIndex = this.mainScreen.mainTable.getSelectedRow();
        if (selectedIndex == -1){
            this.text = new JLabel(Strings.CHOOSE_CHANNEL_TO_REMOVE.concat(": "));

            String[]channelsList = new String[this.mainScreen.channelsList.size()];
            for (int x=0;x<channelsList.length;x++) {
                channelsList[x] = this.mainScreen.channelsList.get(x).getName();
            }
            this.channelsList = new JComboBox<>(channelsList);
            this.channelsList.setBackground(Color.white);
        }else {
            String channelName = this.mainScreen.channelsList.get(selectedIndex).getName();
            this.text = new JLabel(Strings.REMOVE_CHANNEL + " \"" + channelName + "\"?");
        }

        this.removeAll = new JButton(Strings.REMOVE_ALL);
        this.removeAll.setBackground(Color.white);
        this.removeAll.setFocusPainted(false);
        this.removeAll.setContentAreaFilled(false);
        this.removeAll.setOpaque(true);

        this.positiveButton = new JButton(Strings.REMOVE);
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

        this.removeAll.addChangeListener(pushButton);
        this.positiveButton.addChangeListener(pushButton);
        this.negativeButton.addChangeListener(pushButton);

        this.removeAll.addActionListener(clickRemoveAll);
        this.positiveButton.addActionListener(clickPositiveButton);
        this.negativeButton.addActionListener(clickNegativeButton);
    }

    @Override
    public void build() {
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
                    new RemoveChannels(mainScreen, channel).execute();
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

    private class MainPanel extends JPanel {

        private static final long serialVersionUID = 1L;

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

            private static final long serialVersionUID = 1L;

            protected Cell(int x, int y) {
                super();

                this.fill = HORIZONTAL;

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}
