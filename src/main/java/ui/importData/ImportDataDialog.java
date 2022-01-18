package ui.importData;

import backgroundTasks.data_import.*;
import controller.FileBrowser;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Objects;

public class ImportDataDialog extends JFileChooser {
    private static final String IMPORT_DATA = "Імпорт даних";
    private static final String ERROR = "Помилка";
    private static final String WRONG_FILE_EXTENSION = "Формат обраного файлу не підтримується";

    public ImportDataDialog(final MainScreen mainScreen){
        super();

        this.setDialogTitle(IMPORT_DATA);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Файли експорту (*.exp, *.sen, *cal, *dep, *are, *prc, *ins, *pat, *chn, *per)",
                "sen", "exp", "cal", "dep", "are", "prc", "ins", "pat", "chn", "per");
        this.setAcceptAllFileFilterUsed(false);
        this.setFileFilter(filter);

        int result = this.showOpenDialog(mainScreen);
        if (result == JFileChooser.APPROVE_OPTION){
            switch (Objects.requireNonNull(FileBrowser.getFileExtension(this.getSelectedFile()))){
                case "sen":
                    new ImportSensors(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "cal":
                    new ImportCalibrators(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "dep":
                    new ImportDepartments(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "are":
                    new ImportAreas(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "prc":
                    new ImportProcesses(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "ins":
                    new ImportInstallations(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "pat":
                    new ImportPathElements(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "chn":
                    new ImportChannels(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "per":
                    new ImportPersons(mainScreen, this.getSelectedFile()).execute();
                    break;
                case "exp":
                    new ImportData(mainScreen, this.getSelectedFile()).execute();
                default:
                    JOptionPane.showMessageDialog(mainScreen, WRONG_FILE_EXTENSION, ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}