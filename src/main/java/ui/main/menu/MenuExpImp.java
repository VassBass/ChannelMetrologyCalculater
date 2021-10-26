package ui.main.menu;

import constants.Files;
import constants.Strings;
import ui.UI_Container;
import ui.exportData.ExportDataDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuExpImp extends JMenu implements UI_Container {
    private final MainScreen mainScreen;

    private JMenuItem buttonExport, buttonImport;
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
        this.buttonExport = new JMenuItem(Strings.EXPORT_DATA);
        this.buttonImport = new JMenuItem(Strings.IMPORT_DATA);
        this.buttonFolder = new JMenuItem(Strings.EXPORTED_FILES);
    }

    @Override
    public void setReactions() {
        this.buttonExport.addActionListener(clickExport);
        this.buttonImport.addActionListener(clickFolder);
    }

    @Override
    public void build() {
        this.addSeparator();
        this.add(this.buttonExport);
        this.addSeparator();
        this.add(this.buttonImport);
        this.addSeparator();
        this.add(this.buttonFolder);
        this.addSeparator();
    }

    private final ActionListener clickExport = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ExportDataDialog(mainScreen).setVisible(true);
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
