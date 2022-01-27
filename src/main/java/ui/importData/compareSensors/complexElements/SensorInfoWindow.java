package ui.importData.compareSensors.complexElements;

import model.Sensor;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;

public class SensorInfoWindow extends JWindow {
    private static final String MEASUREMENT = "Вид вимірювання:";
    private static final String NAME = "Назва:";
    private static final String TYPE = "Тип:";
    private static final String RANGE = "Діапазон:";
    private static final String NUMBER = "Номер:";
    private static final String VALUE = "Вихідна величина:";
    private static final String ERROR_FORMULA = "Формула розрахунку допустимої похибки:";
    private final String TITLE;

    private final Sensor sensor;

    private ButtonCell title;

    private ButtonCell measurementLabel, nameLabel, typeLabel, rangeLabel, numberLabel, valueLabel, errorFormulaLabel;
    private ButtonCell measurement, name, type, range, number, value, errorFormula;

    public SensorInfoWindow(String title, JDialog parent, Sensor sensor){
        super(parent);
        this.TITLE = title;
        this.sensor = sensor;

        this.createElements();
        this.build();
    }

    private void createElements(){
        this.title = new ButtonCell(Color.YELLOW, Color.BLACK, this.TITLE);
        this.measurementLabel = new ButtonCell(true, MEASUREMENT);
        this.nameLabel = new ButtonCell(true, NAME);
        this.typeLabel = new ButtonCell(true, TYPE);
        this.rangeLabel = new ButtonCell(true, RANGE);
        this.valueLabel = new ButtonCell(true, VALUE);
        this.numberLabel = new ButtonCell(true, NUMBER);
        this.errorFormulaLabel = new ButtonCell(true, ERROR_FORMULA);

        this.measurement = new ButtonCell(false, this.sensor.getMeasurement());
        this.measurementLabel.setToolTipText(this.sensor.getMeasurement());
        this.measurement.setToolTipText(this.sensor.getMeasurement());

        this.name = new ButtonCell(false, this.sensor.getName());
        this.nameLabel.setToolTipText(this.sensor.getName());
        this.name.setToolTipText(this.sensor.getName());

        this.type = new ButtonCell(false, this.sensor.getType());
        this.typeLabel.setToolTipText(this.sensor.getType());
        this.type.setToolTipText(this.sensor.getType());

        String r = " - ";
        if (this.sensor.getRange() > 0) r = this.sensor.getRangeMin() + " - " + this.sensor.getRangeMax();
        this.range = new ButtonCell(false, r);
        this.rangeLabel.setToolTipText(r);
        this.range.setToolTipText(r);

        String v = this.sensor.getValue();
        if (v.length() == 0) v = " - ";
        this.value = new ButtonCell(false, v);
        this.valueLabel.setToolTipText(v);
        this.value.setToolTipText(v);

        String n = this.sensor.getNumber();
        if (n.length() == 0) n = " - ";
        this.number = new ButtonCell(false, n);
        this.numberLabel.setToolTipText(n);
        this.number.setToolTipText(n);

        this.errorFormula = new ButtonCell(false, this.sensor.getErrorFormula());
        this.errorFormulaLabel.setToolTipText(this.sensor.getErrorFormula());
        this.errorFormula.setToolTipText(this.sensor.getErrorFormula());
    }

    private void build(){
        this.setSize(300,400);
        this.setContentPane(new MainPanel());
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(title, new Cell(0));
            this.add(measurementLabel, new Cell(1));
            this.add(measurement, new Cell(2));
            this.add(nameLabel, new Cell(3));
            this.add(name, new Cell(4));
            this.add(typeLabel, new Cell(5));
            this.add(type, new Cell(6));
            this.add(rangeLabel, new Cell(7));
            this.add(range, new Cell(8));
            this.add(valueLabel, new Cell(9));
            this.add(value, new Cell(10));
            this.add(numberLabel, new Cell(11));
            this.add(number, new Cell(12));
            this.add(errorFormulaLabel, new Cell(13));
            this.add(errorFormula, new Cell(14));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int y){
                super();
                this.fill = BOTH;
                this.weightx = 1.0;

                this.gridy = y;
            }
        }
    }
}