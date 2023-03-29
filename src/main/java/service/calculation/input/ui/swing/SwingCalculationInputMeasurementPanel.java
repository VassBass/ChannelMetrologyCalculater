package service.calculation.input.ui.swing;

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
import service.calculation.input.ui.CalculationInputMeasurementPanel;
import util.ObjectHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;

import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationInputMeasurementPanel extends DefaultPanel implements CalculationInputMeasurementPanel {
    private static final int INPUTS_IN_PERCENT = -1;
    private static final int INPUTS_IN_VALUE = 1;
    private static final int INPUTS_BOTH = 0;

    private static final String HEADER_TEXT_INPUT_IN_PERCENT = "% від шкали";
    private static final String HEADER_TEXT_INPUT_IN_VALUE_PREFIX = "Задано в ";
    private static final String HEADER_TEXT_MEASUREMENT_VALUES_PREFIX = "Отримані дані в ";
    private static final String AUTO_TEXT = "Автом.";
    private static final String HEADER_STEP = "Хід";

    private final TreeMap<Double, Double> input;

    private final DefaultCheckBox autoInputInPercent;
    private final DefaultCheckBox autoInputInValue;
    private final DefaultCheckBox[] autoMeasurementValue;

    private final DefaultTextField[] inputsInPercent;
    private final DefaultTextField[] inputsInValue;
    private DefaultTextField[][] measurementValues;

    private int percentDecimalPoint = 2;
    private int valueDecimalPoint = 2;

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
        for (DefaultCheckBox a : autoMeasurementValue) {
            a.setSelected(true);
        }

        input = createDefaultInput(repositoryFactory, channel);
        inputsInPercent = new DefaultTextField[input.size()];
        inputsInValue = new DefaultTextField[input.size()];

        setInputs(input, INPUTS_BOTH);
        setAutoMeasurements(0, 1, 2, 3, 4);

        ButtonCell[] steps = new ButtonCell[input.size() * 2];
        for (int index = 0; index < input.size(); index++) {
            int i = index * 2;
            steps[i] = new ButtonCell(SIMPLE, "П");
            steps[++i] = new ButtonCell(SIMPLE, "З");
        }

        autoInputInPercent.addItemListener(e -> {
            if (autoInputInPercent.isSelected()) {
                setInputs(input, INPUTS_IN_PERCENT);
            } else {
                for (DefaultTextField v : inputsInPercent) v.setEnabled(true);
            }
        });
        autoInputInValue.addItemListener(e -> {
            if (autoInputInValue.isSelected()) {
                setInputs(input, INPUTS_IN_VALUE);
            } else {
                for (DefaultTextField v : inputsInValue) v.setEnabled(true);
            }
        });
        for (int index = 0; index < autoMeasurementValue.length; index++) {
            final int finalIndex = index;
            autoMeasurementValue[index].addItemListener(e -> {
                if (autoMeasurementValue[finalIndex].isSelected()) {
                    setAutoMeasurements(finalIndex);
                    for (DefaultTextField v : measurementValues[finalIndex]) v.setEnabled(false);
                } else {
                    for (DefaultTextField v : measurementValues[finalIndex]) v.setEnabled(true);
                }
            });
        }

        for (DefaultTextField t : inputsInPercent) t.setFocusListener(percentFocusListener);
        for (DefaultTextField t : inputsInValue) t.setFocusListener(valuesFocusListener);
        for (DefaultTextField[] array : measurementValues) {
            for (DefaultTextField t : array) t.setFocusListener(valuesFocusListener);
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
        for (int index = 0; index < inputsInPercent.length; index++) {
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

    public void setDecimalPoint(int percentPoints, int valuePoints) {
        percentDecimalPoint = percentPoints;
        valueDecimalPoint = valuePoints;
        Arrays.stream(inputsInPercent).forEach(e -> e.setText(StringHelper.roundingDouble(Double.parseDouble(e.getText()), percentPoints)));
        Arrays.stream(inputsInValue).forEach(e -> e.setText(StringHelper.roundingDouble(Double.parseDouble(e.getText()), valuePoints)));
        Arrays.stream(measurementValues).forEach(e ->
                Arrays.stream(e).forEach(el -> el.setText(StringHelper.roundingDouble(Double.parseDouble(el.getText()), valuePoints))));
    }

    private void setInputs(Map<Double, Double> in, int whatInputs) {
        int index = 0;
        for (Map.Entry<Double, Double> entry : in.entrySet()) {
            if (whatInputs == INPUTS_BOTH || whatInputs == INPUTS_IN_PERCENT) {
                if (Objects.isNull(inputsInPercent[index])) inputsInPercent[index] = new DefaultTextField(4);
                double key = entry.getKey();
                inputsInPercent[index].setText(StringHelper.roundingDouble(key, percentDecimalPoint));
                inputsInPercent[index].setEnabled(false);
            }
            if (whatInputs == INPUTS_BOTH || whatInputs == INPUTS_IN_VALUE) {
                if (Objects.isNull(inputsInValue[index])) inputsInValue[index] = new DefaultTextField(4);
                inputsInValue[index].setText(StringHelper.roundingDouble(entry.getValue(), valueDecimalPoint));
                inputsInValue[index].setEnabled(false);
            }
            index++;
        }
    }

    /**
     *
     * @param numbersOfMeasurement must be 0, 1, 2, 3 or 4.
     * Where:
     * 0 is first measurement, sets values from input
     * 1 is second measurement, sets values from first measurement
     * 2 is third measurement, sets values from first measurement
     * 3 is fourth measurement, sets values from second measurement
     * 4 is fifth measurement, sets values from third measurement
     */
    private void setAutoMeasurements(@Nonnull int ... numbersOfMeasurement) {
        Arrays.sort(numbersOfMeasurement);
        for (int numberOfMeasurement : numbersOfMeasurement) {
            int index;
            switch (numberOfMeasurement) {
                case 0:
                    TreeMap<Double, Double> currentInput = getInputs();
                    if (Objects.isNull(currentInput)) currentInput = input;
                    if (Objects.isNull(currentInput)) {
                        if (Objects.nonNull(autoMeasurementValue) && Objects.nonNull(autoMeasurementValue[0])) {
                            autoMeasurementValue[0].setSelected(false);
                        }
                        break;
                    }

                    if (Objects.isNull(measurementValues)) measurementValues = new DefaultTextField[5][];
                    if (Objects.isNull(measurementValues[0])) measurementValues[0] = new DefaultTextField[currentInput.size() * 2];

                    index = 0;
                    for (Map.Entry<Double, Double> entry : currentInput.entrySet()) {
                        int i = index * 2;
                        if (Objects.isNull(measurementValues[0][i])) measurementValues[0][i] = new DefaultTextField(4);
                        if (Objects.isNull(measurementValues[0][i + 1])) measurementValues[0][i + 1] = new DefaultTextField(4);
                        measurementValues[0][i].setText(String.valueOf(entry.getValue()));
                        measurementValues[0][i + 1].setText(String.valueOf(entry.getValue()));
                        measurementValues[0][i].setEnabled(false);
                        measurementValues[0][i + 1].setEnabled(false);
                        index++;
                    }
                    break;
                case 1:
                    if (Objects.isNull(measurementValues)) measurementValues = new DefaultTextField[5][];
                    if (Objects.isNull(measurementValues[0])) setAutoMeasurements(0);
                    String[] firstValues = Arrays.stream(measurementValues[0])
                            .map(JTextComponent::getText)
                            .toArray(String[]::new);
                    if (Objects.isNull(measurementValues[1])) measurementValues[1] = new DefaultTextField[firstValues.length];

                    index = 0;
                    for (String val : firstValues) {
                        if (Objects.isNull(measurementValues[1][index])) measurementValues[1][index] = new DefaultTextField(4);
                        measurementValues[1][index].setText(val);
                        measurementValues[1][index++].setEnabled(false);
                    }
                    break;
                case 2:
                    if (Objects.isNull(measurementValues)) measurementValues = new DefaultTextField[5][];
                    if (Objects.isNull(measurementValues[0])) setAutoMeasurements(0);
                    String[] firstValuess = Arrays.stream(measurementValues[0])
                            .map(JTextComponent::getText)
                            .toArray(String[]::new);
                    if (Objects.isNull(measurementValues[2])) measurementValues[2] = new DefaultTextField[firstValuess.length];

                    index = 0;
                    for (String val : firstValuess) {
                        if (Objects.isNull(measurementValues[2][index])) measurementValues[2][index] = new DefaultTextField(4);
                        measurementValues[2][index].setText(val);
                        measurementValues[2][index++].setEnabled(false);
                    }
                    break;
                case 3:
                    if (Objects.isNull(measurementValues)) measurementValues = new DefaultTextField[5][];
                    if (Objects.isNull(measurementValues[1])) setAutoMeasurements(1);
                    String[] secondValues = Arrays.stream(measurementValues[1])
                            .map(JTextComponent::getText)
                            .toArray(String[]::new);
                    if (Objects.isNull(measurementValues[3])) measurementValues[3] = new DefaultTextField[secondValues.length];

                    index = 0;
                    for (String val : secondValues) {
                        if (Objects.isNull(measurementValues[3][index])) measurementValues[3][index] = new DefaultTextField(4);
                        measurementValues[3][index].setText(val);
                        measurementValues[3][index++].setEnabled(false);
                    }
                    break;
                case 4:
                    if (Objects.isNull(measurementValues)) measurementValues = new DefaultTextField[5][];
                    if (Objects.isNull(measurementValues[2])) setAutoMeasurements(2);
                    String[] thirdValues = Arrays.stream(measurementValues[2])
                            .map(JTextComponent::getText)
                            .toArray(String[]::new);
                    if (Objects.isNull(measurementValues[4])) measurementValues[4] = new DefaultTextField[thirdValues.length];

                    index = 0;
                    for (String val : thirdValues) {
                        if (Objects.isNull(measurementValues[4][index])) measurementValues[4][index] = new DefaultTextField(4);
                        measurementValues[4][index].setText(val);
                        measurementValues[4][index++].setEnabled(false);
                    }
                    break;
            }
        }
    }

    private TreeMap<Double, Double> createDefaultInput(RepositoryFactory repositoryFactory, Channel channel) {
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
    public TreeMap<Double, double[]> getMeasurementValues() {
        TreeMap<Double, Double> input = getInputs();
        TreeMap<Double, double[]> result = new TreeMap<>();
        if (Objects.isNull(input)) return null;

        int x = 0;
        for (Map.Entry<Double, Double> entry : input.entrySet()) {
            List<Double> output = new ArrayList<>();
            for (int y = 0; y < measurementValues.length; y++) {
                int i = x * 2;
                String val1 = measurementValues[x][i].getText();
                String val2 = measurementValues[x][i + 1].getText();
                if (!StringHelper.isDouble(val1)) return null;
                if (!StringHelper.isDouble(val2)) return null;
                output.add(Double.parseDouble(val1));
                output.add(Double.parseDouble(val2));
            }
            result.put(entry.getValue(), output.stream().mapToDouble(Double::doubleValue).toArray());
            x++;
        }
        return result;
    }

    private String valuesBuffer;
    private final FocusListener valuesFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
            valuesBuffer = source.getText();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            String text = source.getText();
            if (StringHelper.isDouble(text)) {
                source.setText(StringHelper.roundingDouble(Double.parseDouble(text), valueDecimalPoint));
            } else {
                source.setText(valuesBuffer);
            }

            List<Integer> indexes = new ArrayList<>();
            for (int x = 0; x < autoMeasurementValue.length; x++) {
                if (autoMeasurementValue[x].isSelected()) {
                    indexes.add(x);
                }
            }
            setAutoMeasurements(indexes.stream().mapToInt(Integer::intValue).toArray());
        }
    };

    private String percentBuffer;
    private final FocusListener percentFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
            percentBuffer = source.getText();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            String text = source.getText();
            if (StringHelper.isDouble(text)) {
                source.setText(StringHelper.roundingDouble(Double.parseDouble(text), percentDecimalPoint));
            } else {
                source.setText(percentBuffer);
            }
        }
    };
}
