package backgroundTasks.data_import;

import application.Application;
import model.Worker;
import support.Comparator;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ImportPersons extends SwingWorker<Integer, Void> {
    private static final String ERROR = "Помилка";
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    public ImportPersons(File exportDataFile){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.exportDataFile = exportDataFile;
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    // return 0: Импорт прошел успешно
    // return 1: В файле отсутствует информация о работниках
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            if (this.copyPersons()){
                return 0;
            }else {
                return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            switch (this.get()) {
                case 1:
                    JOptionPane.showMessageDialog(this.mainScreen, "У обраному файлі відсутні данні працівників", ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
                    break;
                case -1:
                    JOptionPane.showMessageDialog(this.mainScreen, "Помилка при виконанні імпорту", ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean copyPersons() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<Worker> importedPersons = (ArrayList<Worker>) ois.readObject();
        if (!importedPersons.isEmpty()) {
            ArrayList<Worker> oldPersonsList = Application.context.personsController.getAll();

            for (Worker imp : importedPersons) {
                boolean exist = false;
                for (Worker old : oldPersonsList) {
                    if (Comparator.personsMatch(imp, old)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    oldPersonsList.add(imp);
                }
            }
            Application.context.personsController.rewriteAll(oldPersonsList);
            return true;
        }else {
            return false;
        }
    }
}