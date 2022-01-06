package ui.importData.compareCalibrators;

import backgroundTasks.export.ExportData;
import backgroundTasks.tasks_for_import.SaveImportData;
import backgroundTasks.tasks_for_import.SaveImportedCalibrators;
import constants.Strings;
import converters.ConverterUI;
import model.Calibrator;
import model.Channel;
import model.Sensor;
import model.Worker;
import support.*;
import ui.UI_Container;
import ui.importData.BreakImportDialog;
import ui.importData.compareCalibrators.complexElements.CompareCalibratorsInfoPanel;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

public class CompareCalibratorsDialog /*extends JDialog implements UI_Container*/ {/*
    private final MainScreen mainScreen;
    private final JDialog current;

    private ArrayList<Sensor> sensors;
    private ArrayList<Channel>channels;
    private ArrayList<Worker>persons;
    private final ArrayList<Calibrator>newCalibratorsList, oldCalibratorsList, importedCalibratorsList;
    private ArrayList<String>departments, areas, processes, installations;
    private final ArrayList<Integer[]>calibratorsIndexes;

    private int marker = 0;
    private final int exportData;

    private CompareCalibratorsInfoPanel infoPanel;

    private JButton buttonChange, buttonSkip, buttonChangeAll, buttonSkipAll;

    public CompareCalibratorsDialog(MainScreen mainScreen, int exportData,
                                    ArrayList<Calibrator>newCalibratorsList, ArrayList<Calibrator>importedCalibratorsList, ArrayList<Integer[]>calibratorsIndexes){
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.current = this;
        this.exportData = exportData;

        this.newCalibratorsList = newCalibratorsList;
        this.oldCalibratorsList = Lists.calibrators();
        this.importedCalibratorsList = importedCalibratorsList;
        this.calibratorsIndexes = calibratorsIndexes;

        if (importedCalibratorsList == null || calibratorsIndexes == null){
            new SaveImportedCalibrators(mainScreen, newCalibratorsList).execute();
        }else {
            this.createElements();
            this.setReactions();
            this.build();
            this.setVisible(true);
        }
    }

    public CompareCalibratorsDialog(MainScreen mainScreen, int exportData,
                                    ArrayList<Sensor>sensors, ArrayList<Channel>channels,
                                    ArrayList<Calibrator>newCalibratorsList, ArrayList<Calibrator>importedCalibratorsList, ArrayList<Integer[]>calibratorsIndexes,
                                    ArrayList<Worker>persons,
                                    ArrayList<String>departments, ArrayList<String>areas, ArrayList<String>processes, ArrayList<String>installations){
        super(mainScreen, Strings.IMPORT, true);
        this.mainScreen = mainScreen;
        this.exportData = exportData;
        this.current = this;
        this.sensors = sensors;
        this.channels = channels;
        this.persons = persons;

        this.newCalibratorsList = newCalibratorsList;
        this.oldCalibratorsList = Lists.calibrators();
        this.importedCalibratorsList = importedCalibratorsList;
        this.calibratorsIndexes = calibratorsIndexes;

        this.departments = departments;
        this.areas = areas;
        this.processes = processes;
        this.installations = installations;

        if (importedCalibratorsList == null || calibratorsIndexes == null){
            new SaveImportData(this.mainScreen, this.sensors, this.channels, this.persons, newCalibratorsList,
                    this.departments, this.areas, this.processes, this.installations).execute();
        }else {
            this.createElements();
            this.setReactions();
            this.build();
            this.setVisible(true);
        }
    }

    private void next(){
        marker++;
        if (marker >= calibratorsIndexes.size()) {
            this.dispose();
            if (this.exportData == ExportData.CALIBRATORS){
                new SaveImportedCalibrators(this.mainScreen, this.newCalibratorsList).execute();
            }else {
                new SaveImportData(this.mainScreen, this.sensors, this.channels, this.persons, this.newCalibratorsList,
                        this.departments, this.areas, this.processes, this.installations).execute();
            }
        }else {
            Integer[] index = calibratorsIndexes.get(marker);
            int indexOld = index[0];
            int indexImport = index[1];
            this.infoPanel = new CompareCalibratorsInfoPanel(this.oldCalibratorsList.get(indexOld), this.importedCalibratorsList.get(indexImport));
            this.setContentPane(new MainPanel());
            this.setVisible(false);
            this.setVisible(true);
        }
    }

    @Override
    public void createElements() {
        Integer[] index = calibratorsIndexes.get(marker);
        int indexOld = index[0];
        int indexImport = index[1];
        this.infoPanel = new CompareCalibratorsInfoPanel(this.oldCalibratorsList.get(indexOld), this.importedCalibratorsList.get(indexImport));

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
        this.setSize(800,350);
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
            Integer[]i = calibratorsIndexes.get(marker);
            newCalibratorsList.add(i[0], Objects.requireNonNull(oldCalibratorsList).get(i[0]));
            next();
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[]i = calibratorsIndexes.get(marker);
            newCalibratorsList.add(i[0], importedCalibratorsList.get(i[1]));
            next();
        }
    };

    private final ActionListener clickSkipAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<calibratorsIndexes.size();marker++){
                Integer[]i = calibratorsIndexes.get(marker);
                newCalibratorsList.add(i[0], Objects.requireNonNull(oldCalibratorsList).get(i[0]));
            }
            next();
        }
    };

    private final ActionListener clickChangeAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (;marker<calibratorsIndexes.size();marker++){
                Integer[]i = calibratorsIndexes.get(marker);
                newCalibratorsList.add(i[0], importedCalibratorsList.get(i[1]));
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
    }*/
}
