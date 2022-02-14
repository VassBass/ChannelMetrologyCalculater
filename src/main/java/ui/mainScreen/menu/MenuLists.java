package ui.mainScreen.menu;

import constants.MeasurementConstants;
import settings.Settings;
import ui.calibratorsList.CalibratorsListDialog;
import ui.mainScreen.MainScreen;
import ui.methodInfo.MethodInfoDialog;
import ui.pathLists.PathListsDialog;
import ui.personsList.PersonsListDialog;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuLists extends JMenu {
    private static final String LISTS = "Списки";
    private static final String DEPARTMENTS_LIST = "Список цехів";
    private static final String AREAS_LIST = "Список ділянок";
    private static final String PROCESSES_LIST = "Список ліній, секцій і т.п";
    private static final String INSTALLATIONS_LIST = "Список установок";
    private static final String WORKERS = "Робітники";
    private static final String SENSORS_LIST = "Список первинних вимірювальних пристроїв";
    private static final String CALIBRATORS_LIST = "Список калібраторів";
    private static final String METHODS = "Методи розрахунку";

    private final MainScreen mainScreen;

    private JMenuItem buttonPersons;
    private JMenuItem buttonDepartments, buttonAreas, buttonProcesses, buttonInstallations;
    private JMenuItem buttonSensors;
    private JMenuItem buttonTemperature, buttonPressure, buttonConsumption;
    private JMenuItem buttonCalibrators;

    public MenuLists(MainScreen mainScreen){
        super(LISTS);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonDepartments = new JMenuItem(DEPARTMENTS_LIST);
        this.buttonAreas = new JMenuItem(AREAS_LIST);
        this.buttonProcesses = new JMenuItem(PROCESSES_LIST);
        this.buttonInstallations = new JMenuItem(INSTALLATIONS_LIST);
        this.buttonPersons = new JMenuItem(WORKERS);
        this.buttonSensors = new JMenuItem(SENSORS_LIST);
        this.buttonCalibrators = new JMenuItem(CALIBRATORS_LIST);

        this.buttonTemperature = new JMenuItem(MeasurementConstants.TEMPERATURE.getValue());
        this.buttonTemperature.setToolTipText(Settings.getSettingValue(MeasurementConstants.TEMPERATURE.getValue()));
        this.buttonPressure = new JMenuItem(MeasurementConstants.PRESSURE.getValue());
        this.buttonPressure.setToolTipText(Settings.getSettingValue(MeasurementConstants.PRESSURE.getValue()));
        this.buttonConsumption = new JMenuItem(MeasurementConstants.CONSUMPTION.getValue());
        this.buttonConsumption.setToolTipText(Settings.getSettingValue(MeasurementConstants.CONSUMPTION.getValue()));
    }

    private void setReactions() {
        this.buttonDepartments.addActionListener(this.clickDepartments);
        this.buttonAreas.addActionListener(this.clickAreas);
        this.buttonProcesses.addActionListener(this.clickProcesses);
        this.buttonInstallations.addActionListener(this.clickInstallations);
        this.buttonPersons.addActionListener(this.clickButtonPersons);
        this.buttonSensors.addActionListener(this.clickSensors);
        this.buttonTemperature.addActionListener(this.clickTemperature);
        this.buttonPressure.addActionListener(this.clickPressure);
        this.buttonConsumption.addActionListener(this.clickConsumption);
        this.buttonCalibrators.addActionListener(this.clickCalibrators);
    }

    private void build() {
        this.add(this.buttonSensors);
        this.addSeparator();
        this.add(this.buttonCalibrators);
        this.addSeparator();
        this.add(this.buttonDepartments);
        this.add(this.buttonAreas);
        this.add(this.buttonProcesses);
        this.add(this.buttonInstallations);
        this.addSeparator();
        this.add(this.buttonPersons);
        this.addSeparator();
        JMenu methods = new JMenu(METHODS);
        methods.add(this.buttonTemperature);
        methods.add(this.buttonPressure);
        methods.add(this.buttonConsumption);
        this.add(methods);
    }

    private final ActionListener clickButtonPersons = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PersonsListDialog(mainScreen).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickDepartments = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PathListsDialog(mainScreen, DEPARTMENTS_LIST).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickAreas = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PathListsDialog(mainScreen, AREAS_LIST).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickProcesses = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PathListsDialog(mainScreen, PROCESSES_LIST).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickInstallations = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PathListsDialog(mainScreen, INSTALLATIONS_LIST).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickSensors = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SensorsListDialog(mainScreen).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickTemperature = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MethodInfoDialog(mainScreen, MeasurementConstants.TEMPERATURE).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickPressure = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MethodInfoDialog(mainScreen, MeasurementConstants.PRESSURE).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickConsumption = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MethodInfoDialog(mainScreen, MeasurementConstants.CONSUMPTION).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickCalibrators = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new CalibratorsListDialog(mainScreen).setVisible(true);
                }
            });
        }
    };
}