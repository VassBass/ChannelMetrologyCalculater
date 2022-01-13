package backgroundTasks.controllers;

import application.Application;
import constants.Strings;
import ui.model.LoadDialog;
import ui.pathLists.PathListsDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class PutPathElementInList extends SwingWorker<Void, Void> {
    private final PathListsDialog dialog;
    private final String elementType;
    private final String elementOldName;
    private final String elementNewName;
    private final LoadDialog loadDialog;

    public PutPathElementInList(PathListsDialog dialog,String elementType, String elementOldName, String elementNewName){
        super();
        this.dialog = dialog;
        this.elementType = elementType;
        this.elementOldName = elementOldName;
        this.elementNewName = elementNewName;

        this.loadDialog = new LoadDialog(dialog);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        ArrayList<String>elementsList = null;
        switch (this.elementType){
            case Strings.DEPARTMENT:
                elementsList = Application.context.departmentsController.getAll();
                break;
            case Strings.AREA:
                elementsList = Application.context.areasController.getAll();
                break;
            case Strings.PROCESS:
                elementsList = Application.context.processesController.getAll();
                break;
            case Strings.INSTALLATION:
                elementsList = Application.context.installationsController.getAll();
                break;
        }
        int indexToRemove = -1;
        int oldIndex = -1;
        for (int x = 0; x< Objects.requireNonNull(elementsList).size(); x++){
            if (this.elementOldName == null){
                if (elementsList.get(x).equals(this.elementNewName)){
                    indexToRemove = x;
                    break;
                }
            }else {
                if (elementsList.get(x).equals(this.elementOldName)){
                    oldIndex = x;
                    if (indexToRemove != -1){
                        break;
                    }
                }else if (elementsList.get(x).equals(this.elementNewName)){
                    indexToRemove = x;
                    if (oldIndex != -1){
                        break;
                    }
                }
            }
        }

        if (oldIndex != -1){
            elementsList.set(oldIndex, this.elementNewName);
        }else {
            elementsList.add(this.elementNewName);
        }
        if (indexToRemove != -1){
            elementsList.remove(indexToRemove);
        }

        switch (this.elementType){
            case Strings.DEPARTMENT:
                //Lists.saveDepartmentsListToFile(elementsList);
                break;
            case Strings.AREA:
                //Lists.saveAreasListToFile(elementsList);
                break;
            case Strings.PROCESS:
                //Lists.saveProcessesListToFile(elementsList);
                break;
            case Strings.INSTALLATION:
                //Lists.saveInstallationsListToFile(elementsList);
                break;
        }

        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.dialog.update(this.elementType);
    }
}
