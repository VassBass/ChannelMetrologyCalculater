package ui.importData.compareSensors;

import backgroundTasks.export.ExportData;
import backgroundTasks.tasks_for_import.SaveImportData;
import backgroundTasks.tasks_for_import.SaveImportedSensors;
import constants.Strings;
import converters.ConverterUI;
import support.*;
import ui.UI_Container;
import ui.importData.BreakImportDialog;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.importData.compareSensors.complexElements.CompareSensorsInfoPanel;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

public class CompareSensorsDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final JDialog current;

    private final ArrayList<Sensor>newSensorsList, oldSensorsList, importedSensorsList;
    private ArrayList<Channel>newChannelsList, importedChannelsList;
    private ArrayList<Worker>newPersonsList, importedPersonsList;
    private ArrayList<Calibrator>newCalibratorsList, importedCalibratorsList;
    private ArrayList<String>departments, areas, processes, installations;
    private final ArrayList<Integer[]>sensorsIndexes;
    private ArrayList<Integer[]> channelIndexes, personsIndexes, calibratorsIndexes;

    private int marker = 0;
    private int exportData;

    private CompareSensorsInfoPanel infoPanel;

    private JButton buttonChange, buttonSkip, buttonChangeAll, buttonSkipAll;

    public CompareSensorsDialog(final MainScreen mainScreen, int exportData,
                                final ArrayList<Sensor> newSensorsList, ArrayList<Sensor> importedSensorsList, ArrayList<Integer[]> sensorsIndexes){
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.exportData = exportData;
        this.current = this;

        this.newSensorsList = newSensorsList;
        this.oldSensorsList = Lists.sensors();
        this.importedSensorsList = importedSensorsList;
        this.sensorsIndexes = sensorsIndexes;

        if (importedSensorsList == null || sensorsIndexes == null){
            new SaveImportData(mainScreen, newSensorsList).execute();
        }else {
            this.createElements();
            this.setReactions();
            this.build();
            this.setVisible(true);
        }
    }

    public CompareSensorsDialog(final MainScreen mainScreen,
                                final ArrayList<Sensor>newSensorsList, ArrayList<Sensor>importedSensorsList, ArrayList<Integer[]>sensorsIndexes,
                                final ArrayList<Channel>newChannelsList, final ArrayList<Channel>importedChannelsList, ArrayList<Integer[]>channelsIndexes,
                                final ArrayList<Worker>newPersonsList, final ArrayList<Worker>importedPersonsList, final ArrayList<Integer[]>personsIndexes,
                                final ArrayList<Calibrator>newCalibratorList, final ArrayList<Calibrator>importedCalibratorsList, final ArrayList<Integer[]>calibratorsIndexes,
                                final ArrayList<String>departments, final ArrayList<String>areas, final ArrayList<String>processes, final ArrayList<String>installations){
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.current = this;

        this.newSensorsList = newSensorsList;
        this.oldSensorsList = Lists.sensors();
        this.importedSensorsList = importedSensorsList;
        this.sensorsIndexes = sensorsIndexes;

        this.newChannelsList = newChannelsList;
        this.importedChannelsList = importedChannelsList;
        this.channelIndexes = channelsIndexes;

        this.newPersonsList = newPersonsList;
        this.importedPersonsList = importedPersonsList;
        this.personsIndexes = personsIndexes;

        this.newCalibratorsList = newCalibratorList;
        this.importedCalibratorsList = importedCalibratorsList;
        this.calibratorsIndexes = calibratorsIndexes;

        this.departments = departments;
        this.areas = areas;
        this.processes = processes;
        this.installations = installations;

        if (sensorsIndexes.size() == 0){
            this.dispose();
                EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new CompareChannelsDialog(mainScreen, newSensorsList,
                            newChannelsList, importedChannelsList, channelIndexes,
                            newPersonsList, importedPersonsList, personsIndexes,
                            newCalibratorsList, importedCalibratorsList, calibratorsIndexes,
                            departments, areas, processes, installations);
                }
            });
        }else {
            this.createElements();
            this.setReactions();
            this.build();
            this.setVisible(true);
        }
    }

    private void next(){
        marker++;
        if (marker >= sensorsIndexes.size()) {
            this.dispose();
            if (this.exportData == ExportData.SENSORS){
                new SaveImportedSensors(this.mainScreen, this.newSensorsList).execute();
            }else {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new CompareChannelsDialog(mainScreen, newSensorsList,
                                newChannelsList, importedChannelsList, channelIndexes,
                                newPersonsList, importedPersonsList, personsIndexes,
                                newCalibratorsList, importedCalibratorsList, calibratorsIndexes,
                                departments, areas, processes, installations);
                    }
                });
            }
        }else {
            Integer[] index = this.sensorsIndexes.get(marker);
            int indexOld = index[0];
            int indexImport = index[1];
            this.infoPanel = new CompareSensorsInfoPanel(this.oldSensorsList.get(indexOld), this.importedSensorsList.get(indexImport));
            this.setContentPane(new MainPanel());
            this.setVisible(false);
            this.setVisible(true);
        }
    }

    @Override
    public void createElements() {
        Integer[] index = this.sensorsIndexes.get(marker);
        int indexOld = index[0];
        int indexImport = index[1];
        this.infoPanel = new CompareSensorsInfoPanel(this.oldSensorsList.get(indexOld), this.importedSensorsList.get(indexImport));

        this.buttonChange = new JButton(Strings._CHANGE);
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
            Integer[]i = sensorsIndexes.get(marker);
            newSensorsList.add(i[0], Objects.requireNonNull(oldSensorsList).get(i[0]));
            next();
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = sensorsIndexes.get(marker);
            newSensorsList.add(i[0], importedSensorsList.get(i[1]));
            next();
        }
    };

    private final ActionListener clickSkipAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<sensorsIndexes.size();marker++){
                Integer[]i = sensorsIndexes.get(marker);
                newSensorsList.add(i[0], Objects.requireNonNull(oldSensorsList).get(i[0]));
            }
            next();
        }
    };

    private final ActionListener clickChangeAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<sensorsIndexes.size();marker++){
                Integer[]i = sensorsIndexes.get(marker);
                newSensorsList.add(i[0], importedSensorsList.get(i[1]));
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
            this.add(buttonsPanel, new MainPanel.Cell(1, 0.05));
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
