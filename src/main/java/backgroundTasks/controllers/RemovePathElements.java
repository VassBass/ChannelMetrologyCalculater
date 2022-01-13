package backgroundTasks.controllers;

import application.Application;
import constants.Strings;
import ui.model.LoadDialog;
import ui.pathLists.PathListsDialog;

import javax.swing.*;
import java.awt.*;

public class RemovePathElements extends SwingWorker<Void, Void> {
    private final PathListsDialog dialog;
    private final LoadDialog loadDialog;
    private final String elementsType;
    private final String elementName;

    public RemovePathElements(PathListsDialog dialog, String elementsType, String elementName){
        super();
        this.dialog = dialog;
        this.elementsType = elementsType;
        this.elementName = elementName;

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
        if (elementName == null){
            switch (elementsType){
                case Strings.DEPARTMENTS_LIST:
                    Application.context.departmentsController.clear();
                    break;
                case Strings.AREAS_LIST:
                    Application.context.areasController.clear();
                    break;
                case Strings.PROCESSES_LIST:
                    Application.context.processesController.clear();
                    break;
                case Strings.INSTALLATIONS_LIST:
                    Application.context.installationsController.clear();
                    break;
            }
        }else {
            switch (elementsType){
                case Strings.DEPARTMENTS_LIST:
                    Application.context.departmentsController.remove(this.elementName);
                    break;
                case Strings.AREAS_LIST:
                    Application.context.areasController.remove(this.elementName);
                    break;
                case Strings.PROCESSES_LIST:
                    Application.context.processesController.remove(this.elementName);
                    break;
                case Strings.INSTALLATIONS_LIST:
                    Application.context.installationsController.remove(this.elementName);
                    break;
            }
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.dialog.update(this.elementsType);
    }
}
