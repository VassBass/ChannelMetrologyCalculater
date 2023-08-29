package service.export_import.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.PersonRepository;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.export_import.Entity;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultExporter implements Exporter {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExporter.class);

    private static final String EXCEPTION_MESSAGE = "Exception was thrown!";
    private static final String EXPORT_FILE_EXTENSION = "cmce";

    private final RepositoryFactory repositoryFactory;
    private final ExportConfigHolder configHolder;

    public DefaultExporter(@Nonnull RepositoryFactory repositoryFactory, @Nonnull ExportConfigHolder configHolder) {
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
    }

    @Override
    public boolean export() {
        Map<Entity, Collection<?>> data = extractData();
        return writeToFile(data);
    }

    private Map<Entity, Collection<?>> extractData() {
        Map<Entity, Collection<?>> result = new HashMap<>();

        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        ControlPointsRepository controlPointsRepository = repositoryFactory.getImplementation(ControlPointsRepository.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        MeasurementFactorRepository measurementFactorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);

        result.put(Entity.CALIBRATOR, calibratorRepository.getAll());
        result.put(Entity.CHANNEL, channelRepository.getAll());
        result.put(Entity.CONTROL_POINTS, controlPointsRepository.getAll());
        result.put(Entity.MEASUREMENT, measurementRepository.getAll());
        result.put(Entity.MEASUREMENT_TRANSFORM_FACTOR, measurementFactorRepository.getAll());
        result.put(Entity.PERSON, personRepository.getAll());
        result.put(Entity.SENSOR, sensorRepository.getAll());
        result.put(Entity.SENSOR_ERROR, sensorErrorRepository.getAll());

        return result;
    }

    boolean writeToFile(Map<Entity, Collection<?>> data) {
        String folder = configHolder.getExportFolderPath();

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        String fullDay = day < 10 ? "0" + day : String.valueOf(day);
        String fullMonth = month < 10 ? "0" + month : String.valueOf(month);
        String fullHours = hours < 10 ? "0" + hours : String.valueOf(hours);
        String fullMinutes = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        String fileName = String.format("export(%s.%s.%s[%s:%s]).%s", fullDay, fullMonth, year, fullHours, fullMinutes, EXPORT_FILE_EXTENSION);

        File file = new File(folder, fileName);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(data);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            logger.warn(EXCEPTION_MESSAGE, e);
            return false;
        }
    }
}
