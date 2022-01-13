package backgroundTasks.tasks_for_import;

import constants.Strings;
import model.Worker;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ImportPersons extends SwingWorker<Integer, Void> {
    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    public ImportPersons(MainScreen mainScreen, File exportDataFile){
        super();
        this.mainScreen = mainScreen;
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
                    JOptionPane.showMessageDialog(mainScreen, "У обраному файлі відсутні данні працівників", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.IMPORT, JOptionPane.INFORMATION_MESSAGE);
                    break;
                case -1:
                    JOptionPane.showMessageDialog(mainScreen, "Помилка при виконанні імпорту", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
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
            /*ArrayList<Worker> oldPersonsList = Lists.persons();

            for (Worker imp : importedPersons) {
                boolean exist = false;
                for (Worker old : Objects.requireNonNull(oldPersonsList)) {
                    if (Comparator.personsMatch(imp, old)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    oldPersonsList.add(imp);
                }
            }
            Lists.savePersonsListToFile(oldPersonsList);*/
            return true;
        }else {
            return false;
        }
    }
}
