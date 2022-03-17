package ui.mainScreen.menu;

import application.Application;
import service.FileBrowser;
import ui.exportData.ConfirmExportDialog;
import ui.importData.ImportFileChooser;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuExpImp extends JMenu {
    private static final String EXPORT_IMPORT = "Експорт/Імпорт";
    private static final String EXPORT_DATA = "Експортувати дані";
    private static final String IMPORT_DATA = "Імпорт даних";
    private static final String EXPORTED_FILES = "Файли експорту";

    private final MainScreen mainScreen;

    private JMenuItem buttonExport;
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
        this.buttonExport = new JMenuItem(EXPORT_DATA);
        this.buttonImport = new JMenuItem(IMPORT_DATA);
        this.buttonFolder = new JMenuItem(EXPORTED_FILES);
    }

    private void setReactions() {
        this.buttonExport.addActionListener(this.clickExport);
        this.buttonImport.addActionListener(this.clickImport);
        this.buttonFolder.addActionListener(this.clickFolder);
    }

    private void build() {
        this.add(this.buttonExport);
        this.addSeparator();
        this.add(this.buttonImport);
        this.addSeparator();
        this.add(this.buttonFolder);
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