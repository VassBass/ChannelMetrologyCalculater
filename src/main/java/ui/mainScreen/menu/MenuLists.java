package ui.mainScreen.menu;

import constants.MeasurementConstants;
import constants.Strings;
import support.Settings;
import ui.UI_Container;
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

public class MenuLists extends JMenu implements UI_Container {
    private final MainScreen mainScreen;

    private JMenuItem buttonPersons;
    private JMenuItem buttonDepartments, buttonAreas, buttonProcesses, buttonInstallations;
    private JMenuItem buttonSensors;
    private JMenuItem buttonTemperature, buttonPressure, buttonConsumption;
    private JMenuItem buttonCalibrators;

    public MenuLists(MainScreen mainScreen){
        super(Strings.LISTS);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonDepartments = new JMenuItem(Strings.DEPARTMENTS_LIST);
        this.buttonAreas = new JMenuItem(Strings.AREAS_LIST);
        this.buttonProcesses = new JMenuItem(Strings.PROCESSES_LIST);
        this.buttonInstallations = new JMenuItem(Strings.INSTALLATIONS_LIST);
        this.buttonPersons = new JMenuItem(Strings.WORKERS);
        this.buttonSensors = new JMenuItem(Strings.SENSORS_LIST);
        this.buttonCalibrators = new JMenuItem(Strings.CALIBRATORS_LIST);

        this.buttonTemperature = new JMenuItem(MeasurementConstants.TEMPERATURE.getValue());
        this.buttonTemperature.setToolTipText(Settings.getSettingValue(MeasurementConstants.TEMPERATURE.getValue()));
        this.buttonPressure = new JMenuItem(MeasurementConstants.PRESSURE.getValue());
        this.buttonPressure.setToolTipText(Settings.getSettingValue(MeasurementConstants.PRESSURE.getValue()));
        this.buttonConsumption = new JMenuItem(MeasurementConstants.CONSUMPTION.getValue());
        this.buttonConsumption.setToolTipText(Settings.getSettingValue(MeasurementConstants.CONSUMPTION.getValue()));
    }

    @Override
    public void setReactions() {
        this.buttonDepartments.addActionListener(clickDepartments);
        this.buttonAreas.addActionListener(clickAreas);
        this.buttonProcesses.addActionListener(clickProcesses);
        this.buttonInstallations.addActionListener(clickInstallations);
        this.buttonPersons.addActionListener(clickButtonPersons);
        this.buttonSensors.addActionListener(clickSensors);
        this.buttonTemperature.addActionListener(clickTemperature);
        this.buttonPressure.addActionListener(clickPressure);
        this.buttonConsumption.addActionListener(clickConsumption);
        this.buttonCalibrators.addActionListener(clickCalibrators);
    }

    @Override
    public void build() {
        this.add(buttonSensors);
        this.addSeparator();
        this.add(buttonCalibrators);
        this.addSeparator();
        this.add(buttonDepartments);
        this.add(buttonAreas);
        this.add(buttonProcesses);
        this.add(buttonInstallations);
        this.addSeparator();
        this.add(this.buttonPersons);
        this.addSeparator();
        JMenu methods = new JMenu(Strings.METHODS);
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
                    new PathListsDialog(mainScreen, Strings.DEPARTMENTS_LIST).setVisible(true);
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
                    new PathListsDialog(mainScreen, Strings.AREAS_LIST).setVisible(true);
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
                    new PathListsDialog(mainScreen, Strings.PROCESSES_LIST).setVisible(true);
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
                    new PathListsDialog(mainScreen, Strings.INSTALLATIONS_LIST).setVisible(true);
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
