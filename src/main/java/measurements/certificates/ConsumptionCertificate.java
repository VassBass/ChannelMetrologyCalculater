package measurements.certificates;

import constants.Files;
import constants.MeasurementConstants;
import constants.Strings;
import constants.Value;
import converters.VariableConverter;
import measurements.calculation.Calculation;
import model.Calibrator;
import model.Channel;
import model.Sensor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import support.Settings;
import support.Values;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class ConsumptionCertificate implements Certificate {
    private Calculation result;
    private Values values;
    private Channel channel;

    private String numberOfCertificate;
    private Calendar checkDate;
    private boolean alarmCheck;
    private String alarmValue;
    private String measurementValue;
    private HSSFWorkbook book;
    private File certificateFile;
    private String numberOfReference;
    private String badMessage;

    private HSSFCell cell(int row, int column){
        HSSFSheet sheet = this.book.getSheetAt(0);
        HSSFRow Row = sheet.getRow(row);
        return Row.getCell(column);
    }

    @Override
    public void init(Calculation result, Values values, Channel channel) {
        this.result = result;
        this.values = values;
        this.channel = channel;

        if (this.result.goodChannel()){
            try{
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(Files.FILE_FORM_CONSUMPTION_YOKOGAWA_GOOD));
                this.book = new HSSFWorkbook(fs);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            this.numberOfReference = values.getStringValue(Value.CHANNEL_REFERENCE);
            this.badMessage = values.getStringValue(Value.CHANNEL_IS_GOOD);
            try{
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(Files.FILE_FORM_CONSUMPTION_YOKOGAWA_BAD));
                this.book = new HSSFWorkbook(fs);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void formation() {
        this.putCertificateData();
        this.putChannelData();
        this.putCalibratorData();
        this.putPersons();
    }

    @Override
    public void putCertificateData() {
        this.numberOfCertificate = this.values.getStringValue(Value.CHANNEL_PROTOCOL_NUMBER);
        cell(11,10).setCellValue(this.numberOfCertificate);
        cell(11,32).setCellValue(this.numberOfCertificate);
        if (!this.result.goodChannel()){
            cell(15,65).setCellValue(this.numberOfCertificate);
        }

        this.checkDate = (Calendar) this.values.getValue(Value.CHANNEL_DATE);
        String date = VariableConverter.dateToString(this.checkDate);
        cell(11,12).setCellValue(date);
        cell(11,35).setCellValue(date);
        if (!this.result.goodChannel()){
            cell(10,58).setCellValue(date);
            cell(16,49).setCellValue(date);
        }

        String externalTemperature = values.getStringValue(Value.CALCULATION_EXTERNAL_TEMPERATURE);
        cell(19,12).setCellValue(externalTemperature);

        String humidity = this.values.getStringValue(Value.CALCULATION_EXTERNAL_HUMIDITY);
        cell(20,12).setCellValue(humidity);

        String atmospherePressure = this.values.getStringValue(Value.CALCULATION_EXTERNAL_PRESSURE);
        cell(21,12).setCellValue(atmospherePressure);

        this.alarmCheck = this.values.getBooleanValue(Value.CALCULATION_ALARM_PANEL);
        this.alarmValue = this.values.getStringValue(Value.CALCULATION_ALARM_VALUE);

        String methodName = Settings.getSettingValue(MeasurementConstants.CONSUMPTION.getValue());
        cell(32,37).setCellValue(methodName);
    }

    @Override
    public void putChannelData() {
        String name = this.channel.getName();
        cell(10, 0).setCellValue(name);
        cell(10, 23).setCellValue(name);
        if (!this.result.goodChannel()) {
            cell(13, 49).setCellValue(name);
        }

        String area = this.channel.getArea();
        cell(13, 11).setCellValue(area);
        cell(36, 8).setCellValue(area);
        cell(13, 34).setCellValue(area);
        cell(38, 31).setCellValue(area);
        if (!this.result.goodChannel()) {
            cell(11, 63).setCellValue(area);
            cell(28, 55).setCellValue(area);
        }

        String process = this.channel.getProcess();
        cell(13, 14).setCellValue(process);
        cell(13, 38).setCellValue(process);
        if (!this.result.goodChannel()) {
            cell(12, 63).setCellValue(process);
        }

        String technologyNumber = this.channel.getTechnologyNumber();
        cell(14, 7).setCellValue(technologyNumber);
        cell(14, 30).setCellValue(technologyNumber);

        String code = this.channel.getCode();
        cell(15, 3).setCellValue(code);

        String rangeMin = VariableConverter.roundingDouble(this.channel.getRangeMin(), Locale.GERMAN);
        cell(16, 10).setCellValue(rangeMin);

        String rangeMax = VariableConverter.roundingDouble(this.channel.getRangeMax(), Locale.GERMAN);
        cell(16, 12).setCellValue(rangeMax);

        this.measurementValue = this.channel.getMeasurement().getValue();
        cell(16, 14).setCellValue(this.measurementValue);
        cell(19, 40).setCellValue(this.measurementValue);
        cell(23, 37).setCellValue(this.measurementValue);
        cell(26, 37).setCellValue(this.measurementValue);
        cell(27, 37).setCellValue(this.measurementValue);
        cell(28, 37).setCellValue(this.measurementValue);
        cell(29, 37).setCellValue(this.measurementValue);
        cell(30, 37).setCellValue(this.measurementValue);
        cell(31, 37).setCellValue(this.measurementValue);

        String errorPercent = VariableConverter.roundingDouble2(this.channel.getAllowableErrorPercent(), Locale.GERMAN);
        cell(17, 12).setCellValue(errorPercent);

        String error = VariableConverter.roundingDouble3(this.channel.getAllowableError(), Locale.GERMAN);
        cell(17, 17).setCellValue(error);

        String frequency = VariableConverter.roundingDouble(this.channel.getFrequency(), Locale.GERMAN);
        cell(26, 41).setCellValue(frequency);
        cell(26, 42).setCellValue(Strings.YEAR_WORD(this.channel.getFrequency()));

        String nextDate;
        if (this.result.goodChannel()) {
            long l = (long) (31536000000L * this.channel.getFrequency());
            Calendar nextDateCal = new GregorianCalendar();
            nextDateCal.setTimeInMillis(this.checkDate.getTimeInMillis() + l);
            nextDate = VariableConverter.dateToString(nextDateCal);
        } else {
            nextDate = Strings.EXTRAORDINARY;
        }
        cell(36, 36).setCellValue(nextDate);

        putSensorData();
        putResult();
    }

    @Override
    public void putSensorData() {
        Sensor sensor = this.channel.getSensor();

        String type = sensor.getType();
        cell(9,5).setCellValue(type);
        cell(9,28).setCellValue(type);

        String sensorNum = sensor.getNumber();
        cell(14,15).setCellValue(sensorNum);
        cell(14,37).setCellValue(sensorNum);
        cell(33,41).setCellValue(sensorNum);

        String value= sensor.getValue();
        cell(17,19).setCellValue(value);
    }

    @Override
    public void putCalibratorData() {
        Calibrator calibrator = (Calibrator) this.values.getValue(Value.CALIBRATOR);

        String type = calibrator.getType();
        cell(16,40).setCellValue(type);

        String number = calibrator.getNumber();
        cell(17,28).setCellValue(number);

        String certificate = calibrator.getCertificateToString();
        cell(18,31).setCellValue(certificate);

        double errorCalibrator = calibrator.getError(this.channel);
        double eP = errorCalibrator / (this.channel.getRange() / 100);
        String errorPercent = VariableConverter.roundingDouble2(eP, Locale.GERMAN);
        cell(19,32).setCellValue(errorPercent);

        String error;
        if (errorCalibrator < 0.001) {
            error = VariableConverter.roundingDouble4(errorCalibrator, Locale.GERMAN);
        }else if (errorCalibrator < 0.01){
            error = VariableConverter.roundingDouble3(errorCalibrator, Locale.GERMAN);
        }else {
            error = VariableConverter.roundingDouble2(errorCalibrator, Locale.GERMAN);
        }
        cell(19,38).setCellValue(error);
    }

    @Override
    public void putResult() {
        double value0 = this.channel.getRangeMin();
        double value25 = ((this.channel.getRange() / 100) * 25) + this.channel.getRangeMin();
        double value50 = ((this.channel.getRange() / 100) * 50) + this.channel.getRangeMin();
        double value75 = ((this.channel.getRange() / 100) * 75) + this.channel.getRangeMin();
        double value100 = this.channel.getRangeMax();

        cell(25, 4).setCellValue(VariableConverter.roundingDouble2(value0, Locale.GERMAN));
        cell(27, 4).setCellValue(VariableConverter.roundingDouble2(value25, Locale.GERMAN));
        cell(29, 4).setCellValue(VariableConverter.roundingDouble2(value50, Locale.GERMAN));
        cell(31, 4).setCellValue(VariableConverter.roundingDouble2(value75, Locale.GERMAN));
        cell(33, 4).setCellValue(VariableConverter.roundingDouble2(value100, Locale.GERMAN));

        double[][]measurementValues = this.measurementValues();
        for (int x=0;x<measurementValues.length;x++){
            int column;
            switch (x){
                default: column = 7; break;
                case 1: column = 11; break;
                case 2: column = 13; break;
                case 3: column = 15; break;
                case 4: column = 18; break;

            }
            double[]mv = measurementValues[x];
            for (int y=0;y<mv.length;y++){
                int row = 25 + y;
                cell(row,column).setCellValue(VariableConverter.roundingDouble3(measurementValues[x][y], Locale.GERMAN));
            }
        }

        String u;
        if (this.result.getExtendedIndeterminacy() < 0.01 && this.result.getExtendedIndeterminacy() > -0.01) {
            u = VariableConverter.roundingDouble3(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }else {
            u = VariableConverter.roundingDouble2(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }
        cell(23,35).setCellValue(u);

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
        cell(25,35).setCellValue(errorReduced);
        cell(26,35).setCellValue(absoluteError);

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

        cell(27,34).setCellValue(s0);
        cell(28,34).setCellValue(s25);
        cell(29,34).setCellValue(s50);
        cell(30,34).setCellValue(s75);
        cell(31,34).setCellValue(s100);

        if (!this.result.goodChannel()){
            if (this.badMessage.equals(Strings.CHANNEL_IS_BAD)){
                cell(34,26).setCellValue("не придатним до експлуатації");
            }else {
                cell(34, 26).setCellValue("не придатним до експлуатації".toUpperCase(Locale.ROOT) + " для комерційного обліку");
                cell(35, 23).setCellValue("але" + " придатним".toUpperCase(Locale.ROOT) + " в якості " + "індикатора".toUpperCase(Locale.ROOT));
            }
            return;
        }else {
            cell(34,26).setCellValue("придатним до експлуатації".toUpperCase(Locale.ROOT));
        }

        String alarm;
        if (this.result.closeToFalse() && this.result.goodChannel()) {
            alarm = this.values.getStringValue(Value.CALCULATION_CLOSE_TO_FALSE);
            cell(36, 22).setCellValue(alarm);
        } else if (this.alarmCheck) {
            alarm = Strings.ALARM_MESSAGE + VariableConverter.roundingDouble(Double.parseDouble(alarmValue), Locale.GERMAN) + this.measurementValue;
            cell(36, 22).setCellValue(alarm);
        }
    }

    @Override
    public void putPersons() {
        String headOfArea = this.values.getStringValue(Value.HEAD_OF_AREA_NAME);
        if (headOfArea == null) {
            headOfArea = "________________";
        }
        cell(36,16).setCellValue(headOfArea);
        cell(38,39).setCellValue(headOfArea);
        if (!this.result.goodChannel()){
            cell(28,62).setCellValue(headOfArea);
        }

        String headOfMetrologyArea = this.values.getStringValue(Value.HEAD_OF_METROLOGY_NAME);
        if (headOfMetrologyArea == null){
            headOfMetrologyArea = "________________";
        }
        cell(38,16).setCellValue(headOfMetrologyArea);
        cell(40,39).setCellValue(headOfMetrologyArea);
        if (!this.result.goodChannel()){
            cell(30,62).setCellValue(headOfMetrologyArea);
            cell(40,62).setCellValue(headOfMetrologyArea);
        }

        String performer1Name = this.values.getStringValue(Value.PERFORMER1_NAME);
        if (performer1Name!=null) {
            cell(40, 16).setCellValue(performer1Name);
            cell(40,11).setCellValue("________________");
        }else {
            cell(40,16).setCellValue("");
            cell(40,11).setCellValue("");
        }

        String performer1Position = this.values.getStringValue(Value.PERFORMER1_POSITION);
        if (performer1Position!=null) {
            cell(40, 0).setCellValue(performer1Position);
        }else {
            cell(40,0).setCellValue("");
        }

        String performer2Name = this.values.getStringValue(Value.PERFORMER2_NAME);
        if (performer2Name!=null) {
            cell(42, 16).setCellValue(performer2Name);
            cell(42,11).setCellValue("________________");
        }else{
            cell(42,16).setCellValue("");
            cell(42,11).setCellValue("");
        }

        String performer2Position = this.values.getStringValue(Value.PERFORMER2_POSITION);
        if (performer2Position!=null) {
            cell(42, 0).setCellValue(performer2Position);
        }else {
            cell(42,0).setCellValue("");
        }

        String calculaterName = this.values.getStringValue(Value.CALCULATER_NAME);
        if (calculaterName == null){
            calculaterName = "________________";
        }
        cell(42,39).setCellValue(calculaterName);
        if (!this.result.goodChannel()){
            cell(32,62).setCellValue(calculaterName);
        }

        String calculaterPosition = this.values.getStringValue(Value.CALCULATER_POSITION);
        if (calculaterPosition == null){
            calculaterPosition = "________________";
        }
        cell(42,23).setCellValue(calculaterPosition);
        if (!this.result.goodChannel()){
            cell(32,48).setCellValue(calculaterPosition);
        }

        if (!this.result.goodChannel()){
            String headOfASUTPDepartment = this.values.getStringValue(Value.HEAD_OF_DEPARTMENT);
            if (headOfASUTPDepartment == null){
                headOfASUTPDepartment = "________________";
            }
            cell(26,62).setCellValue(headOfASUTPDepartment);

            cell(10,55).setCellValue(this.numberOfReference);

        }
    }

    @Override
    public void save() {
        String fileName = "№"
                + this.numberOfCertificate
                + " ("
                + VariableConverter.dateToString(this.checkDate)
                + ").xls";
        this.certificateFile = Files.certificateFile(fileName);
        try {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(this.certificateFile));
            this.book.write(out);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void show() {
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                desktop.open(this.certificateFile);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void print() {
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                desktop.print(this.certificateFile);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void openInExplorer(){
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                desktop.open(Files.CERTIFICATES_DIR);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public File getCertificateFile() {
        return this.certificateFile;
    }

    private double[][]measurementValues(){
        double[][]measurements = new double[5][8];
        double[]measurement1 = (double[]) this.values.getValue(Value.MEASUREMENT_1);
        double[]measurement2 = (double[]) this.values.getValue(Value.MEASUREMENT_2);
        double[]measurement3 = (double[]) this.values.getValue(Value.MEASUREMENT_3);
        double[]measurement4 = (double[]) this.values.getValue(Value.MEASUREMENT_4);
        double[]measurement5 = (double[]) this.values.getValue(Value.MEASUREMENT_5);

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
