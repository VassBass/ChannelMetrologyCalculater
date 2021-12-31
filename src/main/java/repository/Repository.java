package repository;

import controller.FileBrowser;
import model.Model;
import ui.main.MainScreen;
import ui.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Repository<M> extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final File file;
    private ArrayList<M>list;
    private SaveMessage saveMessage;

    public Repository(MainScreen mainScreen, Model model){
        this.mainScreen = mainScreen;
        switch (model){
            case DEPARTMENT:
                this.file = FileBrowser.FILE_DEPARTMENTS;
                break;
            case AREA:
                this.file = FileBrowser.FILE_AREAS;
                break;
            case PROCESS:
                this.file = FileBrowser.FILE_PROCESSES;
                break;
            case INSTALLATION:
                this.file = FileBrowser.FILE_INSTALLATIONS;
                break;
            case CHANNEL:
                this.file = FileBrowser.FILE_CHANNELS;
                break;
            case SENSOR:
                this.file = FileBrowser.FILE_SENSORS;
                break;
            case CALIBRATOR:
                this.file = FileBrowser.FILE_CALIBRATORS;
                break;
            case PERSON:
                this.file = FileBrowser.FILE_PERSONS;
                break;
            default:
                this.file = null;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<M>readList() {
        if (this.file != null) {
            try (ObjectInputStream reader = FileBrowser.getInputStream(this.file)) {
                return (ArrayList<M>) reader.readObject();
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }else return null;
    }

    public void writeList(ArrayList<M>list){
        this.saveMessage = new SaveMessage(this.mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                saveMessage.setVisible(true);
            }
        });
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            FileBrowser.saveToFile(this.file, list);
        }catch (Exception e){
            e.printStackTrace();
        }
        Thread.sleep(3000);
        return null;
    }

    @Override
    protected void done() {
        this.saveMessage.dispose();
    }
}
