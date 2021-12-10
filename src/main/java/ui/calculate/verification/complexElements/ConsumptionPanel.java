package ui.calculate.verification.complexElements;

import constants.MeasurementConstants;
import constants.Strings;
import constants.Value;
import converters.VariableConverter;
import measurements.calculation.Calculation;
import model.Calibrator;
import model.Channel;
import support.Values;
import ui.ButtonCell;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ConsumptionPanel extends JPanel implements UI_Container {
    private final Channel channel;
    private final Values values;
    private final Calculation calculation;

    private ButtonCell channelNameLabel;
    private ButtonCell protocolNumberLabel;
    private ButtonCell dateLabel;
    private ButtonCell pathLabel;
    private ButtonCell technologyNumberLabel;
    private ButtonCell codeLabel;
    private ButtonCell rangeChannelLabel;
    private ButtonCell allowableErrorChannelLabel;
    private ButtonCell sensorLabel;
    private ButtonCell allowableErrorSensorLabel;
    private ButtonCell rangeSensorLabel;
    private ButtonCell controlConditionsLabel;
    private ButtonCell externalTemperatureLabel;
    private ButtonCell humidityLabel;
    private ButtonCell atmospherePressureLabel;
    private ButtonCell resultsTableLabel;
    private ButtonCell calibratorNameLabel;
    private ButtonCell calibratorNumberLabel;
    private ButtonCell calibratorCertificateLabel;
    private ButtonCell allowableErrorCalibratorLabel;
    private ButtonCell metrologyTableLabel;

    private ButtonCell channelName;
    private ButtonCell number;
    private ButtonCell date;
    private ButtonCell path;
    private ButtonCell technologyNumber;
    private ButtonCell code;
    private ButtonCell rangeChannel;
    private ButtonCell allowableErrorChannel;
    private ButtonCell sensor;
    private ButtonCell allowableErrorSensor;
    private ButtonCell rangeSensor;
    private ButtonCell externalTemperature;
    private ButtonCell humidity;
    private ButtonCell atmospherePressure;
    private ButtonCell calibratorName;
    private ButtonCell calibratorNumber;
    private ButtonCell calibratorCertificate;
    private ButtonCell allowableErrorCalibrator;

    private TableProtocol resultsTable;
    private TableCertificate metrologyTable;

    private ButtonCell resultOfCheck;

    private boolean withAlarm;
    private ButtonCell alarmLabel;
    private ButtonCell alarm;

    private JComboBox<String> advice;

    public ConsumptionPanel(Channel channel, Values values, Calculation calculation){
        super(new GridBagLayout());
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setValues();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.channelNameLabel = new ButtonCell(true, Strings._NAME);
        this.protocolNumberLabel = new ButtonCell(true, Strings.PROTOCOL_NUMBER);
        this.dateLabel = new ButtonCell(true, " від ");
        this.pathLabel = new ButtonCell(true, Strings.PATH_OF_CHANNEL);
        this.technologyNumberLabel = new ButtonCell(true, Strings.TECHNOLOGY_NUMBER);
        this.codeLabel = new ButtonCell(true, Strings.CODE);
        this.rangeChannelLabel = new ButtonCell(true, Strings.RANGE_OF_CHANNEL);
        this.allowableErrorChannelLabel = new ButtonCell(true, Strings.ALLOWABLE_ERROR_OF_CHANNEL);
        this.sensorLabel = new ButtonCell(true, Strings.SENSOR);
        this.allowableErrorSensorLabel = new ButtonCell(true, Strings.ALLOWABLE_ERROR_OF_SENSOR);
        this.rangeSensorLabel = new ButtonCell(true, Strings.RANGE_OF_SENSOR);
        this.controlConditionsLabel = new ButtonCell(true, Strings.CONDITIONS_FOR_CONTROL);
        this.externalTemperatureLabel = new ButtonCell(true, Strings.TEMPERATURE_EXTERNAL_ENVIRONMENT);
        this.humidityLabel = new ButtonCell(true, Strings.RELATIVE_HUMIDITY);
        this.atmospherePressureLabel = new ButtonCell(true, Strings.ATMOSPHERE_PRESSURE);
        this.resultsTableLabel = new ButtonCell(true, Strings.TABLE_RESULT_PROTOCOL);
        this.calibratorNameLabel = new ButtonCell(true, Strings.CALIBRATOR);
        this.calibratorNumberLabel = new ButtonCell(true, Strings.PARENT_NUMBER);
        this.calibratorCertificateLabel = new ButtonCell(true, Strings.CERTIFICATE_OF_CALIBRATION);
        this.allowableErrorCalibratorLabel = new ButtonCell(true, Strings.ALLOWABLE_ERROR_OF_CALIBRATOR);
        this.metrologyTableLabel = new ButtonCell(true, Strings.TABLE_RESULT_CERTIFICATE);

        this.channelName = new ButtonCell(false);
        this.number = new ButtonCell(false);
        this.date = new ButtonCell(false);
        this.path = new ButtonCell(false);
        this.technologyNumber = new ButtonCell(false);
        this.code = new ButtonCell(false);
        this.rangeChannel = new ButtonCell(false);
        this.allowableErrorChannel = new ButtonCell(false);
        this.sensor = new ButtonCell(false);
        this.allowableErrorSensor = new ButtonCell(false);
        this.rangeSensor = new ButtonCell(false);
        this.externalTemperature = new ButtonCell(false);
        this.humidity = new ButtonCell(false);
        this.atmospherePressure = new ButtonCell(false);
        this.calibratorName = new ButtonCell(false);
        this.calibratorNumber = new ButtonCell(false);
        this.calibratorCertificate = new ButtonCell(false);
        this.allowableErrorCalibrator = new ButtonCell(false);
    }

    private void setValues(){
        this.resultsTable = new TableProtocol();
        this.metrologyTable = new TableCertificate();

        this.channelName.setText(this.channel.getName());
        this.number.setText(this.values.getStringValue(Value.CHANNEL_PROTOCOL_NUMBER));
        this.date.setText(VariableConverter.dateToString((Calendar) values.getValue(Value.CHANNEL_DATE)));

        String path = this.channel.getArea()
                + " "
                + this.channel.getProcess();
        this.path.setText(path);

        this.technologyNumber.setText(this.channel.getTechnologyNumber());
        this.code.setText(this.channel.getCode());

        String rangeChannel = "Від "
                + VariableConverter.roundingDouble(this.channel.getRangeMin(), Locale.GERMAN)
                + " до "
                + VariableConverter.roundingDouble(this.channel.getRangeMax(), Locale.GERMAN)
                + " "
                + this.channel.getMeasurement().getValue();
        this.rangeChannel.setText(rangeChannel);

        String allowableErrorChannel = Strings.PLUS_MINUS
                + VariableConverter.roundingDouble(this.channel.getAllowableErrorPercent(), Locale.GERMAN)
                + "% або "
                + Strings.PLUS_MINUS
                + VariableConverter.roundingDouble3(this.channel.getAllowableError(), Locale.GERMAN)
                + this.channel.getMeasurement().getValue();
        this.allowableErrorChannel.setText(allowableErrorChannel);

        this.sensor.setText(this.channel.getSensor().getType());

        double errorSensor = this.channel.getSensor().getError(this.channel);
        double ePS = errorSensor / (this.channel.getRange() / 100);
        String errorSensorPercent;
        if (ePS < 0.01){
            errorSensorPercent = VariableConverter.roundingDouble3(ePS, Locale.GERMAN);
        }else {
            errorSensorPercent = VariableConverter.roundingDouble2(ePS, Locale.GERMAN);
        }

        String errorSensorValue;
        if (errorSensor < 0.01){
            errorSensorValue = VariableConverter.roundingDouble3(errorSensor, Locale.GERMAN);
        }else {
            errorSensorValue = VariableConverter.roundingDouble2(errorSensor, Locale.GERMAN);
        }
        String allowableErrorSensor = Strings.PLUS_MINUS
                + errorSensorPercent
                + "% або "
                + Strings.PLUS_MINUS
                + errorSensorValue
                + this.channel.getMeasurement().getValue();
        this.allowableErrorSensor.setText(allowableErrorSensor);

        String rangeSensor = "Від "
                + VariableConverter.roundingDouble(this.channel.getSensor().getRangeMin(), Locale.GERMAN)
                + " до "
                + VariableConverter.roundingDouble(this.channel.getSensor().getRangeMax(), Locale.GERMAN)
                + " "
                + this.channel.getSensor().getValue();
        this.rangeSensor.setText(rangeSensor);

        this.externalTemperature.setText(this.values.getStringValue(Value.CALCULATION_EXTERNAL_TEMPERATURE)
                + MeasurementConstants.DEGREE_CELSIUS.getValue());
        this.humidity.setText(this.values.getStringValue(Value.CALCULATION_EXTERNAL_HUMIDITY)
                + "%");
        this.atmospherePressure.setText(this.values.getStringValue(Value.CALCULATION_EXTERNAL_PRESSURE)
                + "мм рт ст");

        Calibrator calibrator = (Calibrator) this.values.getValue(Value.CALIBRATOR);
        this.calibratorName.setText(calibrator.getType());
        this.calibratorNumber.setText(calibrator.getNumber());

        String certificateCalibrator = calibrator.getCertificateName()
                + " від "
                + VariableConverter.dateToString(calibrator.getCertificateDate())
                + "р. "
                + calibrator.getCertificateCompany();
        this.calibratorCertificate.setText(certificateCalibrator);

        double errorCalibrator = calibrator.getError(this.channel);
        double ePC = errorCalibrator / (this.channel.getRange() / 100);
        String error;
        if (errorCalibrator < 0.001){
            error = VariableConverter.roundingDouble4(errorCalibrator, Locale.GERMAN);
        }else if (errorCalibrator < 0.01){
            error = VariableConverter.roundingDouble3(errorCalibrator, Locale.GERMAN);
        }else {
            error = VariableConverter.roundingDouble2(errorCalibrator, Locale.GERMAN);
        }
        String allowableErrorCalibrator = Strings.PLUS_MINUS
                + VariableConverter.roundingDouble2(ePC, Locale.GERMAN)
                + "% або "
                + Strings.PLUS_MINUS
                + error
                + this.channel.getMeasurement().getValue();
        this.allowableErrorCalibrator.setText(allowableErrorCalibrator);

        this.resultOfCheck = new ButtonCell(true);

        if (this.calculation.goodChannel()){
            this.resultOfCheck.setBackground(Color.GREEN);
            this.resultOfCheck.setText(Strings.CHANNEL_IS_GOOD);
        }else{
            this.resultOfCheck.setBackground(Color.RED);
            this.resultOfCheck.setText(Strings.CHANNEL_IS_BAD);
        }

        this.withAlarm = this.values.getBooleanValue(Value.CALCULATION_ALARM_PANEL);
        if (this.calculation.closeToFalse() && this.calculation.goodChannel()){
            ArrayList<String> toComboBox = new ArrayList<>();
            toComboBox.add("");
            if (withAlarm){
                toComboBox.add(Strings.ALARM_MESSAGE + this.values.getStringValue(Value.CALCULATION_ALARM_VALUE));
            }
            toComboBox.add(Strings.ADVICE_FIX);
            toComboBox.add(Strings.ADVICE_FIX_PRESSURE);
            toComboBox.add(Strings.ADVICE_RANGE);

            this.advice = new JComboBox<>(toComboBox.toArray(new String[0]));
            this.advice.setSelectedIndex(1);
        }else {
            if (withAlarm) {
                this.alarmLabel = new ButtonCell(true, Strings.ALARM_MESSAGE);
                this.alarm = new ButtonCell(false, this.values.getStringValue(Value.CALCULATION_ALARM_VALUE));
            }
        }
    }

    @Override
    public void setReactions() {}

    @Override
    public String getName() {
        return Objects.requireNonNull(this.advice.getSelectedItem()).toString();
    }

    @Override
    public void build() {
        this.add(this.channelNameLabel, new Cell(0, 0, 4));
        this.add(this.channelName, new Cell(0, 1, 4));

        this.add(this.protocolNumberLabel, new Cell(0, 2));
        this.add(this.number, new Cell(1, 2));
        this.add(this.dateLabel, new Cell(2, 2));
        this.add(this.date, new Cell(3, 2));

        this.add(this.pathLabel, new Cell(0, 3, 2));
        this.add(this.path, new Cell(2, 3, 2));

        this.add(this.technologyNumberLabel, new Cell(0, 4, 2));
        this.add(this.technologyNumber, new Cell(2, 4, 2));

        this.add(this.codeLabel, new Cell(0, 5, 2));
        this.add(this.code, new Cell(2, 5, 2));

        this.add(this.rangeChannelLabel, new Cell(0, 6, 2));
        this.add(this.rangeChannel, new Cell(2, 6, 2));

        this.add(this.allowableErrorChannelLabel, new Cell(0, 7, 2));
        this.add(this.allowableErrorChannel, new Cell(2, 7, 2));

        this.add(this.sensorLabel, new Cell(0, 8, 2));
        this.add(this.sensor, new Cell(2, 8, 2));

        this.add(this.allowableErrorSensorLabel, new Cell(0, 9, 2));
        this.add(this.allowableErrorSensor, new Cell(2, 9, 2));

        this.add(this.rangeSensorLabel, new Cell(0, 10, 2));
        this.add(this.rangeSensor, new Cell(2, 10, 2));

        this.add(this.controlConditionsLabel, new Cell(0, 11, 4));

        this.add(this.externalTemperatureLabel, new Cell(0, 12, 2));
        this.add(this.externalTemperature, new Cell(2, 12, 2));

        this.add(this.humidityLabel, new Cell(0, 13, 2));
        this.add(this.humidity, new Cell(2, 13, 2));

        this.add(this.atmospherePressureLabel, new Cell(0, 14, 2));
        this.add(this.atmospherePressure, new Cell(2, 14, 2));

        this.add(this.resultsTableLabel, new Cell(0, 15, 4));
        this.add(this.resultsTable, new Cell(0, 16, 4, 8));

        this.add(this.calibratorNameLabel, new Cell(0, 24, 2));
        this.add(this.calibratorName, new Cell(2, 24, 2));

        this.add(this.calibratorNumberLabel, new Cell(0, 25, 2));
        this.add(this.calibratorNumber, new Cell(2, 25, 2));

        this.add(this.calibratorCertificateLabel, new Cell(0, 26, 2));
        this.add(this.calibratorCertificate, new Cell(2, 26, 2));

        this.add(this.allowableErrorCalibratorLabel, new Cell(0, 27, 2));
        this.add(this.allowableErrorCalibrator, new Cell(2, 27, 2));

        this.add(this.metrologyTableLabel, new Cell(0, 28, 4));
        this.add(this.metrologyTable, new Cell(0, 29, 4, 7));

        this.add(this.resultOfCheck, new Cell(0, 36, 4));

        if (this.calculation.closeToFalse() && this.calculation.goodChannel()){
            this.add(this.advice, new Cell(0,37,4));
        }else {
            if (this.withAlarm) {
                this.add(this.alarmLabel, new Cell(0, 37, 2));
                this.add(this.alarm, new Cell(2, 37, 2));
            }
        }
    }

    private class TableProtocol extends JPanel {

        protected TableProtocol(){
            super(new GridBagLayout());

            if (channel.getSensor().getType().contains(Strings.SENSOR_YOKOGAWA)){
                createYokogawaForm();
            }
        }

        private void createYokogawaForm(){
            //create cells
            ButtonCell headerNum = new ButtonCell(false, "№");
            ButtonCell headerPercent = new ButtonCell(false, "%");
            ButtonCell headerXet = new ButtonCell(false, "Xет," + channel.getMeasurement().getValue());
            ButtonCell headerXi = new ButtonCell(false, "Отримані значення, Хі");

            ButtonCell[] valuesNum1 = new ButtonCell[]{
                    new ButtonCell(false, "1"),
                    new ButtonCell(false, "2"),
                    new ButtonCell(false, "3"),
                    new ButtonCell(false, "4"),
                    new ButtonCell(false, "5")
            };
            ButtonCell[] valuesNum2 = new ButtonCell[5];
            for (int x=1;x < 6;x++){
                valuesNum2[x-1] = new ButtonCell(false, String.valueOf(x));
            }
            ButtonCell[] valuesPercent = new ButtonCell[]{
                    new ButtonCell(false, "0"),
                    new ButtonCell(false, "25"),
                    new ButtonCell(false, "50"),
                    new ButtonCell(false, "75"),
                    new ButtonCell(false, "100")
            };

            double value0 = ((channel.getRangeMin()));
            double value25 = ((channel.getRange() / 100) * 25) + channel.getRangeMin();
            double value50 = ((channel.getRange() / 100) * 50) + channel.getRangeMin();
            double value75 = ((channel.getRange() / 100) * 75) + channel.getRangeMin();
            double value100 = channel.getRangeMax();
            ButtonCell[] valuesXet = new ButtonCell[]{
                    new ButtonCell(false, VariableConverter.roundingDouble2(value0, Locale.GERMAN)),
                    new ButtonCell(false, VariableConverter.roundingDouble2(value25, Locale.GERMAN)),
                    new ButtonCell(false, VariableConverter.roundingDouble2(value50, Locale.GERMAN)),
                    new ButtonCell(false, VariableConverter.roundingDouble2(value75, Locale.GERMAN)),
                    new ButtonCell(false, VariableConverter.roundingDouble2(value100, Locale.GERMAN))
            };

            double[][]in = calculation.getIn();
            ButtonCell[][] valuesXi = new ButtonCell[in.length][in[0].length];
            for (int x=0;x<in.length;x++){
                double[]values = in[x];
                valuesXi[x] = new ButtonCell[values.length];
                for (int y=0;y<values.length;y++){
                    valuesXi[x][y] = new ButtonCell(false, VariableConverter.roundingDouble3(values[y], Locale.GERMAN));
                }
            }

            //build table
            this.removeAll();
            this.add(headerNum, new Cell(0,0,1,2));
            this.add(headerPercent, new Cell(1,0,1,2));
            this.add(headerXet, new Cell(2,0,1,2));
            this.add(headerXi, new Cell(3,0,5,1));

            int x = 3;
            int y = 0;
            for (int index=0;index<valuesNum1.length;index++){
                y = y + 2;
                this.add(valuesNum1[index], new Cell(0, y, 1,2));
                this.add(valuesNum2[index], new Cell(x++, 1,1,1));
                this.add(valuesPercent[index], new Cell(1, y,1,2));
                this.add(valuesXet[index], new Cell(2, y,1,2));
            }

            for (int index1=0;index1<valuesXi.length;index1++){
                x = 3 + index1;
                for (int index2=0;index2<valuesXi[index1].length;index2++){
                    y = 2 + index2;
                    this.add(valuesXi[index1][index2], new Cell(x,y,1,1));
                }
            }
        }
    }

    private class TableCertificate extends JPanel {

        protected TableCertificate(){
            super(new GridBagLayout());

            JButton[] cells = new JButton[16];
            for (int x = 0; x< cells.length; x++){
                cells[x] = new ButtonCell(false);
            }

            cells[0].setText("Назва МХ");
            cells[1].setText("Росширенна невизначенність");
            cells[2].setText("Приведенна похибка");
            cells[3].setText("Абсолютна похибка");
            cells[4].setText("Не усуненна систематична похибка");
            cells[5].setText("Отримані значення МХ");

            String value = channel.getMeasurement().getValue();

            String u;
            double extendedIndeterminacy = calculation.getExtendedIndeterminacy();
            if (extendedIndeterminacy < 0.01 && extendedIndeterminacy > -0.01){
                u  = VariableConverter.roundingDouble3(extendedIndeterminacy, Locale.GERMAN);
            }else {
                u  = VariableConverter.roundingDouble2(calculation.getExtendedIndeterminacy(), Locale.GERMAN);
            }
            cells[6].setText("U = "
                    + Strings.PLUS_MINUS
                    + u
                    + value);

            String gamma;
            double errorInRange = calculation.getErrorInRange();
            if (errorInRange < 0.01 && errorInRange > -0.01){
                gamma  = VariableConverter.roundingDouble3(errorInRange, Locale.GERMAN);
            }else {
                gamma  = VariableConverter.roundingDouble2(errorInRange, Locale.GERMAN);
            }
            cells[7].setText(Strings.GAMMA
                    + " вк = "
                    + Strings.PLUS_MINUS
                    + gamma
                    + "%");

            String delta;
            double absoluteErrorWithSensorError = calculation.getAbsoluteErrorWithSensorError();
            if (absoluteErrorWithSensorError < 0.01 && absoluteErrorWithSensorError > -0.01){
                delta  = VariableConverter.roundingDouble3(absoluteErrorWithSensorError, Locale.GERMAN);
            }else {
                delta  = VariableConverter.roundingDouble2(absoluteErrorWithSensorError, Locale.GERMAN);
            }
            cells[8].setText(Strings.DELTA
                    + " вк = "
                    + Strings.PLUS_MINUS
                    + delta
                    + value);

            String s0;
            String s25;
            String s50;
            String s75;
            String s100;

            double[] systematicErrors = calculation.getSystematicErrors();

            if (systematicErrors[0] < 0.01 && systematicErrors[0] > -0.01){
                s0 = "0% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(systematicErrors[0], Locale.GERMAN)
                        + value;
            }else {
                s0 = "0% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(systematicErrors[0], Locale.GERMAN)
                        + value;
            }

            if (systematicErrors[1] < 0.01 && systematicErrors[1] > -0.01){
                s25 = "25% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(systematicErrors[1], Locale.GERMAN)
                        + value;
            }else {
                s25 = "25% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(systematicErrors[1], Locale.GERMAN)
                        + value;
            }

            if (systematicErrors[2] < 0.01 && systematicErrors[2] > -0.01){
                s50 = "50% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(systematicErrors[2], Locale.GERMAN)
                        + value;
            }else {
                s50 = "50% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(systematicErrors[2], Locale.GERMAN)
                        + value;
            }

            if (systematicErrors[3] < 0.01 && systematicErrors[3] > -0.01){
                s75 = "75% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(systematicErrors[3], Locale.GERMAN)
                        + value;
            }else {
                s75 = "75% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(systematicErrors[3], Locale.GERMAN)
                        + value;
            }

            if (systematicErrors[4] < 0.01 && systematicErrors[4] > -0.01){
                s100 = "100% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(systematicErrors[4], Locale.GERMAN)
                        + value;
            }else {
                s100 = "100% "
                        + Strings.DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(systematicErrors[4], Locale.GERMAN)
                        + value;
            }

            cells[9].setText(s0);
            cells[10].setText(s25);
            cells[11].setText(s50);
            cells[12].setText(s75);
            cells[13].setText(s100);
            cells[14].setText("Міжконтрольний інтервал");
            double frequency = channel.getFrequency();
            cells[15].setText(VariableConverter.roundingDouble(frequency, Locale.GERMAN) + Strings.YEAR_WORD(frequency));

            this.add(cells[0], new Cell( 0,0,1,1));
            this.add(cells[1], new Cell( 0,1,1,1));
            this.add(cells[2], new Cell( 0,2,1,1));
            this.add(cells[3], new Cell( 0,3,1,1));
            this.add(cells[4], new Cell( 0,4,1,5));
            this.add(cells[5], new Cell( 1,0,1,1));
            this.add(cells[6], new Cell( 1,1,1,1));
            this.add(cells[7], new Cell( 1,2,1,1));
            this.add(cells[8], new Cell( 1,3,1,1));
            this.add(cells[9], new Cell( 1,4,1,1));
            this.add(cells[10], new Cell(1,5,1,1));
            this.add(cells[11], new Cell(1,6,1,1));
            this.add(cells[12], new Cell(1,7,1,1));
            this.add(cells[13], new Cell(1,8,1,1));
            this.add(cells[14], new Cell(2,0,1,1));
            this.add(cells[15], new Cell(2,1,1,8));
        }
    }

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

        protected Cell(int x, int y, int width, int height){
            super();

            this.fill = BOTH;

            this.gridwidth = width;
            this.gridheight = height;
            this.gridx = x;
            this.gridy = y;
        }
    }
}
