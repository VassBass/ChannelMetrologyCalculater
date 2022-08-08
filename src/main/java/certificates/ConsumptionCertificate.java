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

public class ConsumptionCertificate extends Certificate {
    private static final String EXTRAORDINARY = "Позачерговий";
    private static final String CHANNEL_IS_BAD = "Канал не придатний";
    private static final String ALARM_MESSAGE = "Сигналізація спрацювала при t = ";

    private String badMessage;

    @Override
    public void init(Calculation result, HashMap<Integer, Object> values, Channel channel) {
        super.init(result, values, channel);
        SystemData systemData = (SystemData) values.get(Key.SYS);
        SystemData os = systemData == null ? SystemData.osName() : systemData;
        File formFile;

        if (this.result.goodChannel()){
            if (os == SystemData.SYS_UNIX){
                formFile = FileBrowser.LINUX_FILE_CONSUMPTION_GOOD;
            }else {
                formFile = FileBrowser.FILE_CONSUMPTION_GOOD;
            }
        }else{
            this.numberOfReference = (String) values.get(Key.CHANNEL_REFERENCE);
            this.badMessage = (String) values.get(Key.CHANNEL_IS_GOOD);
            if (os == SystemData.SYS_UNIX){
                formFile = FileBrowser.LINUX_FILE_CONSUMPTION_BAD;
            }else {
                formFile = FileBrowser.FILE_CONSUMPTION_BAD;
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
        cell(13,2).setCellValue(this.numberOfCertificate);
        cell(13,11).setCellValue(this.numberOfCertificate);
        if (!this.result.goodChannel()){
            cell(19,20).setCellValue(this.numberOfCertificate);
        }

        this.checkDate = (String) this.values.get(Key.CHANNEL_DATE);
        cell(13,5).setCellValue(this.checkDate);
        cell(13,14).setCellValue(this.checkDate);
        if (!this.result.goodChannel()){
            cell(10,24).setCellValue(this.checkDate);
        }

        String externalTemperature = (String) values.get(Key.CALCULATION_EXTERNAL_TEMPERATURE);
        cell(23,4).setCellValue(externalTemperature);

        String humidity = (String) this.values.get(Key.CALCULATION_EXTERNAL_HUMIDITY);
        cell(24,4).setCellValue(humidity);

        String atmospherePressure = (String) this.values.get(Key.CALCULATION_EXTERNAL_PRESSURE);
        cell(25,4).setCellValue(atmospherePressure);

        this.alarmCheck = (boolean) this.values.get(Key.CALCULATION_ALARM_PANEL);
        this.alarmValue = (String) this.values.get(Key.CALCULATION_ALARM_VALUE);

        String methodName = Settings.getSettingValue(Measurement.CONSUMPTION);
        cell(33,15).setCellValue(methodName);
    }

    @Override
    public void putChannelData() {
        String name = this.channel.getName();
        cell(10, 0).setCellValue(name);
        cell(10, 9).setCellValue(name);
        if (!this.result.goodChannel()) {
            cell(13, 18).setCellValue(name);
        }

        String code = this.channel.getCode();
        cell(15, 4).setCellValue(code);

        String technologyNumber = this.channel.getTechnologyNumber();
        cell(16, 4).setCellValue(technologyNumber);
        cell(15, 13).setCellValue(technologyNumber);

        String area = this.channel.getArea();
        cell(17, 4).setCellValue(area);
        cell(41, 3).setCellValue(area);
        cell(16, 13).setCellValue(area);
        cell(43, 12).setCellValue(area);
        if (!this.result.goodChannel()) {
            cell(16, 22).setCellValue(area);
            cell(29, 21).setCellValue(area);
        }

        String process = this.channel.getProcess();
        cell(17, 6).setCellValue(process);
        cell(16, 15).setCellValue(process);
        if (!this.result.goodChannel()) {
            cell(16, 24).setCellValue(process);
        }

        String rangeMin = VariableConverter.roundingDouble(this.channel.getRangeMin(), Locale.GERMAN);
        cell(19, 5).setCellValue(rangeMin);

        String rangeMax = VariableConverter.roundingDouble(this.channel.getRangeMax(), Locale.GERMAN);
        cell(19, 7).setCellValue(rangeMax);

        this.measurementValue = this.channel.getMeasurementValue();
        cell(19, 8).setCellValue(this.measurementValue);
        cell(20, 8).setCellValue(this.measurementValue);
        cell(29, 2).setCellValue(this.measurementValue);
        cell(20, 16).setCellValue(this.measurementValue);
        cell(24, 15).setCellValue(this.measurementValue);
        cell(27, 15).setCellValue(this.measurementValue);
        cell(28, 15).setCellValue(this.measurementValue);
        cell(29, 15).setCellValue(this.measurementValue);
        cell(30, 15).setCellValue(this.measurementValue);
        cell(31, 15).setCellValue(this.measurementValue);
        cell(32, 15).setCellValue(this.measurementValue);

        String errorPercent = VariableConverter.roundingDouble2(this.channel.getAllowableErrorPercent(), Locale.GERMAN);
        cell(20, 5).setCellValue(errorPercent);
        cell(38, 15).setCellValue(errorPercent);

        String error = VariableConverter.roundingDouble3(this.channel.getAllowableError(), Locale.GERMAN);
        cell(20, 7).setCellValue(error);

        String frequency = VariableConverter.roundingDouble(this.channel.getFrequency(), Locale.GERMAN);
        cell(24, 16).setCellValue(frequency + " " + YEAR_WORD(this.channel.getFrequency()));

        String nextDate;
        if (this.result.goodChannel()) {
            long l = (long) (31536000000L * this.channel.getFrequency());
            Calendar nextDateCal = new GregorianCalendar();
            nextDateCal.setTimeInMillis(VariableConverter.stringToDate(this.checkDate).getTimeInMillis() + l);
            nextDate = VariableConverter.dateToString(nextDateCal);
        } else {
            nextDate = EXTRAORDINARY;
        }
        cell(39, 14).setCellValue(nextDate);

        putSensorData();
        putResult();
    }

    @Override
    public void putSensorData() {
        Sensor sensor = this.channel.getSensor();

        String type = sensor.getType();
        cell(11,4).setCellValue(type);
        cell(11,13).setCellValue(type);
        cell(34,11).setCellValue(type);
        if (!this.result.goodChannel()){
            cell(14,20).setCellValue(type);
        }

        String sensorNum = sensor.getNumber();
        cell(18,4).setCellValue(sensorNum);
        cell(35,11).setCellValue(sensorNum);
        if (!this.result.goodChannel()){
            cell(15,20).setCellValue(sensorNum);
        }
    }

    @Override
    public void putCalibratorData() {
        Calibrator calibrator = (Calibrator) this.values.get(Key.CALIBRATOR);

        String type = calibrator.getType();
        cell(17,15).setCellValue(type);

        String number = calibrator.getNumber();
        cell(18,12).setCellValue(number);

        String certificate = calibrator.getCertificateInfo();
        cell(18,9).setCellValue(calibrator._getCertificateType());
        cell(18,12).setCellValue(certificate);

        double errorCalibrator = calibrator.getError(this.channel);
        double eP = errorCalibrator / (this.channel._getRange() / 100);
        String errorPercent = VariableConverter.roundingDouble2(eP, Locale.GERMAN);
        cell(20,13).setCellValue(errorPercent);

        String error;
        if (errorCalibrator < 0.001) {
            error = VariableConverter.roundingDouble4(errorCalibrator, Locale.GERMAN);
        }else if (errorCalibrator < 0.01){
            error = VariableConverter.roundingDouble3(errorCalibrator, Locale.GERMAN);
        }else {
            error = VariableConverter.roundingDouble2(errorCalibrator, Locale.GERMAN);
        }
        cell(20,15).setCellValue(error);
    }

    @Override
    public void putResult() {
        double[] values = this.result.getControlPointsValues();
        cell(30, 2).setCellValue(VariableConverter.roundingDouble2(values[0], Locale.GERMAN));
        cell(32, 2).setCellValue(VariableConverter.roundingDouble2(values[1], Locale.GERMAN));
        cell(34, 2).setCellValue(VariableConverter.roundingDouble2(values[2], Locale.GERMAN));
        cell(36, 2).setCellValue(VariableConverter.roundingDouble2(values[3], Locale.GERMAN));
        cell(38, 2).setCellValue(VariableConverter.roundingDouble2(values[4], Locale.GERMAN));

        double[][]measurementValues = this.measurementValues();
        for (int x=0;x<measurementValues.length;x++){
            int column = 3 + x;
            double[]mv = measurementValues[x];
            for (int y=0;y<mv.length;y++){
                int row = 30 + y;
                cell(row,column).setCellValue(VariableConverter.roundingDouble3(measurementValues[x][y], Locale.GERMAN));
            }
        }

        String u;
        if (this.result.getExtendedIndeterminacy() < 0.01 && this.result.getExtendedIndeterminacy() > -0.01) {
            u = VariableConverter.roundingDouble3(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }else {
            u = VariableConverter.roundingDouble2(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }
        cell(24,14).setCellValue(u);

        String errorReduced;
        if (this.result.getErrorInRange() < 0.01 && this.result.getErrorInRange() > -0.01) {
            errorReduced = VariableConverter.roundingDouble3(this.result.getErrorInRange(), Locale.GERMAN);
        }else {
            errorReduced = VariableConverter.roundingDouble2(this.result.getErrorInRange(), Locale.GERMAN);
        }

        String absoluteError;
        if (this.result.getMaxAbsoluteError() < 0.01 && this.result.getMaxAbsoluteError() > -0.01) {
            absoluteError = VariableConverter.roundingDouble3(this.result.getMaxAbsoluteError(), Locale.GERMAN);
        }else {
            absoluteError = VariableConverter.roundingDouble2(this.result.getMaxAbsoluteError(), Locale.GERMAN);
        }
        cell(26,14).setCellValue(errorReduced);
        cell(38,13).setCellValue(errorReduced);
        cell(27,14).setCellValue(absoluteError);

        String s0;
        String s25;
        String s50;
        String s75;
        String s100;

        if (this.result.getSystematicErrors()[0] < 0.01 && this.result.getSystematicErrors()[0] > -0.01){
            s0 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[0], Locale.GERMAN);
        }else {
            s0 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[0], Locale.GERMAN);
        }
        if (this.result.getSystematicErrors()[1] < 0.01 && this.result.getSystematicErrors()[1] > -0.01){
            s25 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[1], Locale.GERMAN);
        }else {
            s25 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[1], Locale.GERMAN);
        }
        if (this.result.getSystematicErrors()[2] < 0.01 && this.result.getSystematicErrors()[2] > -0.01){
            s50 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[2], Locale.GERMAN);
        }else {
            s50 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[2], Locale.GERMAN);
        }
        if (this.result.getSystematicErrors()[3] < 0.01 && this.result.getSystematicErrors()[3] > -0.01){
            s75 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[3], Locale.GERMAN);
        }else {
            s75 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[3], Locale.GERMAN);
        }
        if (this.result.getSystematicErrors()[4] < 0.01 && this.result.getSystematicErrors()[4] > -0.01){
            s100 = VariableConverter.roundingDouble3(this.result.getSystematicErrors()[4], Locale.GERMAN);
        }else {
            s100 = VariableConverter.roundingDouble2(this.result.getSystematicErrors()[4], Locale.GERMAN);
        }

        cell(28,13).setCellValue(s0);
        cell(29,13).setCellValue(s25);
        cell(30,13).setCellValue(s50);
        cell(31,13).setCellValue(s75);
        cell(32,13).setCellValue(s100);

        if (!this.result.goodChannel()){
            if (!this.badMessage.equals(CHANNEL_IS_BAD)){
                cell(37, 11).setCellValue("не придатним до експлуатації".toUpperCase(Locale.ROOT) + " для комерційного обліку");
                cell(38, 9).setCellValue("але" + " придатним".toUpperCase(Locale.ROOT) + " в якості " + "індикатора".toUpperCase(Locale.ROOT));
            }
            return;
        }

        String alarm;
        if (this.result.closeToFalse() && this.result.goodChannel()) {
            alarm = (String) this.values.get(Key.CALCULATION_CLOSE_TO_FALSE);
            cell(38, 9).setCellValue(alarm);
        } else if (this.alarmCheck) {
            alarm = ALARM_MESSAGE + VariableConverter.roundingDouble(Double.parseDouble(alarmValue), Locale.GERMAN) + this.measurementValue;
            cell(38, 9).setCellValue(alarm);
        }else {
            cell(38,9).setCellValue("");
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
        double[][]measurements = new double[5][10];
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