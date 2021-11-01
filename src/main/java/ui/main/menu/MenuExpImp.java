package ui.main.menu;

import constants.Files;
import constants.Strings;
import ui.UI_Container;
import ui.importData.ImportDataDialog;
import ui.main.MainScreen;

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
        this.buttonExportAllData.addActionListener(clickExport);
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

    private final ActionListener clickExport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
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
