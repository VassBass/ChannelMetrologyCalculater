package ui.importData.compareChannels;

import converters.ConverterUI;
import support.Channel;
import constants.Strings;
import support.Lists;
import support.Sensor;
import support.Worker;
import ui.UI_Container;
import ui.importData.BreakImportDialog;
import ui.importData.compareChannels.complexElements.CompareChannels_infoPanel;
import ui.importData.comparePersons.ComparePersonsDialog;
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

    private final ArrayList<Sensor>sensors;
    private final ArrayList<Channel>newChannelsList, oldChannelsList, importedChannelsList;
    private final ArrayList<Worker>newPersonsList, importedPersonsList;
    private final ArrayList<String>departments, areas, processes, installations;
    private final ArrayList<Integer[]>channelIndexes, personsIndexes;

    private int marker = 0;

    private CompareChannels_infoPanel infoPanel;

    private JButton buttonChange, buttonSkip, buttonChangeAll, buttonSkipAll;

    public CompareChannelsDialog(final MainScreen mainScreen, final ArrayList<Sensor>sensors,
                                 final ArrayList<Channel>newChannelsList, ArrayList<Channel>importedChannelsList, ArrayList<Integer[]>channelIndexes,
                                 final ArrayList<Worker>newPersonsList, final ArrayList<Worker>importedPersonsList, final ArrayList<Integer[]>personsIndexes,
                                 final ArrayList<String>departments, final ArrayList<String>areas, final ArrayList<String>processes, final ArrayList<String>installations) {
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.sensors = sensors;
        this.current = this;

        this.newChannelsList = newChannelsList;
        this.oldChannelsList = Lists.channels();
        this.importedChannelsList = importedChannelsList;
        this.channelIndexes = channelIndexes;

        this.newPersonsList = newPersonsList;
        this.importedPersonsList = importedPersonsList;
        this.personsIndexes = personsIndexes;

        this.departments = departments;
        this.areas = areas;
        this.processes = processes;
        this.installations = installations;

        if (channelIndexes.size() == 0) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ComparePersonsDialog(mainScreen, sensors, newChannelsList, newPersonsList, importedPersonsList, personsIndexes,
                            departments, areas, processes, installations);
                }
            });
        } else {
            this.createElements();
            this.setReactions();
            this.build();
            this.setVisible(true);
        }
    }

    private void next(){
        marker++;
        if (marker >= channelIndexes.size()) {
            this.dispose();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ComparePersonsDialog(mainScreen, sensors, newChannelsList, newPersonsList, importedPersonsList, personsIndexes,
                            departments, areas, processes, installations);
                }
            });
        }else {
            Integer[] index = channelIndexes.get(marker);
            int indexOld = index[0];
            int indexImport = index[1];
            this.infoPanel = new CompareChannels_infoPanel(this.oldChannelsList.get(indexOld), this.importedChannelsList.get(indexImport));
            this.setContentPane(new MainPanel());
            this.setVisible(false);
            this.setVisible(true);
        }
    }

    @Override
    public void createElements() {
        Integer[] index = channelIndexes.get(marker);
        int indexOld = index[0];
        int indexImport = index[1];
        this.infoPanel = new CompareChannels_infoPanel(this.oldChannelsList.get(indexOld), this.importedChannelsList.get(indexImport));

        this.buttonChange = new JButton(Strings.CHANGE);
        this.buttonChange.setBackground(Color.white);
        this.buttonChange.setFocusPainted(false);
        this.buttonChange.setContentAreaFilled(false);
        this.buttonChange.setOpaque(true);

        this.buttonSkip = new JButton(Strings.SKIP);
        this.buttonSkip.setBackground(Color.white);
        this.buttonSkip.setFocusPainted(false);
        this.buttonSkip.setContentAreaFilled(false);
        this.buttonSkip.setOpaque(true);

        this.buttonChangeAll = new JButton(Strings.CHANGE_ALL);
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
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

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
            Integer[]i = channelIndexes.get(marker);
            newChannelsList.add(i[0], oldChannelsList.get(i[0]));
            next();
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = channelIndexes.get(marker);
            newChannelsList.add(i[0], importedChannelsList.get(i[1]));
            next();
        }
    };

    private final ActionListener clickSkipAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<channelIndexes.size();marker++){
                Integer[]i = channelIndexes.get(marker);
                newChannelsList.add(i[0], oldChannelsList.get(i[0]));
            }
            next();
        }
    };

    private final ActionListener clickChangeAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<channelIndexes.size();marker++){
                Integer[]i = channelIndexes.get(marker);
                newChannelsList.add(i[0], importedChannelsList.get(i[1]));
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
