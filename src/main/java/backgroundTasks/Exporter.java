package backgroundTasks;

import service.FileBrowser;
import ui.mainScreen.MainScreen;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Exporter extends SwingWorker<Boolean, Void> {
    private static final Logger LOGGER = Logger.getLogger(Exporter.class.getName());
    private final DialogLoading loadDialog = new DialogLoading();

    private String fileName(){
        StringBuilder builder = new StringBuilder();
        Calendar date = Calendar.getInstance();
        builder.append("export[")
                .append(date.get(Calendar.DAY_OF_MONTH))
                .append(".")
                .append((date.get(Calendar.MONTH) + 1))
                .append(".")
                .append(date.get(Calendar.YEAR))
                .append("].db");
        return builder.toString();
    }

    private void exportData() throws IOException {
        File dataFile = new File(FileBrowser.DIR_MAIN, "Data.db");
        File exportFile = new File(FileBrowser.DIR_EXPORT, this.fileName());
        InputStream in = new FileInputStream(dataFile);
        Path out = Paths.get(exportFile.getAbsolutePath());
        Files.copy(in, out, REPLACE_EXISTING);
        in.close();
    }

    public void export(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
        this.execute();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            this.exportData();
            return true;
        }catch (IOException ex){
            return false;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                int result = JOptionPane.showConfirmDialog(MainScreen.getInstance(),
                        "Експорт закінчено. Відкрити папку з файлами експорту?", "Експорт", JOptionPane.OK_CANCEL_OPTION);
                if (result == 0){
                    Desktop desktop;
                    if (Desktop.isDesktopSupported()){
                        desktop = Desktop.getDesktop();
                        desktop.open(FileBrowser.DIR_EXPORT);
                    }
                }
            }
        } catch (ExecutionException | InterruptedException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error: ", e);
        }
    }
}