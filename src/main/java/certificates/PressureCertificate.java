package certificates;

import calculation.Calculation;
import constants.Key;
import converters.VariableConverter;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import model.Sensor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import service.FileBrowser;
import service.SystemData;
import settings.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class PressureCertificate extends Certificate {
    private static final String EXTRAORDINARY = "Позачерговий";
    private static final String ALARM_MESSAGE = "Сигналізація спрацювала при t = ";

    @Override
    public void init(Calculation result, HashMap<Integer, Object> values, Channel channel) {
        super.init(result, values, channel);
        SystemData systemData = (SystemData) values.get(Key.SYS);
        SystemData os = systemData == null ? SystemData.osName() : systemData;
        File formFile;

        if (this.result.goodChannel()){
            if (os == SystemData.SYS_UNIX){
                formFile = FileBrowser.LINUX_FILE_PRESSURE_GOOD;
            }else {
                formFile = FileBrowser.FILE_PRESSURE_GOOD;
            }
        }else{
            this.numberOfReference = (String) values.get(Key.CHANNEL_REFERENCE);
            if (os == SystemData.SYS_UNIX){
                formFile = FileBrowser.LINUX_FILE_PRESSURE_BAD;
            }else {
                formFile = FileBrowser.FILE_PRESSURE_BAD;
            }
        }
        try{
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(formFile));
            this.book = new HSSFWorkbook(fs);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void putCertificateData() {
        this.numberOfCertificate = (String) this.values.get(Key.CHANNEL_PROTOCOL_NUMBER);
        cell(12,2).setCellValue(this.numberOfCertificate);
        cell(12,11).setCellValue(this.numberOfCertificate);
        if (!this.result.goodChannel()){
            cell(18,20).setCellValue(this.numberOfCertificate);
        }

        this.checkDate = (String) this.values.get(Key.CHANNEL_DATE);
        cell(12,5).setCellValue(this.checkDate);
        cell(12,14).setCellValue(this.checkDate);
        if (!this.result.goodChannel()){
            cell(10,24).setCellValue(this.checkDate);
        }

        String externalTemperature = (String) values.get(Key.CALCULATION_EXTERNAL_TEMPERATURE);
        cell(25,4).setCellValue(externalTemperature);

        String humidity = (String) this.values.get(Key.CALCULATION_EXTERNAL_HUMIDITY);
        cell(26,4).setCellValue(humidity);

        String atmospherePressure = (String) this.values.get(Key.CALCULATION_EXTERNAL_PRESSURE);
        cell(27,4).setCellValue(atmospherePressure);

        this.alarmCheck = (boolean) this.values.get(Key.CALCULATION_ALARM_PANEL);
        this.alarmValue = (String) this.values.get(Key.CALCULATION_ALARM_VALUE);

        String methodName = Settings.getSettingValue(Measurement.PRESSURE);
        cell(32,15).setCellValue(methodName);
    }

    @Override
    public void putChannelData() {
        String name = this.channel.getName();
        cell(10,0).setCellValue(name);
        cell(10,9).setCellValue(name);
        cell(34,9).setCellValue(name);
        if (!this.result.goodChannel()){
            cell(13,18).setCellValue(name);
        }

        String code = this.channel.getCode();
        cell(14,4).setCellValue(code);

        String technologyNumber = this.channel.getTechnologyNumber();
        cell(15,4).setCellValue(technologyNumber);
        cell(14,13).setCellValue(technologyNumber);

        String area = this.channel.getArea();
        cell(16,4).setCellValue(area);
        cell(41,3).setCellValue(area);
        cell(15,13).setCellValue(area);
        cell(43,12).setCellValue(area);
        if (!this.result.goodChannel()){
            cell(14,22).setCellValue(area);
            cell(29,21).setCellValue(area);
        }

        String process = this.channel.getProcess();
        cell(16,6).setCellValue(process);
        cell(15,15).setCellValue(process);
        if (!this.result.goodChannel()){
            cell(14,24).setCellValue(process);
        }

        String rangeMin = VariableConverter.roundingDouble(this.channel.getRangeMin(), Locale.GERMAN);
        cell(17,5).setCellValue(rangeMin);

        String rangeMax = VariableConverter.roundingDouble(this.channel.getRangeMax(), Locale.GERMAN);
        cell(17,7).setCellValue(rangeMax);

        this.measurementValue = this.channel._getMeasurementValue();
        cell(17,8).setCellValue(this.measurementValue);
        cell(18,8).setCellValue(this.measurementValue);
        cell(21,8).setCellValue(this.measurementValue);
        cell(22,8).setCellValue(this.measurementValue);
        cell(32,2).setCellValue(this.measurementValue);
        cell(19,16).setCellValue(this.measurementValue);
        cell(24,15).setCellValue(this.measurementValue);
        cell(27,15).setCellValue(this.measurementValue);
        cell(28,15).setCellValue(this.measurementValue);
        cell(29,15).setCellValue(this.measurementValue);
        cell(30,15).setCellValue(this.measurementValue);

        String errorPercent = VariableConverter.roundingDouble2(this.channel.getAllowableErrorPercent(), Locale.GERMAN);
        cell(18,5).setCellValue(errorPercent);
        cell(36,15).setCellValue(errorPercent);

        String error = VariableConverter.roundingDouble2(this.channel.getAllowableError(), Locale.GERMAN);
        cell(18,7).setCellValue(error);

        String frequency = VariableConverter.roundingDouble(this.channel.getFrequency(), Locale.GERMAN);
        cell(24,16).setCellValue(frequency + " " +  YEAR_WORD(this.channel.getFrequency()));

        String nextDate;
        if (this.result.goodChannel()){
            long l = (long) (31536000000L * this.channel.getFrequency());
            Calendar nextDateCal = new GregorianCalendar();
            nextDateCal.setTimeInMillis(VariableConverter.stringToDate(this.checkDate).getTimeInMillis() + l);
            nextDate = VariableConverter.dateToString(nextDateCal);
        }else {
            nextDate = EXTRAORDINARY;
        }
        cell(38,14).setCellValue(nextDate);

        putSensorData();
        putResult();
    }

    @Override
    public void putSensorData() {
        Sensor sensor = this.channel.getSensor();

        String type = sensor.getType();
        cell(20,4).setCellValue(type);

        double errorSensor = sensor.getError(this.channel);
        double eP = errorSensor / (sensor._getRange() / 100);
        String errorPercent = eP < 0.01 ? VariableConverter.roundingDouble3(eP, Locale.GERMAN) : VariableConverter.roundingDouble2(eP, Locale.GERMAN);
        cell(21,5).setCellValue(errorPercent);

        String error = errorSensor < 0.02 ? VariableConverter.roundingDouble3(errorSensor, Locale.GERMAN) : VariableConverter.roundingDouble2(errorSensor, Locale.GERMAN);
        cell(21,7).setCellValue(error);

        double min = this.channel.getMeasurement().convertFrom(sensor.getValue(), sensor.getRangeMin());
        double max = this.channel.getMeasurement().convertFrom(sensor.getValue(), sensor.getRangeMax());
        String rangeMin = VariableConverter.roundingDouble2(min, Locale.GERMAN);
        String rangeMax = VariableConverter.roundingDouble2(max, Locale.GERMAN);
        cell(22,5).setCellValue(rangeMin);
        cell(22,7).setCellValue(rangeMax);
    }

    @Override
    public void putCalibratorData() {
        Calibrator calibrator = (Calibrator) this.values.get(Key.CALIBRATOR);

        String type = calibrator.getType();
        cell(16,15).setCellValue(type);

        String number = calibrator.getNumber();
        cell(17,12).setCellValue(number);

        String certificate = calibrator.getCertificateInfo();
        cell(18,12).setCellValue(certificate);

        double errorCalibrator = calibrator.getError(this.channel);
        double eP;
        if (calibrator.getValue().equals(this.channel._getMeasurementValue())) {
            eP = errorCalibrator / (this.channel._getRange() / 100);
        }else {
            double calibratorRange = calibrator._getRange();
            double convertedCalibratorRange = this.channel.getMeasurement().convertFrom(calibrator.getValue(), calibratorRange);
            eP = errorCalibrator / (convertedCalibratorRange/100);
        }
        String errorPercent = VariableConverter.roundingDouble2(eP, Locale.GERMAN);
        cell(19,13).setCellValue(errorPercent);

        String error;
        if (errorCalibrator < 0.01){
            error = VariableConverter.roundingDouble3(errorCalibrator, Locale.GERMAN);
        }else {
            error = VariableConverter.roundingDouble2(errorCalibrator, Locale.GERMAN);
        }
        cell(19,15).setCellValue(error);
    }

    @Override
    public void putResult() {
        double[]cpv = this.result.getControlPointsValues();
        double value5 = cpv[1];
        double value50 = cpv[2];
        double value95 = cpv[3];
        Calibrator calibrator = (Calibrator) this.values.get(Key.CALIBRATOR);

        if (calibrator.getType().equals(Calibrator.FLUKE718_30G)){
            double maxCalibratorPower = this.channel.getMeasurement().convertFrom(Measurement.KGS_SM2, -0.8);
            if (value5 < maxCalibratorPower){
                value5 = maxCalibratorPower;
            }
        }

        cell(33, 2).setCellValue(VariableConverter.roundingDouble2(value5, Locale.GERMAN));
        cell(35, 2).setCellValue(VariableConverter.roundingDouble2(value50, Locale.GERMAN));
        cell(37, 2).setCellValue(VariableConverter.roundingDouble2(value95, Locale.GERMAN));

        double[][]measurementValues = this.measurementValues();
        int column = 3;
        for (double[] value : measurementValues) {
            int row = 32;
            for (int z = 0; z < 6; z++) {
                cell(++row, column).setCellValue(VariableConverter.roundingDouble3(value[z + 1], Locale.GERMAN));
            }
            column++;
        }

        String u;
        if (this.result.getExtendedIndeterminacy() < 0.01 && this.result.getExtendedIndeterminacy() > -0.01) {
            u = VariableConverter.roundingDouble3(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }else {
            u = VariableConverter.roundingDouble2(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }
        cell(24,14).setCellValue(u);

        String errorReduced;
        if (this.result.getErrorInRangeWidthSensorError() < 0.01 && this.result.getErrorInRangeWidthSensorError() > -0.01) {
            errorReduced = VariableConverter.roundingDouble3(this.result.getErrorInRangeWidthSensorError(), Locale.GERMAN);
        }else {
            errorReduced = VariableConverter.roundingDouble2(this.result.getErrorInRangeWidthSensorError(), Locale.GERMAN);
        }

        String absoluteError;
        if (this.result.getAbsoluteErrorWithSensorError() < 0.01 && this.result.getAbsoluteErrorWithSensorError() > -0.01) {
            absoluteError = VariableConverter.roundingDouble3(this.result.getAbsoluteErrorWithSensorError(), Locale.GERMAN);
        }else {
            absoluteError = VariableConverter.roundingDouble2(this.result.getAbsoluteErrorWithSensorError(), Locale.GERMAN);
        }
        cell(26,14).setCellValue(errorReduced);
        cell(36,13).setCellValue(errorReduced);
        cell(27,14).setCellValue(absoluteError);

        String s5;
        String s50;
        String s95;

        if (this.result.getSystematicErrors()[0] < 0.01 && this.result.getSystematicErrors()[0] > -0.01){
            s5 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[0], Locale.GERMAN);
        }else {
            s5 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[0], Locale.GERMAN);
        }
        if (this.result.getSystematicErrors()[1] < 0.01 && this.result.getSystematicErrors()[1] > -0.01){
            s50 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[1], Locale.GERMAN);
        }else {
            s50 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[1], Locale.GERMAN);
        }
        if (this.result.getSystematicErrors()[2] < 0.01 && this.result.getSystematicErrors()[2] > -0.01){
            s95 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[2], Locale.GERMAN);
        }else {
            s95 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[2], Locale.GERMAN);
        }
        cell(28,13).setCellValue(s5);
        cell(29,13).setCellValue(s50);
        cell(30,13).setCellValue(s95);

        String alarm;
        if (this.result.closeToFalse() && this.result.goodChannel()) {
            alarm = (String) this.values.get(Key.CALCULATION_CLOSE_TO_FALSE);
            cell(37, 9).setCellValue(alarm);
        } else if (this.alarmCheck) {
            alarm = ALARM_MESSAGE + VariableConverter.roundingDouble(Double.parseDouble(alarmValue), Locale.GERMAN) + this.measurementValue;
            cell(37, 9).setCellValue(alarm);
        }else {
            cell(37,9).setCellValue(" ");
        }
    }

    @Override
    public void putPersons() {
        String headOfArea = (String) this.values.get(Key.HEAD_OF_AREA_NAME);
        if (headOfArea == null) {
            headOfArea = "________________";
        }
        cell(41,6).setCellValue(headOfArea);
        cell(43,15).setCellValue(headOfArea);
        if (!this.result.goodChannel()){
            cell(29,24).setCellValue(headOfArea);
        }

        String headOfMetrologyArea = (String) this.values.get(Key.HEAD_OF_METROLOGY_NAME);
        if (headOfMetrologyArea == null){
            headOfMetrologyArea = "________________";
        }
        cell(43,6).setCellValue(headOfMetrologyArea);
        cell(45,15).setCellValue(headOfMetrologyArea);
        if (!this.result.goodChannel()){
            cell(32,24).setCellValue(headOfMetrologyArea);
            cell(47,24).setCellValue(headOfMetrologyArea);
        }

        String performer1Name = (String) this.values.get(Key.PERFORMER1_NAME);
        if (performer1Name!=null) {
            cell(45, 6).setCellValue(performer1Name);
        }else {
            cell(45,6).setCellValue("");
            cell(45,4).setCellValue("");
        }

        String performer1Position = (String) this.values.get(Key.PERFORMER1_POSITION);
        if (performer1Position!=null) {
            cell(45, 0).setCellValue(performer1Position);
        }else {
            cell(45,0).setCellValue("");
        }

        String performer2Name = (String) this.values.get(Key.PERFORMER2_NAME);
        if (performer2Name!=null) {
            cell(47, 6).setCellValue(performer2Name);
        }else{
            cell(47,6).setCellValue("");
            cell(47,4).setCellValue("");
        }

        String performer2Position = (String) this.values.get(Key.PERFORMER2_POSITION);
        if (performer2Position!=null) {
            cell(47, 0).setCellValue(performer2Position);
        }else {
            cell(47,0).setCellValue("");
        }

        String calculaterName = (String) this.values.get(Key.CALCULATER_NAME);
        if (calculaterName == null){
            calculaterName = "________________";
        }
        cell(47,15).setCellValue(calculaterName);
        if (!this.result.goodChannel()){
            cell(35,24).setCellValue(calculaterName);
        }

        String calculaterPosition = (String) this.values.get(Key.CALCULATER_POSITION);
        if (calculaterPosition == null){
            calculaterPosition = "________________";
        }
        cell(47,9).setCellValue(calculaterPosition);
        if (!this.result.goodChannel()){
            cell(35,18).setCellValue(calculaterPosition);
        }

        if (!this.result.goodChannel()){
            String headOfASUTPDepartment = (String) this.values.get(Key.HEAD_OF_DEPARTMENT);
            if (headOfASUTPDepartment == null){
                headOfASUTPDepartment = "________________";
            }
            cell(26,24).setCellValue(headOfASUTPDepartment);

            cell(10,21).setCellValue(this.numberOfReference);

        }
    }

    @Override
    protected double[][]measurementValues(){
        double[][]measurements = new double[5][8];
        double[]measurement1 = (double[]) this.values.get(Key.MEASUREMENT_1);
        double[]measurement2 = (double[]) this.values.get(Key.MEASUREMENT_2);
        double[]measurement3 = (double[]) this.values.get(Key.MEASUREMENT_3);
        double[]measurement4 = (double[]) this.values.get(Key.MEASUREMENT_4);
        double[]measurement5 = (double[]) this.values.get(Key.MEASUREMENT_5);

        if (measurement2 == null){
            measurement2 = measurement1;
        }
        if (measurement3 == null){
            measurement3 = measurement1;
        }
        if (measurement4 == null){
            measurement4 = measurement2;
        }
        if (measurement5 == null){
            measurement5 = measurement3;
        }
        measurements[0] = measurement1;
        measurements[1] = measurement2;
        measurements[2] = measurement3;
        measurements[3] = measurement4;
        measurements[4] = measurement5;

        return measurements;
    }
}