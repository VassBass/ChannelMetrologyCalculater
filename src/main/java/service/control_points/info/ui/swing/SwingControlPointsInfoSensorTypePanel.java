package service.control_points.info.ui.swing;

import model.dto.Sensor;
import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor.SensorRepository;
import service.control_points.info.ui.ControlPointsInfoSensorTypePanel;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static model.ui.builder.CellBuilder.NONE;

public class SwingControlPointsInfoSensorTypePanel extends TitledPanel implements ControlPointsInfoSensorTypePanel {
    private static final String TITLE_TEXT = "Тип ПВП";

    private final RepositoryFactory repositoryFactory;

    private final DefaultComboBox measurementName;
    private final DefaultComboBox sensorType;

    public SwingControlPointsInfoSensorTypePanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(TITLE_TEXT, Color.BLACK);
        this.repositoryFactory = repositoryFactory;
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        measurementName = new DefaultComboBox(false);
        measurementName.setList(Arrays.asList(measurementRepository.getAllNames()));

        sensorType = new DefaultComboBox(false);
        sensorType.setList(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(measurementName.getSelectedString())));

        measurementName.addItemListener(e -> {
            String selected = measurementName.getSelectedString();
            sensorType.setList(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selected)));
        });

        this.add(measurementName, new CellBuilder().fill(NONE).x(0).build());
        this.add(sensorType, new CellBuilder().fill(NONE).x(1).build());
    }

    @Override
    public void setSensorType(String type) {
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        String measurementName = sensorRepository.getAll().stream()
                .filter(s -> s.getType().equals(type))
                .findAny()
                .map(Sensor::getMeasurementName)
                .orElse(null);
        if (Objects.nonNull(measurementName)) {
            this.measurementName.setSelectedItem(measurementName);
            this.sensorType.setSelectedItem(type);
        }
    }

    @Override
    public String getSensorType() {
        return sensorType.getSelectedString();
    }
}
