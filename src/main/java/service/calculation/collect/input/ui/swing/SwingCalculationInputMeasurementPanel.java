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

    private DefaultTextField[] inputsInPercent;
    private DefaultTextField[] inputsInValue;
    private DefaultTextField[][] measurementValues;

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

        ControlPointsRepository controlPointsRepository = repositoryFactory.getImplementation(ControlPointsRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        ControlPoints controlPoints = null;
        if (ObjectHelper.nonNull(controlPointsRepository, sensorRepository)) {
            Sensor sensor = sensorRepository.get(channel.getCode());
            if (Objects.nonNull(sensor)) {
                String controlPointsName = ControlPoints.createName(sensor.getType(), channel.getRangeMin(), channel.getRangeMax());
                controlPoints = controlPointsRepository.get(controlPointsName);
            }
        }
        if (Objects.isNull(controlPoints)) buildDefaultPanel(channel);
        else buildPanel(controlPoints);
    }

    private void buildPanel(ControlPoints controlPoints) {

    }

    private void buildDefaultPanel(Channel channel) {
        inputsInPercent = new DefaultTextField[] {
                new DefaultTextField(4, "5.0", null),
                new DefaultTextField(4, "50.0", null),
                new DefaultTextField(4, "95.0", null)
        };
        double value5 = channel.getRangeMin() + ((channel.calculateRange() / 100) * 5);
        double value50 = channel.getRangeMin() + (channel.calculateRange() / 2);
        double value95 = channel.getRangeMax() - ((channel.calculateRange() / 100) * 5);
        inputsInValue = new DefaultTextField[] {
                new DefaultTextField(4, String.valueOf(value5), null),
                new DefaultTextField(4, String.valueOf(value50), null),
                new DefaultTextField(4, String.valueOf(value95), null)
        };
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
