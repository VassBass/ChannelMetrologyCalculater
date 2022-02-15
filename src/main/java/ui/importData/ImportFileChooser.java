package ui.importData;

import application.Application;
import backgroundTasks.data_import.*;
import service.FileBrowser;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Objects;

public class ImportFileChooser extends JFileChooser {
    private static final String IMPORT_DATA = "Імпорт даних";
    private static final String ERROR = "Помилка";
    private static final String WRONG_FILE_EXTENSION = "Формат обраного файлу не підтримується";

    public ImportFileChooser(){
        super();
        final MainScreen mainScreen = Application.context.mainScreen;

        this.setDialogTitle(IMPORT_DATA);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Файли експорту (*.sen, *cal, *dep, *are, *prc, *ins, *pat, *chn, *per)",
                "sen", "cal", "dep", "are", "prc", "ins", "pat", "chn", "per");
        this.setAcceptAllFileFilterUsed(false);
        this.setFileFilter(filter);

        int result = this.showOpenDialog(mainScreen);
        if (result == JFileChooser.APPROVE_OPTION){
            switch (Objects.requireNonNull(FileBrowser.getFileExtension(this.getSelectedFile()))){
                case "sen":
                    new ImportSensors(this.getSelectedFile()).execute();
                    break;
                case "cal":
                    new ImportCalibrators(this.getSelectedFile()).execute();
                    break;
                case "dep":
                    new ImportDepartments(this.getSelectedFile()).execute();
                    break;
                case "are":
                    new ImportAreas(this.getSelectedFile()).execute();
                    break;
                case "prc":
                    new ImportProcesses(this.getSelectedFile()).execute();
                    break;
                case "ins":
                    new ImportInstallations(this.getSelectedFile()).execute();
                    break;
                case "pat":
                    new ImportPathElements(this.getSelectedFile()).execute();
                    break;
                case "chn":
                    new ImportChannels(this.getSelectedFile()).execute();
                    break;
                case "per":
                    new ImportPersons(this.getSelectedFile()).execute();
                    break;
                default:
                    JOptionPane.showMessageDialog(mainScreen, WRONG_FILE_EXTENSION, ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}