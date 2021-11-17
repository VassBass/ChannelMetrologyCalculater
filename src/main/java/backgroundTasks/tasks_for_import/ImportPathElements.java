package backgroundTasks.tasks_for_import;

import constants.Strings;
import support.Lists;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ImportPathElements extends SwingWorker<Integer, Void> {
    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    public ImportPathElements(MainScreen mainScreen, File exportDataFile){
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
    // return 1: В файле отсутствуют новые цеха
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            return this.copyPathElements();
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            switch (this.get()) {
                case 1:
                    JOptionPane.showMessageDialog(mainScreen, "У обраному файлі відсутні нові данні", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
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
    private int copyPathElements() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<?>[]importedData = (ArrayList<?>[]) ois.readObject();
        ois.close();
        if (importedData == null){
            return 1;
        }

        for (int x=0;x<4;x++) {
            ArrayList<String> importedList = (ArrayList<String>) importedData[x];
            ArrayList<String> oldList = null;
            ArrayList<String> toAdd = new ArrayList<>();
            switch (x){
                case 0:
                    oldList = Lists.departments();
                    break;
                case 1:
                    oldList  = Lists.areas();
                    break;
                case 2:
                    oldList = Lists.processes();
                    break;
                case 3:
                    oldList = Lists.installations();
                    break;
            }

            for (String imp : importedList) {
                boolean exist = false;
                for (String old : Objects.requireNonNull(oldList)) {
                    if (old.equals(imp)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    toAdd.add(imp);
                }
            }
            if (!toAdd.isEmpty()) {
                ArrayList<String> newList = new ArrayList<>(oldList.size() + toAdd.size());
                newList.addAll(oldList);
                newList.addAll(toAdd);
                switch (x){
                    case 0:
                        Lists.saveDepartmentsListToFile(newList);
                        break;
                    case 1:
                        Lists.saveAreasListToFile(newList);
                        break;
                    case 2:
                        Lists.saveProcessesListToFile(newList);
                        break;
                    case 3:
                        Lists.saveInstallationsListToFile(newList);
                        break;
                }
            }
        }
        return 0;
    }
}
