package backgroundTasks.controllers;

import support.Comparator;
import ui.LoadDialog;
import ui.personsList.PersonsListDialog;
import model.Worker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RemovePerson extends SwingWorker<Void, Void> {
    private final PersonsListDialog parent;
    private final Worker worker;
    private final LoadDialog loadDialog;

    public RemovePerson(PersonsListDialog parent, Worker worker){
        super();
        this.parent = parent;
        this.worker = worker;

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
        /*ArrayList<Worker>workers = Lists.persons();
        if (workers != null) {
            for (int x = 0; x < workers.size(); x++) {
                if (Comparator.personsMatch(workers.get(x), this.worker)) {
                    workers.remove(x);
                    break;
                }
            }

            Lists.savePersonsListToFile(workers);
        }*/
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.parent.update();
    }
}
