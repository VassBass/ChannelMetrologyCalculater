package ui.importData;

import backgroundTasks.ImportCalibrators;
import backgroundTasks.ImportSensors;
import constants.Files;
import constants.Strings;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Objects;

public class ImportDataDialog extends JFileChooser {

    public ImportDataDialog(final MainScreen mainScreen){
        super();

        this.setDialogTitle(Strings.IMPORT_DATA);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Файли експорту (*.exp, *.sen, *cal)","sen", "exp", "cal");
        this.setAcceptAllFileFilterUsed(false);
        this.setFileFilter(filter);

        int result = this.showOpenDialog(mainScreen);
        if (result == JFileChooser.APPROVE_OPTION){
            switch (Objects.requireNonNull(Files.getFileExtension(this.getSelectedFile()))){
                case "sen":
                    new ImportSensors(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "cal":
                    new ImportCalibrators(mainScreen, this.getSelectedFile()).execute();
                    break;
                default:
                    JOptionPane.showMessageDialog(mainScreen, Strings.WRONG_FILE_EXTENSION, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}
