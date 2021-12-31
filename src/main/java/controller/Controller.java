package controller;

import java.io.IOException;
import java.util.ArrayList;

public interface Controller<M> {
    void resetToDefault();
    ArrayList<M>getAll();
    void add(M object);
    void remove(M object);
    void set(M oldObject, M newObject);
    M get(String identifier);
    M get(int index);
    void clear();
    void save();
    void showNotFoundMessage();
}
