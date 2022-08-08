package ui.mainScreen.menu;

import model.Model;
import service.FileBrowser;
import ui.exportData.ConfirmExportDialog;
import ui.importData.ImportFileChooser;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuExpImp extends JMenu {
    private static final Logger LOGGER = Logger.getLogger(MenuExpImp.class.getName());

    private static final String EXPORT_IMPORT = "Експорт/Імпорт";
    private static final String EXPORT_DATA = "Експортувати дані";
    private static final String EXPORTED_FILES = "Файли експорту";
    private static final String IMPORT_CHANNELS = "Імпорт каналів";
    private static final String IMPORT_SENSORS = "Імпорт ПВП";
    private static final String IMPORT_CPV = "Імпортувати значення контрольних точок для ПВП";
    private static final String IMPORT_CALIBRATORS = "Імпорт калібраторів";
    private static final String IMPORT_PERSONS = "Імпорт працівників";
    private static final String IMPORT_DEPARTMENTS = "Імпорт цехів";
    private static final String IMPORT_AREAS = "Імпорт ділянок";
    private static final String IMPORT_PROCESSES = "Імпорт ліній, секцій і т.п";
    private static final String IMPORT_INSTALLATIONS = "Імпорт установок";
    private static final String IMPORT_DATA = "Імпортувати дані";

    private final MainScreen mainScreen;

    private JMenuItem buttonExport, buttonImport, buttonFolder;
    private JMenuItem btnImportChannels, btnImportSensors, btnImportCalibrators, btnImportPersons, btnControlPoints,
            btnImportDepartments, btnImportAreas, btnImportProcesses, btnImportInstallations;

    public MenuExpImp(){
        super(EXPORT_IMPORT);
        this.mainScreen = MainScreen.getInstance();

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonExport = new JMenuItem(EXPORT_DATA);
        this.buttonImport = new JMenuItem(IMPORT_DATA);
        this.buttonFolder = new JMenuItem(EXPORTED_FILES);
        this.btnImportChannels = new JMenuItem(IMPORT_CHANNELS);
        this.btnImportSensors = new JMenuItem(IMPORT_SENSORS);
        this.btnControlPoints = new JMenuItem(IMPORT_CPV);
        this.btnImportCalibrators = new JMenuItem(IMPORT_CALIBRATORS);
        this.btnImportPersons = new JMenuItem(IMPORT_PERSONS);
        this.btnImportDepartments = new JMenuItem(IMPORT_DEPARTMENTS);
        this.btnImportAreas = new JMenuItem(IMPORT_AREAS);
        this.btnImportProcesses = new JMenuItem(IMPORT_PROCESSES);
        this.btnImportInstallations = new JMenuItem(IMPORT_INSTALLATIONS);
    }

    private void setReactions() {
        this.buttonExport.addActionListener(this.clickExport);
        this.buttonImport.addActionListener(this.clickImport);
        this.buttonFolder.addActionListener(this.clickFolder);
        this.btnImportChannels.addActionListener(this.clickImportChannel);
        this.btnImportSensors.addActionListener(this.clickImportSensor);
        this.btnControlPoints.addActionListener(this.clickImportCPV);
        this.btnImportCalibrators.addActionListener(this.clickImportCalibrator);
        this.btnImportPersons.addActionListener(this.clickImportPerson);
        this.btnImportDepartments.addActionListener(this.clickImportDepartment);
        this.btnImportAreas.addActionListener(this.clickImportArea);
        this.btnImportProcesses.addActionListener(this.clickImportProcess);
        this.btnImportInstallations.addActionListener(this.clickImportInstallation);
    }

    private void build() {
        this.add(this.buttonExport);
        this.add(this.buttonFolder);
        this.add(this.buttonImport);
        this.addSeparator();
        this.add(this.btnImportChannels);
        this.add(this.btnImportSensors);
        this.add(this.btnControlPoints);
        this.add(this.btnImportCalibrators);
        this.add(this.btnImportPersons);
        this.add(this.btnImportDepartments);
        this.add(this.btnImportAreas);
        this.add(this.btnImportProcesses);
        this.add(this.btnImportInstallations);
    }

    private final ActionListener clickExport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ConfirmExportDialog(mainScreen).setVisible(true);
        }
    };

    private final ActionListener clickImport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.ALL).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportChannel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.CHANNEL).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportSensor = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.SENSOR).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportCalibrator = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.CALIBRATOR).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportCPV = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.SENSOR_VALUE).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportPerson = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.PERSON).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportDepartment = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.DEPARTMENT).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportArea = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.AREA).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportProcess = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.PROCESS).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
                }
            });
        }
    };

    private final ActionListener clickImportInstallation = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ImportFileChooser(Model.INSTALLATION).setVisible(true);
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    }
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