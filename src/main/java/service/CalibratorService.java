package service;

import model.Measurement;
import model.Calibrator;

import java.util.ArrayList;

public interface CalibratorService {
    void init();
    ArrayList<Calibrator> getAll();
    String[] getAllNames(Measurement measurement);
    ArrayList<Calibrator> add(Calibrator calibrator);
    ArrayList<Calibrator> remove(Calibrator calibrator);
    ArrayList<Calibrator> remove(int index);
    ArrayList<Calibrator> set(Calibrator oldCalibrator, Calibrator newCalibrator);
    Calibrator get(String name);
    Calibrator get(int index);
    void clear();
    void exportData();
    void importData(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange);
    void rewriteInCurrentThread(ArrayList<Calibrator>calibrators);
    void resetToDefault();
}
