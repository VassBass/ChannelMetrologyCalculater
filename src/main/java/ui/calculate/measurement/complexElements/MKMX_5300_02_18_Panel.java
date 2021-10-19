package ui.calculate.measurement.complexElements;

import calibrators.Calibrator;
import constants.CalibratorType;
import constants.MeasurementConstants;
import support.Converter;
import support.Channel;
import ui.ButtonCell;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class MKMX_5300_02_18_Panel extends JPanel implements UI_Container, MeasurementPanel {
    private final Channel channel;
    private final Calibrator calibrator;

    private JButton[] columnsHeader;
    private JButton[] labelPercent;
    private JButton[] labelValue;
    private JButton[] motions;

    private JTextField[] userMeasurements;

    public MKMX_5300_02_18_Panel(Channel channel, Calibrator calibrator){
        super(new GridBagLayout());
        this.channel = channel;
        this.calibrator = calibrator;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        String value = this.channel.getMeasurement().getValue();
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

        int[] valuesPercent = new int[]{0, 5, 50, 95, 100};
        this.labelPercent = new JButton[5];
        this.labelPercent[0] = new ButtonCell(false, valuesPercent[0] + "%");
        this.labelPercent[1] = new ButtonCell(false, valuesPercent[1] + "%");
        this.labelPercent[2] = new ButtonCell(false, valuesPercent[2] + "%");
        this.labelPercent[3] = new ButtonCell(false, valuesPercent[3] + "%");
        this.labelPercent[4] = new ButtonCell(false, valuesPercent[4] + "%");

        double maxCalibratorPower = new Converter(MeasurementConstants.KG_SM2, this.channel.getMeasurement().getValueConstant()).get(-0.8);

        double value0 = this.channel.getRangeMin();
        double value5 = ((this.channel.getRange() / 100) * 5) + this.channel.getRangeMin();
        double value50 = ((this.channel.getRange() / 100) * 50) + this.channel.getRangeMin();
        double value95 = ((this.channel.getRange() / 100) * 95) + this.channel.getRangeMin();
        double value100 = this.channel.getRangeMax();
        this.labelValue = new JButton[5];
        if (this.calibrator.getName() == CalibratorType.FLUKE718_30G && value0 < maxCalibratorPower) {
            if (value5 <= maxCalibratorPower) {
                this.labelValue[0] = new ButtonCell(false, " - ");
                this.labelValue[1] = new ButtonCell(false, Converter.roundingDouble3(maxCalibratorPower, Locale.ENGLISH) + value);
            }else {
                this.labelValue[0] = new ButtonCell(false, Converter.roundingDouble3(maxCalibratorPower, Locale.ENGLISH) + value);
                this.labelValue[1] = new ButtonCell(false, Converter.roundingDouble3(value5, Locale.ENGLISH) + value);
            }
        }else {
            this.labelValue[0] = new ButtonCell(false, Converter.roundingDouble3(value0, Locale.ENGLISH) + value);
            this.labelValue[1] = new ButtonCell(false, Converter.roundingDouble3(value5, Locale.ENGLISH) + value);
        }
        this.labelValue[2] = new ButtonCell(false, Converter.roundingDouble3(value50, Locale.ENGLISH) + value);
        this.labelValue[3] = new ButtonCell(false, Converter.roundingDouble3(value95, Locale.ENGLISH) + value);
        this.labelValue[4] = new ButtonCell(false, Converter.roundingDouble3(value100, Locale.ENGLISH) + value);

        this.motions = new JButton[6];
        this.motions[0] = new ButtonCell(false, motionUp);
        this.motions[1] = new ButtonCell(false, motionDown);
        this.motions[2] = new ButtonCell(false, motionUp);
        this.motions[3] = new ButtonCell(false, motionDown);
        this.motions[4] = new ButtonCell(false, motionUp);
        this.motions[5] = new ButtonCell(false, motionDown);

        this.userMeasurements = new JTextField[8];
        for (int x=0;x<this.userMeasurements.length;x++){
            this.userMeasurements[x] = new JTextField(5);
            this.userMeasurements[x].setHorizontalAlignment(SwingConstants.CENTER);
            this.userMeasurements[x].addFocusListener(focusMeasurement);
        }
        if (this.calibrator.getName() == CalibratorType.FLUKE718_30G && value0 < maxCalibratorPower) {
            if (value5 <= maxCalibratorPower) {
                this.userMeasurements[0].setText(Converter.roundingDouble3(value0, Locale.ENGLISH));
                this.userMeasurements[0].setEnabled(false);
                this.userMeasurements[1].setText(Converter.roundingDouble3(maxCalibratorPower, Locale.ENGLISH));
                this.userMeasurements[2].setText(Converter.roundingDouble3(maxCalibratorPower, Locale.ENGLISH));
            }else {
                this.userMeasurements[0].setText(Converter.roundingDouble3(maxCalibratorPower, Locale.ENGLISH));
                this.userMeasurements[1].setText(Converter.roundingDouble3(value5, Locale.ENGLISH));
                this.userMeasurements[2].setText(Converter.roundingDouble3(value5, Locale.ENGLISH));
            }
        }else {
            this.userMeasurements[0].setText(Converter.roundingDouble3(value0, Locale.ENGLISH));
            this.userMeasurements[1].setText(Converter.roundingDouble3(value5, Locale.ENGLISH));
            this.userMeasurements[2].setText(Converter.roundingDouble3(value5, Locale.ENGLISH));
        }
        this.userMeasurements[3].setText(Converter.roundingDouble3(value50, Locale.ENGLISH));
        this.userMeasurements[4].setText(Converter.roundingDouble3(value50, Locale.ENGLISH));
        this.userMeasurements[5].setText(Converter.roundingDouble3(value95, Locale.ENGLISH));
        this.userMeasurements[6].setText(Converter.roundingDouble3(value95, Locale.ENGLISH));
        this.userMeasurements[7].setText(Converter.roundingDouble3(value100, Locale.ENGLISH));
    }

    @Override
    public void setReactions() {

    }

    @Override
    public void build() {
        this.add(this.columnsHeader[0], new Cell(0,0));
        this.add(this.columnsHeader[1], new Cell(1,0));
        this.add(this.columnsHeader[2], new Cell(2,0));
        this.add(this.columnsHeader[3], new Cell(3,0));

        this.add(this.labelPercent[0], new Cell(0,1));
        this.add(this.labelPercent[1], new Cell(0,2,2));
        this.add(this.labelPercent[2], new Cell(0,4,2));
        this.add(this.labelPercent[3], new Cell(0,6,2));
        this.add(this.labelPercent[4], new Cell(0,8));

        this.add(this.labelValue[0], new Cell(1,1));
        this.add(this.labelValue[1], new Cell(1,2,2));
        this.add(this.labelValue[2], new Cell(1,4,2));
        this.add(this.labelValue[3], new Cell(1,6,2));
        this.add(this.labelValue[4], new Cell(1,8));

        this.add(new ButtonCell(false, "-"), new Cell(2,1));
        this.add(this.motions[0], new Cell(2,2));
        this.add(this.motions[1], new Cell(2,3));
        this.add(this.motions[2], new Cell(2,4));
        this.add(this.motions[3], new Cell(2,5));
        this.add(this.motions[4], new Cell(2,6));
        this.add(this.motions[5], new Cell(2,7));
        this.add(new ButtonCell(false, "-"), new Cell(2,8));

        this.add(this.userMeasurements[0], new Cell(3,1));
        this.add(this.userMeasurements[1], new Cell(3,2));
        this.add(this.userMeasurements[2], new Cell(3,3));
        this.add(this.userMeasurements[3], new Cell(3,4));
        this.add(this.userMeasurements[4], new Cell(3,5));
        this.add(this.userMeasurements[5], new Cell(3,6));
        this.add(this.userMeasurements[6], new Cell(3,7));
        this.add(this.userMeasurements[7], new Cell(3,8));
    }

    @Override
    public double[] getValues() {
        double[]val = new double[8];
        for (int x=0;x<val.length;x++){
            val[x] = Double.parseDouble(this.userMeasurements[x].getText());
        }
        return val;
    }

    @Override
    public void setValues(double[]values) {
        for (int x=0;x<this.userMeasurements.length;x++){
            this.userMeasurements[x].setText(String.valueOf(values[x]));
        }
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
                double value0 = channel.getRangeMin();
                double value5 = ((channel.getRange() / 100) * 5) + channel.getRangeMin();
                double value50 = ((channel.getRange() / 100) * 50) + channel.getRangeMin();
                double value95 = ((channel.getRange() / 100) * 95) + channel.getRangeMin();
                double value100 = channel.getRangeMax();
                for (int x=0;x<userMeasurements.length;x++){
                    if (cell.equals(userMeasurements[x])){
                        switch (x){
                            case 0:
                                cell.setText(String.valueOf(value0));
                                break;
                            case 1:
                            case 2:
                                cell.setText(String.valueOf(value5));
                                break;
                            case 3:
                            case 4:
                                cell.setText(String.valueOf(value50));
                                break;
                            case 5:
                            case 6:
                                cell.setText(String.valueOf(value95));
                                break;
                            case 7:
                                cell.setText(String.valueOf(value100));
                                break;
                        }
                    }
                }
            }else{
                String check = Converter.doubleString(cell.getText());
                double d = Double.parseDouble(check);
                cell.setText(Converter.roundingDouble3(d, Locale.ENGLISH));
            }
        }
    };

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
