package repository;

import model.Measurement;
import model.Calibrator;

import java.util.ArrayList;

public interface CalibratorRepository {
    ArrayList<Calibrator> getAll();
    String[]getAllNames(Measurement measurement);
    Calibrator get(String name);
    Calibrator get(int index);
    void add(Calibrator calibrator);
    void addInCurrentThread(Calibrator calibrator);
    void remove(Calibrator calibrator);
    void remove(int index);
    void set(Calibrator oldCalibrator, Calibrator newCalibrator);
    void setInCurrentThread(Calibrator oldCalibrator, Calibrator newCalibrator);
    void clear();
    void rewriteInCurrentThread(ArrayList<Calibrator>calibrators);
    void export();
    void rewrite(ArrayList<Calibrator>calibrators);
}
