package repository;

import controller.FileBrowser;
import model.Model;
import ui.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Repository<M> extends SwingWorker<Void, Void> {
    private final Window window;
    private final File file;
    private ArrayList<M>list;
    private SaveMessage saveMessage;

    public Repository(Window window, Model model){
        this.window = window;
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
            case MEASUREMENT:
                this.file = FileBrowser.FILE_MEASUREMENTS;
                break;
            default:
                this.file = null;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<M>readList() throws IOException, ClassNotFoundException {
        if (this.file != null) {
            try (ObjectInputStream reader = FileBrowser.getInputStream(this.file)) {
                return (ArrayList<M>) reader.readObject();
            } catch (IOException e) {
                throw new IOException();
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException();
            }
        }else return null;
    }

    public void writeList(ArrayList<M>list){
        this.list = list;
        if (this.window != null) {
            this.saveMessage = new SaveMessage(this.window);
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    saveMessage.setVisible(true);
                }
            });
        }
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            FileBrowser.saveToFile(this.file, list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void done() {
        if (this.saveMessage != null) this.saveMessage.dispose();
    }
}
