package ui.importChannels.compareChannels;

import backgroundTasks.SaveChannelsToFile;
import support.Converter;
import support.Channel;
import constants.Strings;
import ui.UI_Container;
import ui.importChannels.compareChannels.complexElements.CompareChannels_infoPanel;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CompareChannelsDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog current;

    private final ArrayList<Channel>oldList;
    private final ArrayList<Channel>importedList;
    private final ArrayList<Channel>newList;
    private final ArrayList<Integer[]>indexes;

    private int marker = 0;

    private CompareChannels_infoPanel infoPanel;

    private JButton buttonChange, buttonSkip, buttonChangeAll, buttonSkipAll;

    public CompareChannelsDialog(MainScreen mainScreen, ArrayList<Channel>oldList, ArrayList<Channel>importedList, ArrayList<Channel>newList, ArrayList<Integer[]>indexes){
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.oldList = oldList;
        this.importedList = importedList;
        this.newList = newList;
        this.indexes = indexes;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void next(){
        marker++;
        if (marker >= indexes.size()) {
            this.dispose();
            JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.SUCCESS, JOptionPane.INFORMATION_MESSAGE);
            new SaveChannelsToFile(this.mainScreen, this.newList).execute();
        }else {
            Integer[] index = indexes.get(marker);
            int indexOld = index[0];
            int indexImport = index[1];
            this.infoPanel = new CompareChannels_infoPanel(this.oldList.get(indexOld), this.importedList.get(indexImport));
            this.setContentPane(new MainPanel());
            this.setVisible(false);
            this.setVisible(true);
        }
    }

    @Override
    public void createElements() {
        Integer[] index = indexes.get(marker);
        int indexOld = index[0];
        int indexImport = index[1];
        this.infoPanel = new CompareChannels_infoPanel(this.oldList.get(indexOld), this.importedList.get(indexImport));

        this.buttonChange = new JButton(Strings.CHANGE_CHANNEL);
        this.buttonChange.setBackground(Color.white);
        this.buttonChange.setFocusPainted(false);
        this.buttonChange.setContentAreaFilled(false);
        this.buttonChange.setOpaque(true);

        this.buttonSkip = new JButton(Strings.SKIP);
        this.buttonSkip.setBackground(Color.white);
        this.buttonSkip.setFocusPainted(false);
        this.buttonSkip.setContentAreaFilled(false);
        this.buttonSkip.setOpaque(true);

        this.buttonChangeAll = new JButton(Strings.CHANGE_ALL_CHANNELS);
        this.buttonChangeAll.setBackground(Color.white);
        this.buttonChangeAll.setFocusPainted(false);
        this.buttonChangeAll.setContentAreaFilled(false);
        this.buttonChangeAll.setOpaque(true);

        this.buttonSkipAll = new JButton(Strings.SKIP_ALL);
        this.buttonSkipAll.setBackground(Color.white);
        this.buttonSkipAll.setFocusPainted(false);
        this.buttonSkipAll.setContentAreaFilled(false);
        this.buttonSkipAll.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);

        this.buttonSkipAll.addChangeListener(this.pushButton);
        this.buttonSkip.addChangeListener(this.pushButton);
        this.buttonChange.addChangeListener(this.pushButton);
        this.buttonChangeAll.addChangeListener(this.pushButton);

        this.buttonChangeAll.addActionListener(this.clickChangeAll);
        this.buttonSkip.addActionListener(this.clickSkip);
        this.buttonChange.addActionListener(this.clickChange);
        this.buttonSkipAll.addActionListener(this.clickSkipAll);
    }

    @Override
    public void build() {
        this.setSize(800,700);
        this.setLocation(Converter.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

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

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            new BreakImportDialog(mainScreen, current).setVisible(true);
        }
    };

    private final ActionListener clickSkip = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = indexes.get(marker);
            newList.add(i[0], oldList.get(i[0]));
            next();
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = indexes.get(marker);
            newList.add(i[0], importedList.get(i[1]));
            next();
        }
    };

    private final ActionListener clickSkipAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<indexes.size();marker++){
                Integer[]i = indexes.get(marker);
                newList.add(i[0], oldList.get(i[0]));
            }
            next();
        }
    };

    private final ActionListener clickChangeAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<indexes.size();marker++){
                Integer[]i = indexes.get(marker);
                newList.add(i[0], importedList.get(i[1]));
            }
            next();
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            JScrollPane scroll = new JScrollPane(infoPanel);
            scroll.setPreferredSize(new Dimension(800,650));
            this.add(scroll, new Cell(0, 0.95));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonSkip);
            buttonsPanel.add(buttonSkipAll);
            buttonsPanel.add(buttonChangeAll);
            buttonsPanel.add(buttonChange);
            this.add(buttonsPanel, new Cell(1, 0.05));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int y, double weighty){
                super();

                this.fill = BOTH;
                this.weightx = 1.0;
                this.weighty = weighty;

                this.gridx = 0;
                this.gridy = y;
            }
        }
    }
}
