package ui.calculate.verification.complexElements;

import calculation.Calculation;
import constants.Key;
import converters.ValueConverter;
import converters.VariableConverter;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class PressurePanel extends JPanel {
    private static final String NAME = "Назва";
    private static final String PROTOCOL_NUMBER = "Номер протоколу";
    private static final String PATH_OF_CHANNEL = "Розташування вимірювання каналу";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер";
    private static final String CODE = "Код";
    private static final String RANGE_OF_CHANNEL = "Діапазон вимірювального каналу";
    private static final String ALLOWABLE_ERROR_OF_CHANNEL = "Допустима похибка вимірювального каналу";
    private static final String SENSOR = "Первинний вимірювальний пристрій";
    private static final String ALLOWABLE_ERROR_OF_SENSOR = "Допустима похибка перетворювача";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";
    private static final String CONDITIONS_FOR_CONTROL = "Умови проведення контролю";
    private static final String TEMPERATURE_EXTERNAL_ENVIRONMENT = "Температура навколишнього середовища";
    private static final String RELATIVE_HUMIDITY = "Відносна вологість повітря";
    private static final String ATMOSPHERE_PRESSURE = "Атмосферний тиск";
    private static final String TABLE_RESULT_PROTOCOL = "Таблиця отриманих значеннь вимірювального каналу";
    private static final String CALIBRATOR = "Калібратор";
    private static final String PARENT_NUMBER = "Заводський № ";
    private static final String CERTIFICATE_OF_CALIBRATION = "Сертифікат калібрування";
    private static final String ALLOWABLE_ERROR_OF_CALIBRATOR = "Допустима похибка ЗВТ";
    private static final String TABLE_RESULT_CERTIFICATE = "Таблиця отриманих метрологічних хар-к";
    private static final String PLUS_MINUS = "\u00B1";
    private static final String CHANNEL_IS_GOOD = "Канал придатний";
    private static final String CHANNEL_IS_BAD = "Канал не придатний";
    private static final String ALARM_MESSAGE = "Сигналізація спрацювала при t = ";
    private static final String ADVICE_FIX = "Порада: налаштувати вимірювальний канал";
    private static final String ADVICE_FIX_PRESSURE = "Порада: ущільнити датчик, не допускаючи стравлювання";
    private static final String ADVICE_RANGE = "Порада: для кращих показів налаштуйте вимірювальний канал на вказаний діапазон вимірювання";
    private static final String GAMMA = "\u03B3";
    private static final String DELTA = "\u0394";

    private static String YEAR_WORD(double d){
        if (d == 0D){
            return "років";
        }else if (d > 0 && d < 1){
            return "року";
        }else if (d == 1D){
            return "рік";
        }else if (d > 1 && d < 5){
            return "роки";
        }else {
            return "років";
        }
    }

    private final Channel channel;
    private final HashMap<Integer, Object> values;
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

    public PressurePanel(Channel channel, HashMap<Integer, Object> values, Calculation calculation){
        super(new GridBagLayout());
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setValues();
        this.build();
    }

    private void createElements() {
        this.channelNameLabel = new ButtonCell(true, NAME);
        this.protocolNumberLabel = new ButtonCell(true, PROTOCOL_NUMBER);
        this.dateLabel = new ButtonCell(true, " від ");
        this.pathLabel = new ButtonCell(true, PATH_OF_CHANNEL);
        this.technologyNumberLabel = new ButtonCell(true, TECHNOLOGY_NUMBER);
        this.codeLabel = new ButtonCell(true, CODE);
        this.rangeChannelLabel = new ButtonCell(true, RANGE_OF_CHANNEL);
        this.allowableErrorChannelLabel = new ButtonCell(true, ALLOWABLE_ERROR_OF_CHANNEL);
        this.sensorLabel = new ButtonCell(true, SENSOR);
        this.allowableErrorSensorLabel = new ButtonCell(true, ALLOWABLE_ERROR_OF_SENSOR);
        this.rangeSensorLabel = new ButtonCell(true, RANGE_OF_SENSOR);
        this.controlConditionsLabel = new ButtonCell(true, CONDITIONS_FOR_CONTROL);
        this.externalTemperatureLabel = new ButtonCell(true, TEMPERATURE_EXTERNAL_ENVIRONMENT);
        this.humidityLabel = new ButtonCell(true, RELATIVE_HUMIDITY);
        this.atmospherePressureLabel = new ButtonCell(true, ATMOSPHERE_PRESSURE);
        this.resultsTableLabel = new ButtonCell(true, TABLE_RESULT_PROTOCOL);
        this.calibratorNameLabel = new ButtonCell(true, CALIBRATOR);
        this.calibratorNumberLabel = new ButtonCell(true, PARENT_NUMBER);
        this.calibratorCertificateLabel = new ButtonCell(true, CERTIFICATE_OF_CALIBRATION);
        this.allowableErrorCalibratorLabel = new ButtonCell(true, ALLOWABLE_ERROR_OF_CALIBRATOR);
        this.metrologyTableLabel = new ButtonCell(true, TABLE_RESULT_CERTIFICATE);

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
        this.number.setText((String) this.values.get(Key.CHANNEL_PROTOCOL_NUMBER));
        this.date.setText((String) values.get(Key.CHANNEL_DATE));

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

        String allowableErrorChannel = PLUS_MINUS
                + VariableConverter.roundingDouble(this.channel.getAllowableErrorPercent(), Locale.GERMAN)
                + "% або "
                + PLUS_MINUS
                + VariableConverter.roundingDouble3(this.channel.getAllowableError(), Locale.GERMAN)
                + this.channel.getMeasurement().getValue();
        this.allowableErrorChannel.setText(allowableErrorChannel);

        this.sensor.setText(this.channel.getSensor().getType());

        double errorSensor = this.channel.getSensor().getError(this.channel);
        double ePS = errorSensor / (this.channel._getRange() / 100);
        String errorSensorPercent;
        errorSensorPercent = VariableConverter.roundingDouble3(ePS, Locale.GERMAN);

        String errorSensorValue;
        if (errorSensor < 0.01){
            errorSensorValue = VariableConverter.roundingDouble3(errorSensor, Locale.GERMAN);
        }else {
            errorSensorValue = VariableConverter.roundingDouble2(errorSensor, Locale.GERMAN);
        }
        String allowableErrorSensor = PLUS_MINUS
                + errorSensorPercent
                + "% або "
                + PLUS_MINUS
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

        this.externalTemperature.setText(this.values.get(Key.CALCULATION_EXTERNAL_TEMPERATURE)
                + Measurement.DEGREE_CELSIUS);
        this.humidity.setText(this.values.get(Key.CALCULATION_EXTERNAL_HUMIDITY)
                + "%");
        this.atmospherePressure.setText(this.values.get(Key.CALCULATION_EXTERNAL_PRESSURE)
                + "мм рт ст");

        Calibrator calibrator = (Calibrator) this.values.get(Key.CALIBRATOR);
        this.calibratorName.setText(calibrator.getType());
        this.calibratorNumber.setText(calibrator.getNumber());

        String certificateCalibrator = calibrator.getCertificateName()
                + " від "
                + calibrator.getCertificateDate()
                + "р. "
                + calibrator.getCertificateCompany();
        this.calibratorCertificate.setText(certificateCalibrator);

        double errorCalibrator = calibrator.getError(this.channel);
        double ePC;
        if (calibrator.getValue().equals(this.channel.getMeasurement().getValue())) {
            ePC = errorCalibrator / (this.channel._getRange() / 100);
        }else {
            double calibratorRange = calibrator._getRange();
            double convertedCalibratorRange = new ValueConverter(calibrator.getValue(), this.channel.getMeasurement().getValue()).get(calibratorRange);
            ePC = errorCalibrator / (convertedCalibratorRange/100);
        }
        String error;
        if (errorCalibrator < 0.01){
            error = VariableConverter.roundingDouble3(errorCalibrator, Locale.GERMAN);
        }else {
            error = VariableConverter.roundingDouble2(errorCalibrator, Locale.GERMAN);
        }
        String errorP = VariableConverter.roundingDouble2(ePC, Locale.GERMAN);
        String allowableErrorCalibrator = PLUS_MINUS
                + errorP
                + "% або "
                + PLUS_MINUS
                + error
                + this.channel.getMeasurement().getValue();
        this.allowableErrorCalibrator.setText(allowableErrorCalibrator);

        this.resultOfCheck = new ButtonCell(true);

        if (this.calculation.goodChannel()){
            this.resultOfCheck.setBackground(Color.GREEN);
            this.resultOfCheck.setText(CHANNEL_IS_GOOD);
        }else{
            this.resultOfCheck.setBackground(Color.RED);
            this.resultOfCheck.setText(CHANNEL_IS_BAD);
        }

        this.withAlarm = (boolean) this.values.get(Key.CALCULATION_ALARM_PANEL);
        if (this.calculation.closeToFalse() && this.calculation.goodChannel()){
            ArrayList<String> toComboBox = new ArrayList<>();
            toComboBox.add("");
            if (withAlarm){
                toComboBox.add(ALARM_MESSAGE + this.values.get(Key.CALCULATION_ALARM_VALUE));
            }
            toComboBox.add(ADVICE_FIX);
            toComboBox.add(ADVICE_FIX_PRESSURE);
            toComboBox.add(ADVICE_RANGE);

            this.advice = new JComboBox<>(toComboBox.toArray(new String[0]));
            this.advice.setSelectedIndex(1);
        }else {
            if (withAlarm) {
                this.alarmLabel = new ButtonCell(true, ALARM_MESSAGE);
                this.alarm = new ButtonCell(false, (String) this.values.get(Key.CALCULATION_ALARM_VALUE));
            }
        }
    }

    @Override
    public String getName() {
        return Objects.requireNonNull(this.advice.getSelectedItem()).toString();
    }

    private void build() {
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
            int index;
            JButton[] cells = new JButton[48];
            for (int x = 0; x < cells.length; x++){
                if (x==0){
                    cells[x] = new ButtonCell(false, "№");
                    this.add(cells[x], new Cell(0, 0, 1, 2));
                }else if (x < 4){
                    index = x + x;
                    cells[x] = new ButtonCell(false, String.valueOf(x));
                    this.add(cells[x], new Cell(0, index, 1, 2));
                }else if (x ==4){
                    cells[x] = new ButtonCell(false, "%");
                    this.add(cells[x], new Cell(1, 0, 1, 2));
                }else if (x == 5){
                    cells[x] = new ButtonCell(false, "5");
                    this.add(cells[x], new Cell(1, 2, 1, 2));
                }else if (x == 6){
                    cells[x] = new ButtonCell(false, "50");
                    this.add(cells[x], new Cell(1, 4, 1, 2));
                }else if (x == 7){
                    cells[x] = new ButtonCell(false, "95");
                    this.add(cells[x], new Cell(1, 6, 1, 2));
                }else if (x == 8) {
                    cells[x] = new ButtonCell(false, "Xет,".concat(channel.getMeasurement().getValue()));
                    this.add(cells[x], new Cell(3, 0, 1, 2));
                }else if (x == 9) {
                    Calibrator calibrator = (Calibrator) values.get(Key.CALIBRATOR);
                    double value5 = calculation.getControlPointsValues()[1];
                    if (calibrator.getType().equals(Calibrator.FLUKE718_30G)) {
                        double maxCalibratorPower = new ValueConverter(Measurement.KGS_SM2, channel.getMeasurement().getValue()).get(-0.8);
                        if (value5 < maxCalibratorPower){
                            cells[x] = new ButtonCell(false, VariableConverter.roundingDouble2(maxCalibratorPower, Locale.GERMAN));
                        }else {
                            cells[x] = new ButtonCell(false, VariableConverter.roundingDouble2(value5, Locale.GERMAN));
                        }
                    }else {
                        cells[x] = new ButtonCell(false, VariableConverter.roundingDouble2(value5, Locale.GERMAN));
                    }
                    this.add(cells[x], new Cell(3,2,1,2));
                }else if (x == 10) {
                    double value50 = calculation.getControlPointsValues()[2];
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble2(value50, Locale.GERMAN));
                    this.add(cells[x], new Cell(3,4,1,2));
                }else if (x == 11) {
                    double value95 = calculation.getControlPointsValues()[3];
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble2(value95, Locale.GERMAN));
                    this.add(cells[x], new Cell(3,6,1,2));
                }else if (x == 12){
                    cells[x] = new ButtonCell(false, "Отримані значення, Хі");
                    this.add(cells[x], new Cell(4, 0, 5, 1));
                }else if (x < 18){
                    index = x - 12;
                    cells[x] = new ButtonCell(false, String.valueOf(index));
                    this.add(cells[x], new Cell(index + 3, 1, 1, 1));
                }else if (x < 24){
                    index = x - 17;
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble3(calculation.getIn()[0][index], Locale.GERMAN));
                    this.add(cells[x], new Cell(4, index + 1, 1, 1));
                }else if (x < 30){
                    index = x - 23;
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble3(calculation.getIn()[1][index], Locale.GERMAN));
                    this.add(cells[x], new Cell(5, index + 1, 1, 1));
                }else if (x < 36){
                    index = x - 29;
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble3(calculation.getIn()[2][index], Locale.GERMAN));
                    this.add(cells[x], new Cell(6, index + 1, 1, 1));
                }else if (x < 42){
                    index = x - 35;
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble3(calculation.getIn()[3][index], Locale.GERMAN));
                    this.add(cells[x], new Cell(7, index + 1, 1, 1));
                }else {
                    index = x -41;
                    cells[x] = new ButtonCell(false, VariableConverter.roundingDouble3(calculation.getIn()[4][index], Locale.GERMAN));
                    this.add(cells[x], new Cell(8, index + 1, 1, 1));
                }
            }
        }

    }

    private class TableCertificate extends JPanel {

        protected TableCertificate(){
            super(new GridBagLayout());

            JButton[] cells = new JButton[14];
            for (int x = 0; x< cells.length; x++){
                cells[x] = new ButtonCell(false);
            }

            cells[0].setText("Назва МХ");
            cells[1].setText("Росширенна невизначенність");
            cells[2].setText("Приведенна похибка");
            cells[3].setText("Абсолютна похибка");
            cells[4].setText("Не усуненна систематична похибка");
            cells[5].setText("Отримані значення МХ");

            String u;
            if (calculation.getExtendedIndeterminacy() < 0.01 && calculation.getExtendedIndeterminacy() > -0.01){
                u  = VariableConverter.roundingDouble3(calculation.getExtendedIndeterminacy(), Locale.GERMAN);
            }else {
                u  = VariableConverter.roundingDouble2(calculation.getExtendedIndeterminacy(), Locale.GERMAN);
            }
            cells[6].setText("U = "
                    + PLUS_MINUS
                    + u
                    + channel.getMeasurement().getValue());

            String gamma;
            if (calculation.getErrorInRangeWidthSensorError() < 0.01 && calculation.getErrorInRangeWidthSensorError() > -0.01){
                gamma  = VariableConverter.roundingDouble3(calculation.getErrorInRangeWidthSensorError(), Locale.GERMAN);
            }else {
                gamma  = VariableConverter.roundingDouble2(calculation.getErrorInRangeWidthSensorError(), Locale.GERMAN);
            }
            cells[7].setText(GAMMA
                    + " вк = "
                    + PLUS_MINUS
                    + gamma
                    + "%");

            String delta;
            if (calculation.getAbsoluteErrorWithSensorError() < 0.01 && calculation.getAbsoluteErrorWithSensorError() > -0.01){
                delta  = VariableConverter.roundingDouble3(calculation.getAbsoluteErrorWithSensorError(), Locale.GERMAN);
            }else {
                delta  = VariableConverter.roundingDouble2(calculation.getAbsoluteErrorWithSensorError(), Locale.GERMAN);
            }
            cells[8].setText(DELTA
                    + " вк = "
                    + PLUS_MINUS
                    + delta
                    + channel.getMeasurement().getValue());

            String s5;
            String s50;
            String s95;

            if (calculation.getSystematicErrors()[0] < 0.01 && calculation.getSystematicErrors()[0] > -0.01){
                s5 = "5% "
                        + DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(calculation.getSystematicErrors()[0], Locale.GERMAN)
                        + channel.getMeasurement().getValue();
            }else {
                s5 = "5% "
                        + DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(calculation.getSystematicErrors()[0], Locale.GERMAN)
                        + channel.getMeasurement().getValue();
            }
            if (calculation.getSystematicErrors()[1] < 0.01 && calculation.getSystematicErrors()[1] > -0.01){
                s50 = "50% "
                        + DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(calculation.getSystematicErrors()[1], Locale.GERMAN)
                        + channel.getMeasurement().getValue();
            }else {
                s50 = "50% "
                        + DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(calculation.getSystematicErrors()[1], Locale.GERMAN)
                        + channel.getMeasurement().getValue();
            }
            if (calculation.getSystematicErrors()[2] < 0.01 && calculation.getSystematicErrors()[2] > -0.01){
                s95 = "95% "
                        + DELTA
                        + "s = "
                        + VariableConverter.roundingDouble3(calculation.getSystematicErrors()[2], Locale.GERMAN)
                        + channel.getMeasurement().getValue();
            }else {
                s95 = "95% "
                        + DELTA
                        + "s = "
                        + VariableConverter.roundingDouble2(calculation.getSystematicErrors()[2], Locale.GERMAN)
                        + channel.getMeasurement().getValue();
            }
            cells[9].setText(s5);
            cells[10].setText(s50);
            cells[11].setText(s95);
            cells[12].setText("Міжконтрольний інтервал");
            cells[13].setText(VariableConverter.roundingDouble(channel.getFrequency(), Locale.GERMAN)
                    + " " + YEAR_WORD(channel.getFrequency()));

            this.add(cells[0], new Cell(0, 0, 1, 1));
            this.add(cells[1], new Cell(0, 1, 1, 1));
            this.add(cells[2], new Cell(0, 2, 1, 1));
            this.add(cells[3], new Cell(0, 3, 1, 1));
            this.add(cells[4], new Cell(0, 4, 1, 3));
            this.add(cells[5], new Cell(1, 0, 1, 1));
            this.add(cells[6], new Cell(1, 1, 1, 1));
            this.add(cells[7], new Cell(1, 2, 1, 1));
            this.add(cells[8], new Cell(1, 3, 1, 1));
            this.add(cells[9], new Cell(1, 4, 1, 1));
            this.add(cells[10], new Cell(1, 5, 1, 1));
            this.add(cells[11], new Cell(1, 6, 1, 1));
            this.add(cells[12], new Cell(2, 0, 1, 1));
            this.add(cells[13], new Cell(2, 1, 1, 6));
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