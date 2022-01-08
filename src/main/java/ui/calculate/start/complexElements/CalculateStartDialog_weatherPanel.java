package ui.calculate.start.complexElements;

import constants.MeasurementConstants;
import constants.Strings;
import converters.VariableConverter;
import ui.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CalculateStartDialog_weatherPanel extends JPanel implements UI_Container {

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

    @Override
    public void createElements() {
        this.label = new ButtonCell(true, Strings.CONDITIONS_FOR_CONTROL);
        this.labelTemperature = new ButtonCell(false, Strings.TEMPERATURE_EXTERNAL_ENVIRONMENT);
        this.labelHumidity = new ButtonCell(false, Strings.RELATIVE_HUMIDITY);
        this.labelPressure = new ButtonCell(false, Strings.ATMOSPHERE_PRESSURE);

        this.temperatureValue = new ButtonCell(false, MeasurementConstants.DEGREE_CELSIUS.getValue());
        this.humidityValue = new ButtonCell(false, "%");
        this.pressureValue = new ButtonCell(false, "мм рт ст");

        this.temperature = new JTextField("21",5);
        this.temperature.setHorizontalAlignment(SwingConstants.CENTER);
        this.humidity = new JTextField("70",5);
        this.humidity.setHorizontalAlignment(SwingConstants.CENTER);
        this.pressure = new JTextField("750",5);
        this.pressure.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public void setReactions() {
        this.temperature.addFocusListener(focusTemperature);
        this.humidity.addFocusListener(focusHumidity);
        this.pressure.addFocusListener(focusPressure);
    }

    @Override
    public void build() {
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
            if (temperature.getText().length()>0){
                String check = VariableConverter.intString(temperature.getText());
                temperature.setText(check);
            }else{
                temperature.setText("21");
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
            if (humidity.getText().length()>0){
                String check = VariableConverter.intString(humidity.getText());
                humidity.setText(check);
            }else{
                humidity.setText("70");
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
            if (pressure.getText().length()>0){
                String check = VariableConverter.intString(pressure.getText());
                pressure.setText(check);
            }else{
                pressure.setText("750");
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
