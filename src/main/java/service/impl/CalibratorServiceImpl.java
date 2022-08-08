package service.impl;

import def.DefaultCalibrators;
import model.Calibrator;
import model.Measurement;
import repository.CalibratorRepository;
import repository.impl.CalibratorRepositorySQLite;
import service.CalibratorService;

import javax.annotation.Nonnull;
import java.util.Collection;

public class CalibratorServiceImpl implements CalibratorService {
    private static CalibratorServiceImpl service;

    private final CalibratorRepository repository;

    private CalibratorServiceImpl(){
        repository = new CalibratorRepositorySQLite();
    }

    public CalibratorServiceImpl(CalibratorRepository repository){
        this.repository = repository;
    }

    public static CalibratorServiceImpl getInstance() {
        if (service == null) service = new CalibratorServiceImpl();

        return service;
    }

    @Override
    public Collection<Calibrator> getAll() {
        return this.repository.getAll();
    }

    @Override
    public String[] getAllNames(Measurement measurement){
        return this.repository.getAllNames(measurement);
    }

    @Override
    public boolean add(@Nonnull Calibrator calibrator) {
        return this.repository.add(calibrator);
    }

    @Override
    public boolean remove(@Nonnull Calibrator calibrator) {
        return this.repository.remove(calibrator);
    }

    @Override
    public boolean removeByMeasurementValue(String measurementValue) {
        return this.repository.removeByMeasurementValue(measurementValue);
    }

    @Override
    public boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator) {
        return this.repository.set(oldCalibrator, newCalibrator);
    }

    @Override
    public boolean changeMeasurementValue(String oldValue, String newValue) {
        return this.repository.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public Calibrator get(String name) {
        return this.repository.get(name);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean importData(Collection<Calibrator>newCalibrators, Collection<Calibrator>calibratorsForChange){
        return this.repository.importData(newCalibrators, calibratorsForChange);
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Calibrator>calibrators){
        return this.repository.rewrite(calibrators);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultCalibrators.get());
    }

    @Override
    public boolean isExists(Calibrator calibrator) {
        return this.repository.isExists(calibrator);
    }
}