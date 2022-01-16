package ui.mainScreen.menu;

import ui.calculate.start.CalculateStartDialog;
import ui.channelInfo.DialogChannel;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuChannel extends JMenu {
    private static final String CHANNEL = "Канал";
    private static final String ADD = "Додати";
    private static final String CALCULATE = "Розрахувати";
    private static final String DETAILS = "Детальніше";
    private static final String REMOVE = "Видалити";

    private final MainScreen mainScreen;

    private JMenuItem buttonCalculate = null;
    private JMenuItem buttonAdd = null;
    private JMenuItem buttonDetails = null;
    private JMenuItem buttonRemove = null;

    public MenuChannel(MainScreen mainScreen){
        super(CHANNEL);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonAdd = new JMenuItem(ADD);
        this.buttonCalculate = new JMenuItem(CALCULATE);
        this.buttonDetails = new JMenuItem(DETAILS);
        this.buttonRemove = new JMenuItem(REMOVE);
    }

    private void setReactions() {
        this.buttonDetails.addActionListener(this.clickDetails);
        this.buttonAdd.addActionListener(this.clickAdd);
        this.buttonRemove.addActionListener(this.clickRemove);
        this.buttonCalculate.addActionListener(this.clickCalculate);
    }

    private void build() {
        this.add(this.buttonAdd);
        this.add(this.buttonRemove);
        this.addSeparator();
        this.add(this.buttonDetails);
        this.add(this.buttonCalculate);
    }

    private final ActionListener clickDetails = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final int channelIndex = mainScreen.mainTable.getSelectedRow();
            if (channelIndex!=-1) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new DialogChannel(mainScreen, mainScreen.channelsList.get(channelIndex)).setVisible(true);
                    }
                });
            }
        }
    };

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new DialogChannel(mainScreen, null).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickCalculate = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int index = mainScreen.mainTable.getSelectedRow();
                    if (index != -1){
                        new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
                    }
                }
            });
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainScreen.channelsList.size() > 0) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new DialogRemoveChannels(mainScreen).setVisible(true);
                    }
                });
            }
        }
    };
}