package ui.calculate.measurement.complexElements;

import model.Channel;

import javax.swing.*;
import java.awt.*;

public abstract class MeasurementPanel extends JPanel {
    protected final Channel channel;

    protected double[] values;

    protected JButton[] columnsHeader;
    protected JButton[] labelPercent;
    protected JButton[] labelValue;
    protected JButton[] motions;

    protected JTextField[] userMeasurements;

    public MeasurementPanel(LayoutManager layout, Channel channel){
        super(layout);
        this.channel = channel;
    }

    public double[] getControlPointsValues() {
        return this.values;
    }

    public void setValues(double[]values) {
        for (int x=0;x<this.userMeasurements.length;x++){
            this.userMeasurements[x].setText(String.valueOf(values[x]));
        }
    }

    protected abstract void createElements();
    protected abstract void build();

    public abstract double[] getValues();

    protected static class Cell extends GridBagConstraints {

        protected Cell(int x, int y){
            super();
            this.fill = BOTH;

            this.gridx = x;
            this.gridy = y;
        }

        protected Cell(int x, int y, int height){
            super();
            this.fill = BOTH;


            this.gridheight = height;
            this.gridx = x;
            this.gridy = y;
        }
    }
}