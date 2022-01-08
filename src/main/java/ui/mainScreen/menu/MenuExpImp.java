package ui.mainScreen.menu;

import backgroundTasks.export.ExportData;
import constants.Files;
import constants.Strings;
import ui.exportData.ConfirmExportDialog;
import ui.importData.ImportDataDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuExpImp extends JMenu implements UI_Container {
    private final MainScreen mainScreen;

    private JMenuItem buttonExportAllData;
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

    public MenuExpImp(MainScreen mainScreen){
        super(Strings.EXPORT_IMPORT);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonExportAllData = new JMenuItem(Strings.EXPORT_ALL_DATA);
        this.buttonExportChannels = new JMenuItem(Strings.EXPORT_CHANNELS);
        this.buttonExportSensors = new JMenuItem(Strings.EXPORT_SENSORS);
        this.buttonExportCalibrators = new JMenuItem(Strings.EXPORT_CALIBRATORS);
        this.buttonExportPersons = new JMenuItem(Strings.EXPORT_PERSONS);
        this.buttonExportDepartments = new JMenuItem(Strings.EXPORT_DEPARTMENTS);
        this.buttonExportAreas = new JMenuItem(Strings.EXPORT_AREAS);
        this.buttonExportProcesses = new JMenuItem(Strings.EXPORT_PROCESSES);
        this.buttonExportInstallations = new JMenuItem(Strings.EXPORT_INSTALLATIONS);
        this.buttonExportAllPathElements = new JMenuItem(Strings.EXPORT_ALL_PATH);
        this.buttonImport = new JMenuItem(Strings.IMPORT_DATA);
        this.buttonFolder = new JMenuItem(Strings.EXPORTED_FILES);
    }

    @Override
    public void setReactions() {
        this.buttonExportAllData.addActionListener(clickExportAllData);
        this.buttonExportChannels.addActionListener(clickExportChannels);
        this.buttonExportSensors.addActionListener(clickExportSensors);
        this.buttonExportCalibrators.addActionListener(clickExportCalibrators);
        this.buttonExportPersons.addActionListener(clickExportPersons);
        this.buttonExportDepartments.addActionListener(clickExportDepartments);
        this.buttonExportAreas.addActionListener(clickExportAreas);
        this.buttonExportProcesses.addActionListener(clickExportProcesses);
        this.buttonExportInstallations.addActionListener(clickExportInstallations);
        this.buttonExportAllPathElements.addActionListener(clickExportAllPathElements);
        this.buttonImport.addActionListener(clickImport);
        this.buttonFolder.addActionListener(clickFolder);
    }

    @Override
    public void build() {
        this.add(this.buttonExportAllData);
        this.addSeparator();
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

    private final ActionListener clickExportAllData = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, ExportData.ALL_DATA).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickExportChannels = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmExportDialog(mainScreen, ExportData.CHANNELS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.SENSORS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.CALIBRATORS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.PERSONS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.DEPARTMENTS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.AREAS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.PROCESSES).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.INSTALLATIONS).setVisible(true);
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
                    new ConfirmExportDialog(mainScreen, ExportData.ALL_PATH_ELEMENTS).setVisible(true);
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
                    new ImportDataDialog(mainScreen).setVisible(true);
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
                    desktop.open(Files.EXPORT_DIR);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    };
}
