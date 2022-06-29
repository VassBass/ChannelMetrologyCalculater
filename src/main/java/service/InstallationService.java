package service;

import java.util.ArrayList;

public interface InstallationService {
    ArrayList<String> getAll();
    String[] getAllInStrings();
    boolean add(String object);
    boolean remove(String object);
    boolean set(String oldObject, String newObject);
    boolean clear();
    boolean rewrite(ArrayList<String>installations);
    boolean resetToDefault();
}
