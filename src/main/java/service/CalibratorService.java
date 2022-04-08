package service;

import model.Calibrator;
import model.Measurement;

import java.util.ArrayList;

public interface CalibratorService {
    void init();
    ArrayList<Calibrator> getAll();
    String[] getAllNames(Measurement measurement);

    ArrayList<Calibrator> add(Calibrator calibrator);
    void addInCurrentThread(Calibrator calibrator);

    ArrayList<Calibrator> remove(Calibrator calibrator);
    ArrayList<Calibrator> remove(int index);

    ArrayList<Calibrator> set(Calibrator oldCalibrator, Calibrator newCalibrator);
    void setInCurrentThread(Calibrator oldCalibrator, Calibrator newCalibrator);

    Calibrator get(String name);
    Calibrator get(int index);

    void clear();

    void importDataInCurrentThread(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange);
    void rewriteInCurrentThread(ArrayList<Calibrator>calibrators);
    void resetToDefaultInCurrentThread();

    boolean isExists(Calibrator calibrator);
    boolean backgroundTaskIsRun();
}
