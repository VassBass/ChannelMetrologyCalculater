package ui.mainScreen.menu;

import model.Measurement;
import model.Model;
import settings.Settings;
import ui.calibratorsList.CalibratorsListDialog;
import ui.controlPointsValues.ControlPointsListDialog;
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
    private static final String CONTROL_POINTS = "Контрольні точки";

    private final MainScreen mainScreen;

    private JMenuItem buttonPersons;
    private JMenuItem buttonDepartments, buttonAreas, buttonProcesses, buttonInstallations;
    private JMenuItem buttonSensors, buttonControlPoints;
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
        this.buttonControlPoints = new JMenuItem(CONTROL_POINTS);
        this.buttonCalibrators = new JMenuItem(CALIBRATORS_LIST);

        this.buttonTemperature = new JMenuItem(Measurement.TEMPERATURE);
        this.buttonTemperature.setToolTipText(Settings.getSettingValue(Measurement.TEMPERATURE));
        this.buttonPressure = new JMenuItem(Measurement.PRESSURE);
        this.buttonPressure.setToolTipText(Settings.getSettingValue(Measurement.PRESSURE));
        this.buttonConsumption = new JMenuItem(Measurement.CONSUMPTION);
        this.buttonConsumption.setToolTipText(Settings.getSettingValue(Measurement.CONSUMPTION));
    }

    private void setReactions() {
        this.buttonDepartments.addActionListener(this.clickDepartments);
        this.buttonAreas.addActionListener(this.clickAreas);
        this.buttonProcesses.addActionListener(this.clickProcesses);
        this.buttonInstallations.addActionListener(this.clickInstallations);
        this.buttonPersons.addActionListener(this.clickButtonPersons);
        this.buttonSensors.addActionListener(this.clickSensors);
        this.buttonControlPoints.addActionListener(this.clickControlPoints);
        this.buttonTemperature.addActionListener(this.clickTemperature);
        this.buttonPressure.addActionListener(this.clickPressure);
        this.buttonConsumption.addActionListener(this.clickConsumption);
        this.buttonCalibrators.addActionListener(this.clickCalibrators);
    }

    private void build() {
        this.add(this.buttonSensors);
        this.add(this.buttonControlPoints);
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
                    new PathListsDialog(mainScreen, Model.DEPARTMENT).setVisible(true);
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
                    new PathListsDialog(mainScreen, Model.AREA).setVisible(true);
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
                    new PathListsDialog(mainScreen, Model.PROCESS).setVisible(true);
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
                    new PathListsDialog(mainScreen, Model.INSTALLATION).setVisible(true);
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

    private final ActionListener clickControlPoints = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ControlPointsListDialog(mainScreen).setVisible(true);
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
                    new MethodInfoDialog(mainScreen, Measurement.TEMPERATURE).setVisible(true);
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
                    new MethodInfoDialog(mainScreen, Measurement.PRESSURE).setVisible(true);
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
                    new MethodInfoDialog(mainScreen, Measurement.CONSUMPTION).setVisible(true);
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