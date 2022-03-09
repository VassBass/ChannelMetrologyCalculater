package repository;

import model.Calibrator;

import java.util.ArrayList;

public interface CalibratorRepository {
    ArrayList<Calibrator> getAll();
    void add(Calibrator calibrator);
    void remove(String calibratorName);
    void set(Calibrator oldCalibrator, Calibrator newCalibrator);
    void clear();
    void rewriteInCurrentThread(ArrayList<Calibrator>calibrators);
    void export(ArrayList<Calibrator>calibrators);
    void rewrite(ArrayList<Calibrator>calibrators);
}
