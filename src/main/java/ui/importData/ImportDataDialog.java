package ui.importData;

import backgroundTasks.ImportData;
import constants.Files;
import constants.Strings;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.Objects;

public class ImportDataDialog extends JFileChooser {

    public ImportDataDialog(final MainScreen mainScreen){
        super();

        this.setDialogTitle(Strings.IMPORT_DATA);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Файли експорту (*.exp)", "exp");
        this.setFileFilter(filter);

        int result = this.showOpenDialog(mainScreen);
        if (result == JFileChooser.APPROVE_OPTION){
            if (Objects.requireNonNull(Files.getFileExtension(this.getSelectedFile())).equals("exp")){
                new ImportData(mainScreen, this.getSelectedFile()).execute();
            }else{
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(mainScreen, Strings.WRONG_FILE_EXTENSION, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }
    }
}
