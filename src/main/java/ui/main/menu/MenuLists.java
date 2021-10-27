package ui.main.menu;

import calculation.Method;
import constants.Strings;
import constants.Value;
import support.Settings;
import ui.UI_Container;
import ui.main.MainScreen;
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
    private JMenuItem mkmx_5300_01, mkmx_5300_02;

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

        this.mkmx_5300_01 = new JMenuItem(Settings.getSettingValue(Value.NAME_MKMX_5300_01));
        this.mkmx_5300_02 = new JMenuItem(Settings.getSettingValue(Value.NAME_MKMX_5300_02));
    }

    @Override
    public void setReactions() {
        this.buttonDepartments.addActionListener(clickDepartments);
        this.buttonAreas.addActionListener(clickAreas);
        this.buttonProcesses.addActionListener(clickProcesses);
        this.buttonInstallations.addActionListener(clickInstallations);
        this.buttonPersons.addActionListener(clickButtonPersons);
        this.buttonSensors.addActionListener(clickSensors);
        this.mkmx_5300_01.addActionListener(clickMKMX_5300_01);
        this.mkmx_5300_02.addActionListener(clickMKMX_5300_02);
    }

    @Override
    public void build() {
        this.add(buttonSensors);
        this.addSeparator();
        this.add(buttonDepartments);
        this.add(buttonAreas);
        this.add(buttonProcesses);
        this.add(buttonInstallations);
        this.addSeparator();
        this.add(this.buttonPersons);
        this.addSeparator();
        JMenu methods = new JMenu(Strings.METHODS);
        methods.add(this.mkmx_5300_01);
        methods.add(this.mkmx_5300_02);
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

    private final ActionListener clickMKMX_5300_01 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MethodInfoDialog(mainScreen, Method.MKMX_5300_01_18).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickMKMX_5300_02 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MethodInfoDialog(mainScreen, Method.MKMX_5300_02_18).setVisible(true);
                }
            });
        }
    };
}
