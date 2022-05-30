package ui.mainScreen.menu;

import model.Measurement;
import model.Model;
import settings.Settings;
import ui.calibratorsList.CalibratorsListDialog;
import ui.controlPointsValues.ControlPointsListDialog;
import ui.mainScreen.MainScreen;
import ui.measurementsList.MeasurementsListDialog;
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
    private static final String MEASUREMENTS_LIST = "Список вимірювальних величин";

    private final MainScreen mainScreen;

    private JMenuItem btn_persons;
    private JMenuItem btn_departments, btn_areas, btn_processes, btn_installations;
    private JMenuItem btn_sensors, btn_controlPoints;
    private JMenuItem btn_measurements, btn_temperature, btn_pressure, btn_consumption;
    private JMenuItem btn_calibrators;


    public MenuLists(MainScreen mainScreen){
        super(LISTS);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.btn_departments = new JMenuItem(DEPARTMENTS_LIST);
        this.btn_areas = new JMenuItem(AREAS_LIST);
        this.btn_processes = new JMenuItem(PROCESSES_LIST);
        this.btn_installations = new JMenuItem(INSTALLATIONS_LIST);
        this.btn_persons = new JMenuItem(WORKERS);
        this.btn_sensors = new JMenuItem(SENSORS_LIST);
        this.btn_controlPoints = new JMenuItem(CONTROL_POINTS);
        this.btn_calibrators = new JMenuItem(CALIBRATORS_LIST);
        this.btn_measurements = new JMenuItem(MEASUREMENTS_LIST);

        this.btn_temperature = new JMenuItem(Measurement.TEMPERATURE);
        this.btn_temperature.setToolTipText(Settings.getSettingValue(Measurement.TEMPERATURE));
        this.btn_pressure = new JMenuItem(Measurement.PRESSURE);
        this.btn_pressure.setToolTipText(Settings.getSettingValue(Measurement.PRESSURE));
        this.btn_consumption = new JMenuItem(Measurement.CONSUMPTION);
        this.btn_consumption.setToolTipText(Settings.getSettingValue(Measurement.CONSUMPTION));
    }

    private void setReactions() {
        this.btn_departments.addActionListener(this.clickDepartments);
        this.btn_areas.addActionListener(this.clickAreas);
        this.btn_processes.addActionListener(this.clickProcesses);
        this.btn_installations.addActionListener(this.clickInstallations);
        this.btn_persons.addActionListener(this.clickPersons);
        this.btn_sensors.addActionListener(this.clickSensors);
        this.btn_controlPoints.addActionListener(this.clickControlPoints);
        this.btn_temperature.addActionListener(this.clickTemperature);
        this.btn_pressure.addActionListener(this.clickPressure);
        this.btn_consumption.addActionListener(this.clickConsumption);
        this.btn_calibrators.addActionListener(this.clickCalibrators);
        this.btn_measurements.addActionListener(this.clickMeasurements);
    }

    private void build() {
        this.add(this.btn_sensors);
        this.add(this.btn_controlPoints);
        this.addSeparator();
        this.add(this.btn_calibrators);
        this.addSeparator();
        this.add(this.btn_departments);
        this.add(this.btn_areas);
        this.add(this.btn_processes);
        this.add(this.btn_installations);
        this.addSeparator();
        this.add(this.btn_persons);
        this.addSeparator();
        this.add(this.btn_measurements);
        JMenu methods = new JMenu(METHODS);
        methods.add(this.btn_temperature);
        methods.add(this.btn_pressure);
        methods.add(this.btn_consumption);
        this.add(methods);
    }

    private final ActionListener clickPersons = new ActionListener() {
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

    private final ActionListener clickMeasurements = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MeasurementsListDialog(mainScreen).setVisible(true);
                }
            });
        }
    };
}