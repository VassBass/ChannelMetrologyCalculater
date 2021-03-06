package ui.calculate.measurement.complexElements;

import application.Application;
import converters.VariableConverter;
import model.Channel;
import model.Measurement;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class ConsumptionPanel_ROSEMOUNT extends MeasurementPanel {

    public ConsumptionPanel_ROSEMOUNT(Channel channel){
        super(new GridBagLayout(), channel);
        this.createElements();
        this.build();
    }

    @Override
    protected void createElements() {
        String value = this.channel._getMeasurementValue();
        String columnValue = "Задано в [" + value + "]";
        String columnMeasurement = "Отримані дані в [" + value + "]";
        String columnMotion = "Хід";
        String motionUp = "Прямий";
        String motionDown = "Зворотній";

        this.columnsHeader = new JButton[3];
        this.columnsHeader[0] = new ButtonCell(true, columnValue);
        this.columnsHeader[1] = new ButtonCell(true, columnMotion);
        this.columnsHeader[2] = new ButtonCell(true, columnMeasurement);

        this.values = Application.context.controlPointsValuesService.getValues(
                this.channel.getSensor().getType(), this.channel.getRangeMin(), this.channel.getRangeMax());

        if (this.values == null){
            double value0 = 0D;
            double value91 = 0.91;
            double value305 = 3.05;
            double value914 = 9.14;
            if (value.equals(Measurement.CM_S)) value91 *= 100;
            if (value.equals(Measurement.CM_S)) value305 *= 100;
            if (value.equals(Measurement.CM_S)) value914 *= 100;

            this.values = new double[]{value0, value91, value305, value914};
        }

        this.labelValue = new JButton[4];
        this.labelValue[0] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[0], Locale.ENGLISH) + value);
        this.labelValue[1] = new ButtonCell(false,VariableConverter.roundingDouble2(this.values[1], Locale.ENGLISH) + value);
        this.labelValue[2] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[2], Locale.ENGLISH) + value);
        this.labelValue[3] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[3], Locale.ENGLISH) + value);

        this.motions = new JButton[8];
        this.motions[0] = new ButtonCell(false, motionUp);
        this.motions[1] = new ButtonCell(false, motionDown);
        this.motions[2] = new ButtonCell(false, motionUp);
        this.motions[3] = new ButtonCell(false, motionDown);
        this.motions[4] = new ButtonCell(false, motionUp);
        this.motions[5] = new ButtonCell(false, motionDown);
        this.motions[6] = new ButtonCell(false, motionUp);
        this.motions[7] = new ButtonCell(false, motionDown);

        this.userMeasurements = new JTextField[8];
        for (int x=0;x<this.userMeasurements.length;x++){
            this.userMeasurements[x] = new JTextField(5);
            this.userMeasurements[x].setHorizontalAlignment(SwingConstants.CENTER);
            this.userMeasurements[x].addFocusListener(focusMeasurement);
        }
        this.userMeasurements[0].setText(VariableConverter.roundingDouble3(this.values[0], Locale.ENGLISH));
        this.userMeasurements[1].setText(VariableConverter.roundingDouble3(this.values[0], Locale.ENGLISH));
        this.userMeasurements[2].setText(VariableConverter.roundingDouble3(this.values[1], Locale.ENGLISH));
        this.userMeasurements[3].setText(VariableConverter.roundingDouble3(this.values[1], Locale.ENGLISH));
        this.userMeasurements[4].setText(VariableConverter.roundingDouble3(this.values[2], Locale.ENGLISH));
        this.userMeasurements[5].setText(VariableConverter.roundingDouble3(this.values[2], Locale.ENGLISH));
        this.userMeasurements[6].setText(VariableConverter.roundingDouble3(this.values[3], Locale.ENGLISH));
        this.userMeasurements[7].setText(VariableConverter.roundingDouble3(this.values[3], Locale.ENGLISH));
    }

    @Override
    protected void build() {
        this.add(this.columnsHeader[0], new ConsumptionPanel.Cell(0,0));
        this.add(this.columnsHeader[1], new ConsumptionPanel.Cell(1,0));
        this.add(this.columnsHeader[2], new ConsumptionPanel.Cell(2,0));

        this.add(this.labelValue[0], new ConsumptionPanel.Cell(0,1,2));
        this.add(this.labelValue[1], new ConsumptionPanel.Cell(0,3,2));
        this.add(this.labelValue[2], new ConsumptionPanel.Cell(0,5,2));
        this.add(this.labelValue[3], new ConsumptionPanel.Cell(0,7,2));

        this.add(this.motions[0], new ConsumptionPanel.Cell(1,1));
        this.add(this.motions[1], new ConsumptionPanel.Cell(1,2));
        this.add(this.motions[2], new ConsumptionPanel.Cell(1,3));
        this.add(this.motions[3], new ConsumptionPanel.Cell(1,4));
        this.add(this.motions[4], new ConsumptionPanel.Cell(1,5));
        this.add(this.motions[5], new ConsumptionPanel.Cell(1,6));
        this.add(this.motions[6], new ConsumptionPanel.Cell(1,7));
        this.add(this.motions[7], new ConsumptionPanel.Cell(1,8));

        this.add(this.userMeasurements[0], new ConsumptionPanel.Cell(2,1));
        this.add(this.userMeasurements[1], new ConsumptionPanel.Cell(2,2));
        this.add(this.userMeasurements[2], new ConsumptionPanel.Cell(2,3));
        this.add(this.userMeasurements[3], new ConsumptionPanel.Cell(2,4));
        this.add(this.userMeasurements[4], new ConsumptionPanel.Cell(2,5));
        this.add(this.userMeasurements[5], new ConsumptionPanel.Cell(2,6));
        this.add(this.userMeasurements[6], new ConsumptionPanel.Cell(2,7));
        this.add(this.userMeasurements[7], new ConsumptionPanel.Cell(2,8));
    }

    @Override
    public double[] getValues() {
        double[]val = new double[8];
        for (int x=0;x<val.length;x++){
            val[x] = Double.parseDouble(this.userMeasurements[x].getText());
        }
        return val;
    }

    private final FocusListener focusMeasurement = new FocusListener(){
        @Override
        public void focusGained(FocusEvent e){
            JTextField cell = (JTextField) e.getSource();
            cell.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e){
            String value = channel._getMeasurementValue();
            JTextField cell = (JTextField) e.getSource();
            if (cell.getText().length()==0 || cell.getText().equals("-")){
                double value0 = 0D;
                double value91 = 0.91;
                double value305 = 3.05;
                double value914 = 9.14;
                if (value.equals(Measurement.CM_S)) value91 = value91 * 100;
                if (value.equals(Measurement.CM_S)) value305 = value305 * 100;
                if (value.equals(Measurement.CM_S)) value914 = value914 * 100;
                for (int x=0;x<userMeasurements.length;x++){
                    if (cell.equals(userMeasurements[x])){
                        switch (x){
                            case 0:
                            case 1:
                                cell.setText(String.valueOf(value0));
                                break;
                            case 2:
                            case 3:
                                cell.setText(String.valueOf(value91));
                                break;
                            case 4:
                            case 5:
                                cell.setText(String.valueOf(value305));
                                break;
                            case 6:
                            case 7:
                                cell.setText(String.valueOf(value914));
                                break;
                        }
                    }
                }
            }else{
                String check = VariableConverter.doubleString(cell.getText());
                double d = Double.parseDouble(check);
                cell.setText(VariableConverter.roundingDouble3(d, Locale.ENGLISH));
            }
        }
    };
}