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
import java.util.Objects;

public class ImportAreas extends SwingWorker<Integer, Void> {
    private static final String ERROR = "Помилка";
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    public ImportAreas(File exportDataFile){
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
    // return 1: В файле отсутствуют новые участки
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        ArrayList<String> list;
        try {
            list = newAreasList();
        }catch (Exception e){
            return -1;
        }
        if (list == null){
            return 1;
        }else {
            Application.context.areaService.rewriteInCurrentThread(list);
            return 0;
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
    private ArrayList<String> newAreasList() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<String>importedAreas = (ArrayList<String>) ois.readObject();
        ois.close();
        if (importedAreas.isEmpty()){
            return null;
        }

        ArrayList<String>oldList = Application.context.areaService.getAll();
        ArrayList<String>toAdd = new ArrayList<>();
        for (String imp : importedAreas){
            boolean exist = false;
            for (String old : Objects.requireNonNull(oldList)){
                if (old.equals(imp)){
                    exist = true;
                    break;
                }
            }
            if (!exist){
                toAdd.add(imp);
            }
        }
        if (toAdd.isEmpty()){
            return null;
        }else {
            oldList.addAll(toAdd);
            return oldList;
        }
    }
}