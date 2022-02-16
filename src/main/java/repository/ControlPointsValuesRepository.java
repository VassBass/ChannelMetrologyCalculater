package repository;

import application.Application;
import model.ControlPointsValues;
import service.FileBrowser;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ControlPointsValuesRepository extends SwingWorker<Void, Void> {
    private ArrayList<ControlPointsValues>list;
    private SaveMessage saveMessage;

    @SuppressWarnings("unchecked")
    public ArrayList<ControlPointsValues>readList() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileBrowser.FILE_CONTROL_POINTS_VALUES));
        ArrayList<ControlPointsValues>values = (ArrayList<ControlPointsValues>) ois.readObject();
        ois.close();
        return values;
    }

    public void writeList(ArrayList<ControlPointsValues>list){
        this.list = list;
        this.saveMessage = new SaveMessage(Application.context.mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                saveMessage.setVisible(true);
            }
        });
        Application.setBusy(true);
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        FileBrowser.saveToFile(FileBrowser.FILE_CONTROL_POINTS_VALUES, this.list);
        return null;
    }

    @Override
    protected void done() {
        this.saveMessage.dispose();
        Application.setBusy(false);
    }
}
