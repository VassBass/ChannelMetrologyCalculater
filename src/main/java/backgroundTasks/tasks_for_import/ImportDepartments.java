package backgroundTasks.tasks_for_import;

import constants.Strings;
import ui.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ImportDepartments extends SwingWorker<Integer, Void> {
    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    public ImportDepartments(MainScreen mainScreen, File exportDataFile){
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
        ArrayList<String>list;
        try {
            list = newDepartmentsList();
        }catch (Exception e){
            return -1;
        }
        if (list == null){
            return 1;
        }else {
            //Lists.saveDepartmentsListToFile(list);
            return 0;
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
    private ArrayList<String> newDepartmentsList() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<String>importedDepartments = (ArrayList<String>) ois.readObject();
        ois.close();
        if (importedDepartments.isEmpty()){
            return null;
        }

        /*ArrayList<String>oldList = Lists.departments();
        ArrayList<String>toAdd = new ArrayList<>();
        for (String imp : importedDepartments){
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
            ArrayList<String>newList = new ArrayList<>(oldList.size() + toAdd.size());
            newList.addAll(oldList);
            newList.addAll(toAdd);
            return newList;
        }*/return  null;
    }
}