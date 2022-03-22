package ui.importData;

import application.Application;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportFileChooser extends JFileChooser {
    private static final String IMPORT_DATA = "Імпорт даних";

    public ImportFileChooser(){
        super();
        final MainScreen mainScreen = Application.context.mainScreen;

        this.setDialogTitle(IMPORT_DATA);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Файли експорту (*.db)",
                "db");
        this.setAcceptAllFileFilterUsed(false);
        this.setFileFilter(filter);

        int result = this.showOpenDialog(mainScreen);
        if (result == JFileChooser.APPROVE_OPTION){

        }
    }
}