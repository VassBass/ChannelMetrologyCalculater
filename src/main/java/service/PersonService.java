package service;

import model.Worker;

import java.awt.*;
import java.util.ArrayList;

public interface PersonService {
    void init(Window window);
    ArrayList<Worker> getAll();
    String[] getAllNames();
    String[] getNamesOfHeads();
    ArrayList<Worker> add(Worker person);
    ArrayList<Worker> remove(Worker person);
    ArrayList<Worker> set(Worker oldPerson, Worker newPerson);
    Worker get(int index);
    void clear();
    void save();
    boolean exportData();
    void rewriteInCurrentThread(ArrayList<Worker>persons);
}
