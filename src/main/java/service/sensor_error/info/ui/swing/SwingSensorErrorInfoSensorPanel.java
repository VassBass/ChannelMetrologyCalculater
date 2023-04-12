package service.sensor_error.info.ui.swing;

import model.ui.*;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor.SensorRepository;
import service.sensor_error.info.ui.SensorErrorInfoSensorPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static javax.swing.SwingConstants.RIGHT;
import static util.StringHelper.FOR_LAST_ZERO;

public class SwingSensorErrorInfoSensorPanel extends TitledPanel implements SensorErrorInfoSensorPanel {
    private static final String TITLE_TEXT = "Дані ПВП";
    private static final String SENSOR_TOOLTIP_TEXT = "<html>" +
            "Дана формула похибки буде доступна для всіх ПВП обраного типу " +
            "<br>та якщо діапазон каналу буде попадати у даний діапазон" +
            "<br>(з урахуванням перетвореннь вимірювальних величин)" +
            "<br>тобто при введених данних:" +
            "<br>тип = dTrance p02, діапазон = 0...1000 Па" +
            "<br>буде запропоновано для:" +
            "<br>dTrance з діапазоном каналу 0...250 Па" +
            "<br>dTrance з діапазоном каналу 0...0.5 кПа" +
            "<br>НЕ буде запропоновано:" +
            "<br>PR-50G-PCV з діапазоном каналу 0...250 Па" +
            "<br>dTrance з діапазоном каналу -100...250 Па" +
            "</html>";
    private static final String SENSOR_TYPES_TEXT = "Тип ПВП";
    private static final String RANGE_TEXT = "Діапазон";

    private final TitledComboBox sensorTypes;
    private final DefaultTextField rangeMin;
    private final DefaultTextField rangeMax;
    private final DefaultComboBox measurementValue;
    private final TitledPanel rangePanel;
    private final Color defTitleColor;

    public SwingSensorErrorInfoSensorPanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(TITLE_TEXT, Color.BLACK);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        sensorTypes = new TitledComboBox(true, SENSOR_TYPES_TEXT);
        measurementValue = new DefaultComboBox(false);
        rangeMin = new DefaultTextField(5, RIGHT);
        rangeMax = new DefaultTextField(5);

        rangePanel = new TitledPanel(RANGE_TEXT);
        rangePanel.add(rangeMin, new CellBuilder().x(0).build());
        rangePanel.add(new DefaultLabel("..."), new CellBuilder().x(1).build());
        rangePanel.add(rangeMax, new CellBuilder().x(2).build());
        rangePanel.add(measurementValue, new CellBuilder().x(3).build());

        defTitleColor = ((TitledBorder) rangePanel.getBorder()).getTitleColor();

        sensorTypes.setList(new ArrayList<>(sensorRepository.getAllTypes()));
        rangeMin.setText("0.00");
        rangeMax.setText("100.00");
        measurementValue.setList(Arrays.asList(measurementRepository.getAllValues()));

        this.setToolTipText(SENSOR_TOOLTIP_TEXT);

        this.add(sensorTypes, new CellBuilder().x(0).build());
        this.add(rangePanel, new CellBuilder().x(1).build());
    }

    @Override
    public void setSensorType(@Nonnull String type) {
        sensorTypes.setSelectedItem(type);
    }

    @Override
    public void setRangeMin(double min) {
        rangeMin.setText(StringHelper.roundingDouble(min, FOR_LAST_ZERO));
    }

    @Override
    public void setRangeMax(double max) {
        rangeMax.setText(StringHelper.roundingDouble(max, FOR_LAST_ZERO));
    }

    @Override
    public void setRange(double r1, double r2) {
        double min = Math.min(r1, r2);
        double max = Math.max(r1, r2);
        rangeMin.setText(StringHelper.roundingDouble(min, FOR_LAST_ZERO));
        rangeMax.setText(StringHelper.roundingDouble(max, FOR_LAST_ZERO));
    }

    @Override
    public void setMeasurementValue(@Nonnull String value) {
        measurementValue.setSelectedItem(value);
    }

    @Override
    public String getSensorType() {
        return sensorTypes.getSelectedString();
    }

    @Override
    public double getRangeMin() {
        String value = rangeMin.getText();
        if (StringHelper.isDouble(value)) {
            rangePanel.setTitleColor(defTitleColor);
            return Double.parseDouble(value);
        } else {
            rangePanel.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    @Override
    public double getRangeMax() {
        String value = rangeMax.getText();
        if (StringHelper.isDouble(value)) {
            rangePanel.setTitleColor(defTitleColor);
            return Double.parseDouble(value);
        } else {
            rangePanel.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    @Override
    public String getMeasurementValue() {
        return measurementValue.getSelectedString();
    }
}
