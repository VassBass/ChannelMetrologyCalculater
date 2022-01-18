package ui.importData.compareSensors;

import application.Application;
import backgroundTasks.data_import.SaveImportData;
import backgroundTasks.data_import.SaveImportedSensors;
import converters.ConverterUI;
import model.*;
import ui.importData.BreakImportDialog;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.importData.compareSensors.complexElements.CompareSensorsInfoPanel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CompareSensorsDialog extends JDialog {
    private static final String IMPORT = "Імпорт";
    private static final String CHANGE = "Замінити";
    private static final String SKIP = "Пропустити";
    private static final String CHANGE_ALL = "Замінити все";
    private static final String SKIP_ALL = "Пропустити все";

    private final MainScreen mainScreen;
    private final JDialog current;

    private final ArrayList<Sensor> newSensorsList, oldSensorsList, importedSensorsList;
    private ArrayList<Channel>newChannelsList, importedChannelsList;
    private ArrayList<Calibrator>newCalibratorsList, importedCalibratorsList;

    private ArrayList<Worker>persons;
    private ArrayList<String>departments, areas, processes, installations;

    private final ArrayList<Integer[]>sensorsIndexes;
    private ArrayList<Integer[]> channelIndexes, calibratorsIndexes;

    private int marker = 0;
    private final Model exportData;

    private CompareSensorsInfoPanel infoPanel;

    private JButton buttonChange, buttonSkip, buttonChangeAll, buttonSkipAll;

    public CompareSensorsDialog(final MainScreen mainScreen, Model exportData,
                                final ArrayList<Sensor> newSensorsList, ArrayList<Sensor> importedSensorsList, ArrayList<Integer[]> sensorsIndexes){
        super(mainScreen, IMPORT, true);
        this.mainScreen = mainScreen;
        this.exportData = exportData;
        this.current = this;

        this.newSensorsList = newSensorsList;
        this.oldSensorsList = Application.context.sensorsController.getAll();
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

    public CompareSensorsDialog(final MainScreen mainScreen, final Model exportData,
                                final ArrayList<Sensor>newSensorsList, ArrayList<Sensor>importedSensorsList, ArrayList<Integer[]>sensorsIndexes,
                                final ArrayList<Channel>newChannelsList, final ArrayList<Channel>importedChannelsList, ArrayList<Integer[]>channelsIndexes,
                                ArrayList<Calibrator>newCalibratorList, final ArrayList<Calibrator>importedCalibratorsList, final ArrayList<Integer[]>calibratorsIndexes,
                                final ArrayList<Worker>persons,
                                final ArrayList<String>departments, final ArrayList<String>areas, final ArrayList<String>processes, final ArrayList<String>installations){
        super(mainScreen, IMPORT, true);
        this.mainScreen = mainScreen;
        this.exportData = exportData;
        this.current = this;

        this.newSensorsList = newSensorsList;
        this.oldSensorsList = Application.context.sensorsController.getAll();
        this.importedSensorsList = importedSensorsList;
        this.sensorsIndexes = sensorsIndexes;

        this.newChannelsList = newChannelsList;
        this.importedChannelsList = importedChannelsList;
        this.channelIndexes = channelsIndexes;

        this.newCalibratorsList = newCalibratorList;
        this.importedCalibratorsList = importedCalibratorsList;
        this.calibratorsIndexes = calibratorsIndexes;

        this.persons = persons;
        this.departments = departments;
        this.areas = areas;
        this.processes = processes;
        this.installations = installations;

        if (importedSensorsList == null || sensorsIndexes == null){
            this.dispose();
            new CompareChannelsDialog(mainScreen, exportData, newSensorsList,
                    newChannelsList, importedChannelsList, channelIndexes,
                    newCalibratorsList, importedCalibratorsList, calibratorsIndexes,
                    persons, departments, areas, processes, installations);
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
            if (this.exportData == Model.SENSOR){
                new SaveImportedSensors(this.mainScreen, this.newSensorsList).execute();
            }else {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new CompareChannelsDialog(mainScreen, exportData, newSensorsList,
                                newChannelsList, importedChannelsList, channelIndexes,
                                newCalibratorsList, importedCalibratorsList, calibratorsIndexes,
                                persons, departments, areas, processes, installations);
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

    private void createElements() {
        Integer[] index = this.sensorsIndexes.get(marker);
        int indexOld = index[0];
        int indexImport = index[1];
        this.infoPanel = new CompareSensorsInfoPanel(this.oldSensorsList.get(indexOld), this.importedSensorsList.get(indexImport));

        this.buttonChange = new DefaultButton(CHANGE);
        this.buttonSkip = new DefaultButton(SKIP);
        this.buttonChangeAll = new DefaultButton(CHANGE_ALL);
        this.buttonSkipAll = new DefaultButton(SKIP_ALL);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);

        this.buttonChangeAll.addActionListener(this.clickChangeAll);
        this.buttonSkip.addActionListener(this.clickSkip);
        this.buttonChange.addActionListener(this.clickChange);
        this.buttonSkipAll.addActionListener(this.clickSkipAll);
    }

    private void build() {
        this.setSize(800,700);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

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
            newSensorsList.add(i[0], oldSensorsList.get(i[0]));
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
                newSensorsList.add(i[0], oldSensorsList.get(i[0]));
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