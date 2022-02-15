package backgroundTasks.data_import;

import application.Application;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ImportPathElements extends SwingWorker<Integer, Void> {
    private static final String ERROR = "Помилка";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";
    private static final String IMPORT = "Імпорт";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    public ImportPathElements(File exportDataFile){
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
                    JOptionPane.showMessageDialog(mainScreen, "У обраному файлі відсутні нові данні", ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
                    break;
                case -1:
                    JOptionPane.showMessageDialog(mainScreen, "Помилка при виконанні імпорту", ERROR, JOptionPane.ERROR_MESSAGE);
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
                    oldList = Application.context.departmentService.getAll();
                    break;
                case 1:
                    oldList  = Application.context.areaService.getAll();
                    break;
                case 2:
                    oldList = Application.context.processService.getAll();
                    break;
                case 3:
                    oldList = Application.context.installationService.getAll();
                    break;
            }
            if (oldList == null) return -1;

            for (String imp : importedList) {
                boolean exist = false;
                for (String old : oldList) {
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
                oldList.addAll(toAdd);
                switch (x){
                    case 0:
                        Application.context.departmentService.rewriteInCurrentThread(oldList);
                        break;
                    case 1:
                        Application.context.areaService.rewriteInCurrentThread(oldList);
                        break;
                    case 2:
                        Application.context.processService.rewriteInCurrentThread(oldList);
                        break;
                    case 3:
                        Application.context.installationService.rewriteInCurrentThread(oldList);
                        break;
                }
            }
        }
        return 0;
    }
}