package ui.mainScreen.menu;

import application.Application;
import service.FileBrowser;
import model.Model;
import ui.exportData.ConfirmExportDialog;
import ui.importData.ImportFileChooser;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuExpImp extends JMenu {
    private static final String EXPORT_IMPORT = "Експорт/Імпорт";
    private static final String EXPORT_ALL_DATA = "Експортувати все";
    private static final String EXPORT_CHANNELS = "Експортувати канали";
    private static final String EXPORT_SENSORS = "Експортувати ПВП";
    private static final String EXPORT_CALIBRATORS = "Експортувати калібратори";
    private static final String EXPORT_PERSONS = "Експортувати інформацію про робітників";
    private static final String EXPORT_DEPARTMENTS = "Експортувати цехи";
    private static final String EXPORT_AREAS = "Експортувати ділянки";
    private static final String EXPORT_PROCESSES = "Експортувати лінії, секції і т.п.";
    private static final String EXPORT_INSTALLATIONS = "Експортувати установки";
    private static final String EXPORT_ALL_PATH = "Експортувати всі елементи розташування каналів";
    private static final String IMPORT_DATA = "Імпорт даних";
    private static final String EXPORTED_FILES = "Файли експорту";

    private final MainScreen mainScreen;

    private JMenuItem buttonExportChannels;
    private JMenuItem buttonExportSensors;
    private JMenuItem buttonExportCalibrators;
    private JMenuItem buttonExportPersons;
    private JMenuItem buttonExportDepartments;
    private JMenuItem buttonExportAreas;
    private JMenuItem buttonExportProcesses;
    private JMenuItem buttonExportInstallations;
    private JMenuItem buttonExportAllPathElements;
    private JMenuItem buttonImport;
    private JMenuItem buttonFolder;

    public MenuExpImp(){
        super(EXPORT_IMPORT);
        this.mainScreen = Application.context.mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonExportChannels = new JMenuItem(EXPORT_CHANNELS);
        this.buttonExportSensors = new JMenuItem(EXPORT_SENSORS);
        this.buttonExportCalibrators = new JMenuItem(EXPORT_CALIBRATORS);
        this.buttonExportPersons = new JMenuItem(EXPORT_PERSONS);
        this.buttonExportDepartments = new JMenuItem(EXPORT_DEPARTMENTS);
        this.buttonExportAreas = new JMenuItem(EXPORT_AREAS);
        this.buttonExportProcesses = new JMenuItem(EXPORT_PROCESSES);
        this.buttonExportInstallations = new JMenuItem(EXPORT_INSTALLATIONS);
        this.buttonExportAllPathElements = new JMenuItem(EXPORT_ALL_PATH);
        this.buttonImport = new JMenuItem(IMPORT_DATA);
        this.buttonFolder = new JMenuItem(EXPORTED_FILES);
    }

    private void setReactions() {
        this.buttonExportChannels.addActionListener(this.clickExportChannels);
        this.buttonExportSensors.addActionListener(this.clickExportSensors);
        this.buttonExportCalibrators.addActionListener(this.clickExportCalibrators);
        this.buttonExportPersons.addActionListener(this.clickExportPersons);
        this.buttonExportDepartments.addActionListener(this.clickExportDepartments);
        this.buttonExportAreas.addActionListener(this.clickExportAreas);
        this.buttonExportProcesses.addActionListener(this.clickExportProcesses);
        this.buttonExportInstallations.addActionListener(this.clickExportInstallations);
        this.buttonExportAllPathElements.addActionListener(this.clickExportAllPathElements);
        this.buttonImport.addActionListener(this.clickImport);
        this.buttonFolder.addActionListener(this.clickFolder);
    }

    private void build() {
        this.add(this.buttonExportChannels);
        this.addSeparator();
        this.add(this.buttonExportSensors);
        this.addSeparator();
        this.add(this.buttonExportCalibrators);
        this.addSeparator();
        this.add(this.buttonExportPersons);
        this.addSeparator();
        this.add(this.buttonExportDepartments);
        this.add(this.buttonExportAreas);
        this.add(this.buttonExportProcesses);
        this.add(this.buttonExportInstallations);
        this.add(this.buttonExportAllPathElements);
        this.addSeparator();
        this.add(this.buttonImport);
        this.addSeparator();
        this.add(this.buttonFolder);
    }

    private final ActionListener clickExportChannels = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.CHANNEL).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportSensors = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.SENSOR).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportCalibrators = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.CALIBRATOR).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportPersons = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.PERSON).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportDepartments = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.DEPARTMENT).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportAreas = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.AREA).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportProcesses = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.PROCESS).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportInstallations = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.INSTALLATION).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportAllPathElements = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, Model.PATH_ELEMENT).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickImport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (Application.isBusy(mainScreen)) return;
                    new ImportFileChooser().setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickFolder = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Desktop desktop;
            if (Desktop.isDesktopSupported()){
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(FileBrowser.DIR_EXPORT);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    };
}