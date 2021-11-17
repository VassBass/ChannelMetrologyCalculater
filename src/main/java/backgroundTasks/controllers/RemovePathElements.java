package backgroundTasks.controllers;

import constants.Strings;
import support.Lists;
import ui.LoadDialog;
import ui.pathLists.PathListsDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

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
                    Lists.saveDepartmentsListToFile(new ArrayList<String>());
                    break;
                case Strings.AREAS_LIST:
                    Lists.saveAreasListToFile(new ArrayList<String>());
                    break;
                case Strings.PROCESSES_LIST:
                    Lists.saveProcessesListToFile(new ArrayList<String>());
                    break;
                case Strings.INSTALLATIONS_LIST:
                    Lists.saveInstallationsListToFile(new ArrayList<String>());
                    break;
            }
        }else {
            ArrayList<String>elements;
            switch (elementsType){
                case Strings.DEPARTMENTS_LIST:
                    elements = Lists.departments();
                    Objects.requireNonNull(elements).remove(this.elementName);
                    Lists.saveDepartmentsListToFile(elements);
                    break;
                case Strings.AREAS_LIST:
                    elements = Lists.areas();
                    Objects.requireNonNull(elements).remove(this.elementName);
                    Lists.saveAreasListToFile(elements);
                    break;
                case Strings.PROCESSES_LIST:
                    elements = Lists.processes();
                    Objects.requireNonNull(elements).remove(this.elementName);
                    Lists.saveProcessesListToFile(elements);
                    break;
                case Strings.INSTALLATIONS_LIST:
                    elements = Lists.installations();
                    Objects.requireNonNull(elements).remove(this.elementName);
                    Lists.saveInstallationsListToFile(elements);
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
