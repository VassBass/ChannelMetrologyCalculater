package ui.calculate.start.complexElements;

import constants.MeasurementConstants;
import converters.VariableConverter;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CalculateStartDialog_weatherPanel extends JPanel {
    private static final String CONDITIONS_FOR_CONTROL = "Умови проведення контролю";
    private static final String TEMPERATURE_EXTERNAL_ENVIRONMENT = "Температура навколишнього середовища";
    private static final String RELATIVE_HUMIDITY = "Відносна вологість повітря";
    private static final String ATMOSPHERE_PRESSURE = "Атмосферний тиск";
    private static final String PERCENT = "%";
    private static final String ATMOSPHERE_PRESSURE_VALUE = "мм рт ст";
    private static final String DEFAULT_TEMPERATURE_VALUE = "21";
    private static final String DEFAULT_PRESSURE_VALUE = "750";
    private static final String DEFAULT_HUMIDITY_VALUE = "70";

    private ButtonCell label;
    private ButtonCell labelTemperature;
    private ButtonCell labelHumidity;
    private ButtonCell labelPressure;
    private ButtonCell temperatureValue;
    private ButtonCell humidityValue;
    private ButtonCell pressureValue;

    public JTextField temperature;
    public JTextField humidity;
    public JTextField pressure;

    public CalculateStartDialog_weatherPanel(){
        super(new GridBagLayout());

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.label = new ButtonCell(true, CONDITIONS_FOR_CONTROL);
        this.labelTemperature = new ButtonCell(false, TEMPERATURE_EXTERNAL_ENVIRONMENT);
        this.labelHumidity = new ButtonCell(false, RELATIVE_HUMIDITY);
        this.labelPressure = new ButtonCell(false, ATMOSPHERE_PRESSURE);

        this.temperatureValue = new ButtonCell(false, MeasurementConstants.DEGREE_CELSIUS.getValue());
        this.humidityValue = new ButtonCell(false, PERCENT);
        this.pressureValue = new ButtonCell(false, ATMOSPHERE_PRESSURE_VALUE);

        this.temperature = new JTextField(DEFAULT_TEMPERATURE_VALUE,5);
        this.temperature.setHorizontalAlignment(SwingConstants.CENTER);
        this.humidity = new JTextField(DEFAULT_HUMIDITY_VALUE,5);
        this.humidity.setHorizontalAlignment(SwingConstants.CENTER);
        this.pressure = new JTextField(DEFAULT_PRESSURE_VALUE,5);
        this.pressure.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setReactions() {
        this.temperature.addFocusListener(this.focusTemperature);
        this.humidity.addFocusListener(this.focusHumidity);
        this.pressure.addFocusListener(this.focusPressure);
    }

    private void build() {
        this.add(this.label, new Cell(0, 0, 3));

        this.add(this.labelTemperature, new Cell(0, 1));
        this.add(this.temperature, new Cell(1, 1));
        this.add(this.temperatureValue, new Cell(2, 1));

        this.add(this.labelHumidity, new Cell(0, 2));
        this.add(this.humidity, new Cell(1, 2));
        this.add(this.humidityValue, new Cell(2, 2));

        this.add(this.labelPressure, new Cell(0, 3));
        this.add(this.pressure, new Cell(1, 3));
        this.add(this.pressureValue, new Cell(2, 3));
    }

    private final FocusListener focusTemperature = new FocusListener(){
        @Override
        public void focusGained(FocusEvent e){
            temperature.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e){
            if (temperature.getText().length() > 0){
                String check = VariableConverter.intString(temperature.getText());
                temperature.setText(check);
            }else{
                temperature.setText(DEFAULT_TEMPERATURE_VALUE);
            }
        }
    };

    private final FocusListener focusHumidity = new FocusListener(){
        @Override
        public void focusGained(FocusEvent e){
            humidity.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e){
            if (humidity.getText().length() > 0){
                String check = VariableConverter.intString(humidity.getText());
                humidity.setText(check);
            }else{
                humidity.setText(DEFAULT_HUMIDITY_VALUE);
            }
        }
    };

    private final FocusListener focusPressure = new FocusListener(){
        @Override
        public void focusGained(FocusEvent e){
            pressure.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e){
            if (pressure.getText().length() > 0){
                String check = VariableConverter.intString(pressure.getText());
                pressure.setText(check);
            }else{
                pressure.setText(DEFAULT_PRESSURE_VALUE);
            }
        }
    };

    private static class Cell extends GridBagConstraints {

        protected Cell(int x, int y){
            super();

            this.fill = BOTH;

            this.gridx = x;
            this.gridy = y;
        }

        protected Cell(int x, int y, int width){
            super();

            this.fill = BOTH;

            this.gridwidth = width;
            this.gridx = x;
            this.gridy = y;
        }
    }
}