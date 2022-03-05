package repository;

import measurements.Measurement;

import java.util.ArrayList;

public interface MeasurementRepository {
    ArrayList<Measurement>getAll();
    void rewriteInCurrentThread(ArrayList<Measurement>measurements);
}
