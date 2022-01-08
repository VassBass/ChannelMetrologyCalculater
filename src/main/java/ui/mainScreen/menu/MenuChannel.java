package ui.mainScreen.menu;

import constants.Strings;
import ui.mainScreen.MainScreen;
import ui.removeChannels.DialogRemoveChannels;
import ui.searchChannel.DialogSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuChannel extends JMenu implements UI_Container {
    private final MainScreen mainScreen;

    private JMenuItem buttonCalculate = null;
    private JMenuItem buttonAdd = null;
    private JMenuItem buttonDetails = null;
    private JMenuItem buttonRemove = null;
    private JMenuItem buttonSearch = null;

    public MenuChannel(MainScreen mainScreen){
        super(Strings.CHANNEL);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonAdd = new JMenuItem(Strings.ADD);
        this.buttonCalculate = new JMenuItem(Strings.CALCULATE);
        this.buttonDetails = new JMenuItem(Strings.DETAILS);
        this.buttonRemove = new JMenuItem(Strings.REMOVE);
        this.buttonSearch = new JMenuItem(Strings.SEARCH);
    }

    @Override
    public void setReactions() {
        this.buttonDetails.addActionListener(this.clickDetails);
        this.buttonAdd.addActionListener(this.clickAdd);
        this.buttonRemove.addActionListener(this.clickRemove);
        this.buttonCalculate.addActionListener(this.clickCalculate);
        this.buttonSearch.addActionListener(this.clickSearch);
    }

    @Override
    public void build() {
        this.add(this.buttonAdd);
        this.add(this.buttonRemove);
        this.addSeparator();
        this.add(this.buttonDetails);
        this.add(this.buttonCalculate);
        this.addSeparator();
        this.add(this.buttonSearch);
    }

    private final ActionListener clickDetails = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final int channelIndex = mainScreen.mainTable.getSelectedRow();
            if (channelIndex!=-1) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //new DialogChannel(mainScreen, mainScreen.channelsList.get(channelIndex)).setVisible(true);
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
                    //new DialogChannel(mainScreen, null).setVisible(true);
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
                        //new CalculateStartDialog(mainScreen, mainScreen.channelsList.get(index), null).setVisible(true);
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

    private final ActionListener clickSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonSearch.getText().equals(Strings.SEARCH)){
                new DialogSearch(mainScreen).setVisible(true);
            }else {
                //mainScreen.update(Lists.channels(), false, null, null);
            }
        }
    };
}
