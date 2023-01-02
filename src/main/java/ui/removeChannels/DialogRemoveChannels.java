package ui.removeChannels;

import model.Channel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static ui.UI_Constants.POINT_CENTER;

public class DialogRemoveChannels extends JDialog {
    private static final String REMOVE_CHANNEL = "Видалити канал";
    private static final String CHOOSE_CHANNEL_TO_REMOVE = "Виберіть канал для видалення: ";
    private static final String REMOVE_ALL = "Видалити всі (Shift + R)";
    private static final String REMOVE = "Видалити (R)";
    private static final String CANCEL = "Відміна (Q)";

    private final MainScreen mainScreen;

    private JLabel text;

    private JButton removeAll, positiveButton, negativeButton;

    private JComboBox<String> channelsList = null;

    public DialogRemoveChannels(MainScreen mainScreen){
        super(mainScreen, REMOVE_CHANNEL, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        int selectedIndex = this.mainScreen.mainTable.getSelectedRow();
//        if (selectedIndex == -1){
//            String[]channelsList = new String[this.mainScreen.channelsList.size()];
//            for (int x=0;x<channelsList.length;x++) {
//                channelsList[x] = this.mainScreen.channelsList.get(x).getName();
//            }
//            this.channelsList = new JComboBox<>(channelsList);
//            this.channelsList.setBackground(Color.white);
//            TitledBorder border = BorderFactory.createTitledBorder(CHOOSE_CHANNEL_TO_REMOVE);
//            border.setTitleJustification(TitledBorder.CENTER);
//            this.channelsList.setBorder(border);
//        }else {
//            String channelName = this.mainScreen.channelsList.get(selectedIndex).getName();
//            this.text = new JLabel(REMOVE_CHANNEL + " \"" + channelName + "\"?");
//        }

        this.removeAll = new DefaultButton(REMOVE_ALL);
        this.positiveButton = new DefaultButton(REMOVE);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        if (removeAll != null) {
            removeAll.addActionListener(clickRemoveAll);
            removeAll.addKeyListener(keyListener);
        }

        if (positiveButton != null) {
            positiveButton.addActionListener(clickPositiveButton);
            positiveButton.addKeyListener(keyListener);
        }
        if (negativeButton != null) {
            negativeButton.addActionListener(clickNegativeButton);
            negativeButton.addKeyListener(keyListener);
        }

        if (channelsList != null) channelsList.addKeyListener(keyListener);
    }

    private void build() {
        this.setSize(800, 150);
        this.setLocation(POINT_CENTER(this.mainScreen, this));
        this.setResizable(true);

        this.setContentPane(new MainPanel());
    }

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_Q:
                    negativeButton.doClick();
                    break;
                case KeyEvent.VK_R:
                    if (e.isShiftDown()){
                        removeAll.doClick();
                    }else {
                        positiveButton.doClick();
                    }
                    break;
            }
        }
    };

    private final ActionListener clickRemoveAll = e -> {
//            EventQueue.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    setVisible(false);
//                    String message = "Ви впевнені що хочете видалити всі канали? Загальна кількість: "
//                            + mainScreen.channelsList.size();
//                    int result = JOptionPane.showConfirmDialog(DialogRemoveChannels.this,
//                            message, REMOVE_ALL, JOptionPane.OK_CANCEL_OPTION);
//                    if (result == 0){
//                        if (Application.isBusy(DialogRemoveChannels.this)) return;
//                        dispose();
//                        mainScreen.setChannelsList(new ArrayList<Channel>());
//                        Application.context.channelService.clear();
//                        if (Application.context.channelSorter.isOn()) {
//                            Application.context.channelSorter.setOff();
//                            mainScreen.searchPanel.buttonSearch.doClick();
//                        }
//                    }else {
//                        setVisible(true);
//                    }
//                }
//            });
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = mainScreen.mainTable.getSelectedRow();
            final Channel channel;
//            if (selectedIndex == -1){
//                channel = mainScreen.channelsList.get(channelsList.getSelectedIndex());
//            }else {
//                channel = mainScreen.channelsList.get(selectedIndex);
//            }
//            EventQueue.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    DialogRemoveChannels.this.dispose();
//                    Application.context.channelService.remove(channel);
//                    if (Application.context.channelSorter.isOn()){
//                        mainScreen.setChannelsList(Application.context.channelSorter.getCurrent());
//                    }else {
//                        mainScreen.setChannelsList(new ArrayList<>(Application.context.channelService.getAll()));
//                    }
//                }
//            });
        }
    };

    private final ActionListener clickNegativeButton = e -> DialogRemoveChannels.this.dispose();

    private class MainPanel extends JPanel {

        protected MainPanel() {
            super(new GridBagLayout());
            this.setBackground(Color.WHITE);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setBackground(Color.WHITE);
            buttonsPanel.add(removeAll);
            buttonsPanel.add(negativeButton);
            buttonsPanel.add(positiveButton);

            if (channelsList != null) {
                this.add(channelsList, new Cell(0,0));
            }else {
                this.add(text, new Cell(0,0));
            }
            this.add(buttonsPanel, new Cell(0,1));
        }

        private class Cell extends GridBagConstraints {

            protected Cell(int x, int y) {
                super();

                this.fill = HORIZONTAL;
                this.insets = new Insets(5,0,5,0);

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}