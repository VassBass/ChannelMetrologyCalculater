package ui.controlPointsValues.complexElements;

import converters.VariableConverter;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class ControlPointsPanel extends JPanel {
    private double rangeMin, rangeMax;
    private double[] percentValues;
    protected ButtonCell[] percentValuesCells;
    protected JTextField[] valuesCells;

    public ControlPointsPanel(double rangeMin, double rangeMax){
        super(new GridBagLayout());
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    protected void create(double[]percentValues){
        this.percentValues = percentValues;
        this.percentValuesCells = new ButtonCell[percentValues.length];
        this.valuesCells = new JTextField[percentValues.length];
        for (int i=0;i<percentValues.length;i++){
            String val = VariableConverter.roundingDouble3(percentValues[i], Locale.ENGLISH) + "%";
            this.percentValuesCells[i] = new ButtonCell(true, val);

            JTextField cell = new JTextField(5);
            cell.setHorizontalAlignment(SwingConstants.CENTER);
            cell.addFocusListener(this.focusListener);
            this.valuesCells[i] = cell;
        }

        this.setValues(null);
        this.build();
    }

    public double[]getValues(){
        double[] values = new double[this.valuesCells.length];
        for (int i=0;i<this.valuesCells.length;i++){
            JTextField cell = this.valuesCells[i];
            values[i] = Double.parseDouble(cell.getText());
        }
        return values;
    }

    public void setValues(double[]values){
        if (values == null){
            clear();
        }else {
            for (int i = 0; i < this.valuesCells.length; i++) {
                valuesCells[i].setText(VariableConverter.roundingDouble3(values[i], Locale.ENGLISH));
            }
        }
    }

    public void clear(){
        double range = this.rangeMax - this.rangeMin;
        for (int i=0;i<this.valuesCells.length;i++){
            double value = ((range / 100) * this.percentValues[i]) + this.rangeMin;
            this.valuesCells[i].setText(VariableConverter.roundingDouble3(value, Locale.ENGLISH));
        }
    }

    public void changeRange(double rangeMin, double rangeMax){
        if (this.rangeMin != rangeMin) this.rangeMin = rangeMin;
        if (this.rangeMax != rangeMax) this.rangeMax = rangeMax;
        this.clear();
    }

    private void build(){
        for (int i=0;i<this.percentValuesCells.length;i++){
            this.add(this.percentValuesCells[i], new Cell(i, 0));
            this.add(this.valuesCells[i], new Cell(i, 1));
        }
    }

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            String val = VariableConverter.doubleString(source.getText());
            source.setText(val);
        }
    };

    private static class Cell extends GridBagConstraints {
        protected Cell(int x, int y){
            this.fill = BOTH;
            this.weightx = 1D;
            this.weighty = 1D;
            this.gridx = x;
            this.gridy = y;
        }
    }
}