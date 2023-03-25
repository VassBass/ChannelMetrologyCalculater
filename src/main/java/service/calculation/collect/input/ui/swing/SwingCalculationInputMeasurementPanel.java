package service.calculation.collect.input.ui.swing;

import model.dto.Channel;
import model.dto.ControlPoints;
import model.dto.Sensor;
import model.ui.ButtonCell;
import model.ui.DefaultCheckBox;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.sensor.SensorRepository;
import service.calculation.collect.input.ui.CalculationInputMeasurementPanel;
import util.ObjectHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputMeasurementPanel extends DefaultPanel implements CalculationInputMeasurementPanel {
    private static final String HEADER_TEXT_INPUT_IN_PERCENT = "% від шкали";
    private static final String HEADER_TEXT_INPUT_IN_VALUE_PREFIX = "Задано в ";
    private static final String HEADER_TEXT_MEASUREMENT_VALUES_PREFIX = "Отримані дані в ";
    private static final String AUTO_TEXT = "Автом.";
    private static final String HEADER_STEP = "Хід";

    private final DefaultCheckBox autoInputInPercent;
    private final DefaultCheckBox autoInputInValue;
    private final DefaultCheckBox[] autoMeasurementValue;

    private DefaultTextField[] inputsInPercent;
    private DefaultTextField[] inputsInValue;
    private DefaultTextField[][] measurementValues;

    public SwingCalculationInputMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull Channel channel) {
        super();

        ButtonCell headerInputInPercent = new ButtonCell(HEADER, HEADER_TEXT_INPUT_IN_PERCENT);
        ButtonCell headerInputInValue = new ButtonCell(HEADER, HEADER_TEXT_INPUT_IN_VALUE_PREFIX + channel.getMeasurementValue());
        ButtonCell headerMeasurementValues = new ButtonCell(HEADER, HEADER_TEXT_MEASUREMENT_VALUES_PREFIX + channel.getMeasurementValue());
        ButtonCell headerStep = new ButtonCell(HEADER, HEADER_STEP);

        autoInputInPercent = new DefaultCheckBox(AUTO_TEXT);
        autoInputInValue = new DefaultCheckBox(AUTO_TEXT);
        autoMeasurementValue = new DefaultCheckBox[] {
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT),
                new DefaultCheckBox(AUTO_TEXT)
        };
        autoInputInPercent.setSelected(true);
        autoInputInValue.setSelected(true);
        for (int i = 1; i < autoMeasurementValue.length; i++) {
            autoMeasurementValue[i].setSelected(true);
        }

        TreeMap<Double, Double> input = getDefaultInput(repositoryFactory, channel);
        inputsInPercent = new DefaultTextField[input.size()];
        inputsInValue = new DefaultTextField[input.size()];
        measurementValues = new DefaultTextField[5][input.size() * 2];

        ButtonCell[] steps = new ButtonCell[input.size() * 2];

        int index = 0;
        for (Map.Entry<Double, Double> entry : input.entrySet()) {
            inputsInPercent[index] = new DefaultTextField(4, String.valueOf(entry.getKey()), null);
            inputsInValue[index] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);

            int i = index * 2;
            measurementValues[0][i] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[0][i + 1] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[1][i] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[1][i + 1] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[2][i] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[2][i + 1] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[3][i] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[3][i + 1] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[4][i] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);
            measurementValues[4][i + 1] = new DefaultTextField(4, String.valueOf(entry.getValue()), null);

            steps[i] = new ButtonCell(SIMPLE, "П");
            steps[i + 1] = new ButtonCell(SIMPLE, "З");

            index++;
        }

        //header
        this.add(headerInputInPercent, new CellBuilder().x(0).y(0).width(1).height(2).build());
        this.add(headerInputInValue, new CellBuilder().x(1).y(0).width(1).height(2).build());
        this.add(headerStep, new CellBuilder().x(2).y(0).width(1).height(3).build());
        this.add(headerMeasurementValues, new CellBuilder().x(3).y(0).width(5).height(1).build());
        this.add(new ButtonCell(SIMPLE, "1"), new CellBuilder().x(3).y(1).width(1).height(1).build());
        this.add(new ButtonCell(SIMPLE, "2"), new CellBuilder().x(4).y(1).width(1).height(1).build());
        this.add(new ButtonCell(SIMPLE, "3"), new CellBuilder().x(5).y(1).width(1).height(1).build());
        this.add(new ButtonCell(SIMPLE, "4"), new CellBuilder().x(6).y(1).width(1).height(1).build());
        this.add(new ButtonCell(SIMPLE, "5"), new CellBuilder().x(7).y(1).width(1).height(1).build());

        //check boxes
        this.add(autoInputInPercent, new CellBuilder().x(0).y(2).width(1).height(1).build());
        this.add(autoInputInValue, new CellBuilder().x(1).y(2).width(1).height(1).build());
        this.add(autoMeasurementValue[0], new CellBuilder().x(3).y(2).width(1).height(1).build());
        this.add(autoMeasurementValue[1], new CellBuilder().x(4).y(2).width(1).height(1).build());
        this.add(autoMeasurementValue[2], new CellBuilder().x(5).y(2).width(1).height(1).build());
        this.add(autoMeasurementValue[3], new CellBuilder().x(6).y(2).width(1).height(1).build());
        this.add(autoMeasurementValue[4], new CellBuilder().x(7).y(2).width(1).height(1).build());

        //values
        int y = 3;
        for (index = 0; index < inputsInPercent.length; index++) {
            this.add(inputsInPercent[index], new CellBuilder().x(0).y(y).width(1).height(2).build());
            this.add(inputsInValue[index], new CellBuilder().x(1).y(y).width(1).height(2).build());

            int i = index * 2;
            this.add(steps[i], new CellBuilder().x(2).y(y).width(1).height(1).build());
            this.add(steps[i + 1], new CellBuilder().x(2).y(y + 1).width(1).height(1).build());
            this.add(measurementValues[0][i], new CellBuilder().x(3).y(y).width(1).height(1).build());
            this.add(measurementValues[0][i + 1], new CellBuilder().x(3).y(y + 1).width(1).height(1).build());
            this.add(measurementValues[1][i], new CellBuilder().x(4).y(y).width(1).height(1).build());
            this.add(measurementValues[1][i + 1], new CellBuilder().x(4).y(y + 1).width(1).height(1).build());
            this.add(measurementValues[2][i], new CellBuilder().x(5).y(y).width(1).height(1).build());
            this.add(measurementValues[2][i + 1], new CellBuilder().x(5).y(y + 1).width(1).height(1).build());
            this.add(measurementValues[3][i], new CellBuilder().x(6).y(y).width(1).height(1).build());
            this.add(measurementValues[3][i + 1], new CellBuilder().x(6).y(y + 1).width(1).height(1).build());
            this.add(measurementValues[4][i], new CellBuilder().x(7).y(y).width(1).height(1).build());
            this.add(measurementValues[4][i + 1], new CellBuilder().x(7).y(y + 1).width(1).height(1).build());
            y +=2;
        }
    }

    private TreeMap<Double, Double> getDefaultInput(RepositoryFactory repositoryFactory, Channel channel) {
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
        if (Objects.nonNull(controlPoints)) {
            return new TreeMap<>(controlPoints.getValues());
        } else {
            double value5 = channel.getRangeMin() + ((channel.calculateRange() / 100) * 5);
            double value50 = channel.getRangeMin() + (channel.calculateRange() / 2);
            double value95 = channel.getRangeMax() - ((channel.calculateRange() / 100) * 5);
            TreeMap<Double, Double> input = new TreeMap<>();
            input.put(5.0, value5);
            input.put(50.0, value50);
            input.put(95.0, value95);
            return input;
        }
    }

    @Override
    public TreeMap<Double, Double> getInputs() {
        TreeMap<Double, Double> result = new TreeMap<>();
        for (int i = 0; i < inputsInPercent.length; i++) {
            String percent = inputsInPercent[i].getText();
            String value = inputsInValue[i].getText();
            if (StringHelper.isDouble(percent) && StringHelper.isDouble(value)) {
                result.put(Double.parseDouble(percent), Double.parseDouble(value));
            } else return null;
        }
        return result;
    }

    @Override
    public double[][] getMeasurementValues() {
        double[][] result = new double[5][inputsInPercent.length * 2];
        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {
                String val = measurementValues[x][y].getText();
                if (StringHelper.isDouble(val)) {
                    result[x][y] = Double.parseDouble(val);
                } else return null;
            }
        }
        return result;
    }
}
