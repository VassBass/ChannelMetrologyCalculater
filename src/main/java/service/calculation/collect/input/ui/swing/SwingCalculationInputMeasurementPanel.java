package service.calculation.collect.input.ui.swing;

import model.dto.Channel;
import model.dto.ControlPoints;
import model.dto.Sensor;
import model.ui.ButtonCell;
import model.ui.DefaultCheckBox;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.sensor.SensorRepository;
import service.calculation.collect.input.ui.CalculationInputMeasurementPanel;
import util.ObjectHelper;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.TreeMap;

import static model.ui.ButtonCell.HEADER;

public class SwingCalculationInputMeasurementPanel extends DefaultPanel implements CalculationInputMeasurementPanel {
    private static final String HEADER_TEXT_INPUT_IN_PERCENT = "% від шкали";
    private static final String HEADER_TEXT_INPUT_IN_VALUE_PREFIX = "Задано в ";
    private static final String HEADER_TEXT_MEASUREMENT_VALUES_PREFIX = "Отримані дані в ";
    private static final String AUTO_TEXT = "Автом.";

    private final DefaultCheckBox autoInputInPercent;
    private final DefaultCheckBox autoInputInValue;
    private final DefaultCheckBox[] autoMeasurementValue;

    private final DefaultTextField[] inputsInPercent;
    private final DefaultTextField[] inputsInValue;
    private final DefaultTextField[][] measurementValues;

    public SwingCalculationInputMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory,
                                                 @Nonnull Channel channel) {
        super();

        ButtonCell headerInputInPercent = new ButtonCell(HEADER, HEADER_TEXT_INPUT_IN_PERCENT);
        ButtonCell headerInputInValue = new ButtonCell(HEADER, HEADER_TEXT_INPUT_IN_VALUE_PREFIX + channel.getMeasurementValue());
        ButtonCell headerMeasurementValues = new ButtonCell(HEADER, HEADER_TEXT_MEASUREMENT_VALUES_PREFIX + channel.getMeasurementValue());

        autoInputInPercent = new DefaultCheckBox(AUTO_TEXT);
        autoInputInValue = new DefaultCheckBox(AUTO_TEXT);
        autoMeasurementValue = new DefaultCheckBox[] {
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT)
        };

    }

    private boolean setControlPointsValuesFromRepository(RepositoryFactory repositoryFactory) {
        ControlPointsRepository controlPointsRepository = repositoryFactory.getImplementation(ControlPointsRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        ControlPoints controlPoints;
        if (ObjectHelper.nonNull(controlPointsRepository, sensorRepository)) {
            Sensor sensor = sensorRepository.get(channel.getCode());
            if (Objects.nonNull(sensor)) {
                String name = ControlPoints.createName(sensor.getType(), channel.getRangeMin(), channel.getRangeMax());
                controlPoints = controlPointsRepository.get(name);
                if
            }
        }
    }

    @Override
    public TreeMap<Double, Double> getInputs() {
        return null;
    }

    @Override
    public double[][] getMeasurementValues() {
        return new double[0][];
    }

    @Override
    public void setInputs(TreeMap<Double, Double> inputs) {

    }

    @Override
    public void setMeasurementValues(double[][] values) {

    }
}
