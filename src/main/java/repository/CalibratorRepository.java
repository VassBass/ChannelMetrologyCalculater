package repository;

import model.Calibrator;
import model.Measurement;

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
    void rewrite(ArrayList<Calibrator>calibrators);
    void importDataInCurrentThread(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange);

    boolean isExists(Calibrator calibrator);

    boolean backgroundTaskIsRun();
}
