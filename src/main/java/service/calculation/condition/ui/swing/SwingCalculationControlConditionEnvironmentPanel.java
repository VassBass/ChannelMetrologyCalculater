package service.calculation.condition.ui.swing;

import model.dto.Measurement;
import model.ui.ButtonCell;
import model.ui.DefaultPanel;
import model.ui.DefaultTextField;
import model.ui.builder.CellBuilder;
import service.calculation.condition.ui.CalculationControlConditionEnvironmentPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static javax.swing.SwingConstants.CENTER;
import static model.ui.ButtonCell.HEADER;
import static model.ui.ButtonCell.SIMPLE;

public class SwingCalculationControlConditionEnvironmentPanel extends DefaultPanel implements CalculationControlConditionEnvironmentPanel {
    private static final String HEADER_TEXT = "Умови проведення контролю";
    private static final String TEMPERATURE_TEXT = "Температура навколишнього середовища";
    private static final String HUMIDITY_TEXT = "Відносна вологість повітря";
    private static final String PRESSURE_TEXT = "Атмосферний тиск";
    private static final String TEMPERATURE_MEASUREMENT_VALUE = Measurement.DEGREE_CELSIUS;
    private static final String HUMIDITY_MEASUREMENT_VALUE = "%";
    private static final String PRESSURE_MEASUREMENT_VALUE = Measurement.MM_ACVA;

    private final ButtonCell temperatureLabel;
    private final ButtonCell humidityLabel;
    private final ButtonCell pressureLabel;
    private final DefaultTextField temperatureValue;
    private final DefaultTextField humidityValue;
    private final DefaultTextField pressureValue;

    public SwingCalculationControlConditionEnvironmentPanel() {
        super();

        ButtonCell header = new ButtonCell(HEADER, HEADER_TEXT);
        temperatureLabel = new ButtonCell(SIMPLE, TEMPERATURE_TEXT);
        humidityLabel = new ButtonCell(SIMPLE, HUMIDITY_TEXT);
        pressureLabel = new ButtonCell(SIMPLE, PRESSURE_TEXT);
        ButtonCell temperatureMeasurementValue = new ButtonCell(SIMPLE, TEMPERATURE_MEASUREMENT_VALUE);
        ButtonCell humidityMeasurementValue = new ButtonCell(SIMPLE, HUMIDITY_MEASUREMENT_VALUE);
        ButtonCell pressureMeasurementValue = new ButtonCell(SIMPLE, PRESSURE_MEASUREMENT_VALUE);
        temperatureValue = new DefaultTextField(2, CENTER);
        humidityValue = new DefaultTextField(2, CENTER);
        pressureValue = new DefaultTextField(3, CENTER);

        temperatureValue.setFocusListener(focusGained);
        humidityValue.setFocusListener(focusGained);
        pressureValue.setFocusListener(focusGained);

        this.add(header, new CellBuilder().x(0).y(0).width(3).build());
        this.add(temperatureLabel, new CellBuilder().x(0).y(1).width(1).build());
        this.add(temperatureValue, new CellBuilder().x(1).y(1).width(1).build());
        this.add(temperatureMeasurementValue, new CellBuilder().x(2).y(1).width(1).build());
        this.add(humidityLabel, new CellBuilder().x(0).y(2).width(1).build());
        this.add(humidityValue, new CellBuilder().x(1).y(2).width(1).build());
        this.add(humidityMeasurementValue, new CellBuilder().x(2).y(2).width(1).build());
        this.add(pressureLabel, new CellBuilder().x(0).y(3).width(1).build());
        this.add(pressureValue, new CellBuilder().x(1).y(3).width(1).build());
        this.add(pressureMeasurementValue, new CellBuilder().x(2).y(3).width(1).build());
    }

    @Override
    public void setTemperature(@Nonnull String temperature) {
        temperatureValue.setText(temperature);
    }

    @Nullable
    @Override
    public String getTemperature() {
        String value = temperatureValue.getText();
        if (StringHelper.isDouble(value)) {
            temperatureLabel.setBackground(Color.WHITE);
            return value;
        } else {
            temperatureLabel.setBackground(Color.RED);
            return null;
        }
    }

    @Override
    public void setPressure(@Nonnull String pressure) {
        pressureValue.setText(pressure);
    }

    @Nullable
    @Override
    public String getPressure() {
        String value = pressureValue.getText();
        if (StringHelper.isDouble(value)) {
            pressureLabel.setBackground(Color.WHITE);
            return value;
        } else {
            pressureLabel.setBackground(Color.RED);
            return null;
        }
    }

    @Override
    public void setHumidity(@Nonnull String humidity) {
        humidityValue.setText(humidity);
    }

    @Nullable
    @Override
    public String getHumidity() {
        String value = humidityValue.getText();
        if (StringHelper.isDouble(value)) {
            humidityLabel.setBackground(Color.WHITE);
            return value;
        } else {
            humidityLabel.setBackground(Color.RED);
            return null;
        }
    }

    private final FocusAdapter focusGained = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
        }
    };
}