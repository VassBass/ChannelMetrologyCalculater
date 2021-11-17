package backgroundTasks.controllers;

import support.Lists;
import constants.Strings;
import ui.LoadDialog;
import ui.personsList.PersonsListDialog;
import support.Worker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PutPersonInList extends SwingWorker<Void, Void> {
    private final PersonsListDialog parent;
    private final Worker newWorker, oldWorker;
    private final LoadDialog loadDialog;

    public PutPersonInList(final PersonsListDialog parent, Worker newWorker, Worker oldWorker){
        super();
        this.parent = parent;
        this.newWorker = newWorker;
        this.oldWorker = oldWorker;

        this.loadDialog = new LoadDialog(parent);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        ArrayList<Worker>workers = Lists.persons();
        if (workers != null) {
            if (this.workerIsExists(this.newWorker)) {
                JOptionPane.showMessageDialog(this.parent, "Робітник з такими данними вже є у списку", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
            } else {
                if (this.oldWorker == null) {
                    workers.add(this.newWorker);
                } else {
                    for (int x = 0; x < workers.size(); x++) {
                        if (oldWorker.equalsPerson(workers.get(x))) {
                            workers.set(x, this.newWorker);
                            break;
                        }
                    }
                }
                Lists.savePersonsListToFile(workers);
            }
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.parent.update();
    }

    private boolean workerIsExists(Worker worker){
        ArrayList<Worker>workers = Lists.persons();
        if (workers != null) {
            for (Worker value : workers) {
                if (worker.equalsPerson(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
