package ui.calculate.measurement.complexElements;

import application.Application;
import converters.VariableConverter;
import model.Channel;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class ConsumptionPanel extends MeasurementPanel {

    public ConsumptionPanel(Channel channel){
        super(new GridBagLayout(), channel);
        this.createElements();
        this.build();
    }

    @Override
    protected void createElements() {
        String value = this.channel._getMeasurementValue();
        String columnValue = "Задано в [" + value + "]";
        String columnMeasurement = "Отримані дані в [" + value + "]";
        String columnPercent = "% від шкали";
        String columnMotion = "Хід";
        String motionUp = "Прямий";
        String motionDown = "Зворотній";

        this.columnsHeader = new JButton[4];
        this.columnsHeader[0] = new ButtonCell(true, columnPercent);
        this.columnsHeader[1] = new ButtonCell(true, columnValue);
        this.columnsHeader[2] = new ButtonCell(true, columnMotion);
        this.columnsHeader[3] = new ButtonCell(true, columnMeasurement);

        int[] valuesPercent = new int[]{0, 25, 50, 75, 100};
        this.labelPercent = new JButton[5];
        this.labelPercent[0] = new ButtonCell(false, valuesPercent[0] + "%");
        this.labelPercent[1] = new ButtonCell(false, valuesPercent[1] + "%");
        this.labelPercent[2] = new ButtonCell(false, valuesPercent[2] + "%");
        this.labelPercent[3] = new ButtonCell(false, valuesPercent[3] + "%");
        this.labelPercent[4] = new ButtonCell(false, valuesPercent[4] + "%");

        this.values = Application.context.controlPointsValuesService.getValues(
                this.channel.getSensor().getType(), this.channel.getRangeMin(), this.channel.getRangeMax());

        if (this.values == null){
            double value0 = this.channel.getRangeMin();
            double value25 = ((this.channel._getRange() / 100) * 25) + this.channel.getRangeMin();
            double value50 = ((this.channel._getRange() / 100) * 50) + this.channel.getRangeMin();
            double value75 = ((this.channel._getRange() / 100) * 75) + this.channel.getRangeMin();
            double value100 = this.channel.getRangeMax();
            this.values = new double[]{value0, value25, value50, value75, value100};
        }
        this.labelValue = new JButton[5];

        this.labelValue[0] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[0], Locale.ENGLISH) + value);
        this.labelValue[1] = new ButtonCell(false,VariableConverter.roundingDouble2(this.values[1], Locale.ENGLISH) + value);
        this.labelValue[2] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[2], Locale.ENGLISH) + value);
        this.labelValue[3] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[3], Locale.ENGLISH) + value);
        this.labelValue[4] = new ButtonCell(false, VariableConverter.roundingDouble2(this.values[4], Locale.ENGLISH) + value);

        this.motions = new JButton[10];
        this.motions[0] = new ButtonCell(false, motionUp);
        this.motions[1] = new ButtonCell(false, motionDown);
        this.motions[2] = new ButtonCell(false, motionUp);
        this.motions[3] = new ButtonCell(false, motionDown);
        this.motions[4] = new ButtonCell(false, motionUp);
        this.motions[5] = new ButtonCell(false, motionDown);
        this.motions[6] = new ButtonCell(false, motionUp);
        this.motions[7] = new ButtonCell(false, motionDown);
        this.motions[8] = new ButtonCell(false, motionUp);
        this.motions[9] = new ButtonCell(false, motionDown);

        this.userMeasurements = new JTextField[10];
        for (int x=0;x<this.userMeasurements.length;x++){
            this.userMeasurements[x] = new JTextField(5);
            this.userMeasurements[x].setHorizontalAlignment(SwingConstants.CENTER);
            this.userMeasurements[x].addFocusListener(this.focusMeasurement);
        }
        this.userMeasurements[0].setText(VariableConverter.roundingDouble3(this.values[0], Locale.ENGLISH));
        this.userMeasurements[1].setText(VariableConverter.roundingDouble3(this.values[0], Locale.ENGLISH));
        this.userMeasurements[2].setText(VariableConverter.roundingDouble3(this.values[1], Locale.ENGLISH));
        this.userMeasurements[3].setText(VariableConverter.roundingDouble3(this.values[1], Locale.ENGLISH));
        this.userMeasurements[4].setText(VariableConverter.roundingDouble3(this.values[2], Locale.ENGLISH));
        this.userMeasurements[5].setText(VariableConverter.roundingDouble3(this.values[2], Locale.ENGLISH));
        this.userMeasurements[6].setText(VariableConverter.roundingDouble3(this.values[3], Locale.ENGLISH));
        this.userMeasurements[7].setText(VariableConverter.roundingDouble3(this.values[3], Locale.ENGLISH));
        this.userMeasurements[8].setText(VariableConverter.roundingDouble3(this.values[4], Locale.ENGLISH));
        this.userMeasurements[9].setText(VariableConverter.roundingDouble3(this.values[4], Locale.ENGLISH));
    }

    @Override
    protected void build() {
        this.add(this.columnsHeader[0], new Cell(0,0));
        this.add(this.columnsHeader[1], new Cell(1,0));
        this.add(this.columnsHeader[2], new Cell(2,0));
        this.add(this.columnsHeader[3], new Cell(3,0));

        this.add(this.labelPercent[0], new Cell(0,1,2));
        this.add(this.labelPercent[1], new Cell(0,3,2));
        this.add(this.labelPercent[2], new Cell(0,5,2));
        this.add(this.labelPercent[3], new Cell(0,7,2));
        this.add(this.labelPercent[4], new Cell(0,9,2));

        this.add(this.labelValue[0], new Cell(1,1,2));
        this.add(this.labelValue[1], new Cell(1,3,2));
        this.add(this.labelValue[2], new Cell(1,5,2));
        this.add(this.labelValue[3], new Cell(1,7,2));
        this.add(this.labelValue[4], new Cell(1,9,2));

        this.add(this.motions[0], new Cell(2,1));
        this.add(this.motions[1], new Cell(2,2));
        this.add(this.motions[2], new Cell(2,3));
        this.add(this.motions[3], new Cell(2,4));
        this.add(this.motions[4], new Cell(2,5));
        this.add(this.motions[5], new Cell(2,6));
        this.add(this.motions[6], new Cell(2,7));
        this.add(this.motions[7], new Cell(2,8));
        this.add(this.motions[8], new Cell(2,9));
        this.add(this.motions[9], new Cell(2,10));

        this.add(this.userMeasurements[0], new Cell(3,1));
        this.add(this.userMeasurements[1], new Cell(3,2));
        this.add(this.userMeasurements[2], new Cell(3,3));
        this.add(this.userMeasurements[3], new Cell(3,4));
        this.add(this.userMeasurements[4], new Cell(3,5));
        this.add(this.userMeasurements[5], new Cell(3,6));
        this.add(this.userMeasurements[6], new Cell(3,7));
        this.add(this.userMeasurements[7], new Cell(3,8));
        this.add(this.userMeasurements[8], new Cell(3,9));
        this.add(this.userMeasurements[9], new Cell(3,10));
    }

    @Override
    public double[] getValues() {
        double[]val = new double[10];
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
            JTextField cell = (JTextField) e.getSource();
            if (cell.getText().length()==0 || cell.getText().equals("-")){
                for (int x=0;x<userMeasurements.length;x++){
                    if (cell.equals(userMeasurements[x])){
                        switch (x){
                            case 0:
                            case 1:
                                cell.setText(String.valueOf(values[0]));
                                break;
                            case 2:
                            case 3:
                                cell.setText(String.valueOf(values[1]));
                                break;
                            case 4:
                            case 5:
                                cell.setText(String.valueOf(values[2]));
                                break;
                            case 6:
                            case 7:
                                cell.setText(String.valueOf(values[3]));
                                break;
                            case 8:
                            case 9:
                                cell.setText(String.valueOf(values[4]));
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