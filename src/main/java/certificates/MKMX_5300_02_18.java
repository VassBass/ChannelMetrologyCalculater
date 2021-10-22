package certificates;

import calculation.Calculation;
import constants.*;
import calibrators.Calibrator;
import converters.PressureConverter;
import converters.VariableConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import support.Sensor;
import support.Channel;
import support.Values;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class MKMX_5300_02_18 implements Certificate {
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
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(Files.FILE_FORM_MKMX_5300_02_18_GOOD));
                this.book = new HSSFWorkbook(fs);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            this.numberOfReference = values.getStringValue(Value.CHANNEL_REFERENCE);
            try{
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(Files.FILE_FORM_MKMX_5300_02_18_BAD));
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
        cell(11,31).setCellValue(this.numberOfCertificate);

        this.checkDate = (Calendar) this.values.getValue(Value.CHANNEL_DATE);
        String date = VariableConverter.dateToString(this.checkDate);
        cell(11,12).setCellValue(date);
        cell(11,34).setCellValue(date);
        if (!this.result.goodChannel()){
            cell(9,55).setCellValue(date);
            cell(16,46).setCellValue(date);
        }

        String externalTemperature = values.getStringValue(Value.CALCULATION_EXTERNAL_TEMPERATURE);
        cell(23,11).setCellValue(externalTemperature);

        String humidity = this.values.getStringValue(Value.CALCULATION_EXTERNAL_HUMIDITY);
        cell(24,11).setCellValue(humidity);

        String atmospherePressure = this.values.getStringValue(Value.CALCULATION_EXTERNAL_PRESSURE);
        cell(25,11).setCellValue(atmospherePressure);

        this.alarmCheck = this.values.getBooleanValue(Value.CALCULATION_ALARM_PANEL);
        this.alarmValue = this.values.getStringValue(Value.CALCULATION_ALARM_VALUE);
    }

    @Override
    public void putChannelData() {
        String name = this.channel.getName();
        cell(9,0).setCellValue(name);
        cell(9,22).setCellValue(name);
        cell(32,22).setCellValue(name);
        if (!this.result.goodChannel()){
            cell(13,46).setCellValue(name);
        }

        String area = this.channel.getArea();
        cell(13,11).setCellValue(area);
        cell(36,8).setCellValue(area);
        cell(13,33).setCellValue(area);
        cell(38,30).setCellValue(area);
        if (!this.result.goodChannel()){
            cell(11,60).setCellValue(area);
            cell(13,11).setCellValue(area);
        }

        String process = this.channel.getProcess();
        cell(13,14).setCellValue(process);
        cell(13,37).setCellValue(process);
        if (!this.result.goodChannel()){
            cell(12,60).setCellValue(process);
        }

        String technologyNumber = this.channel.getTechnologyNumber();
        cell(14,7).setCellValue(technologyNumber);
        cell(14,29).setCellValue(technologyNumber);
        if (!this.result.goodChannel()){
            String name_ = name + "  " + technologyNumber;
            cell(13,46).setCellValue(name_);
        }

        String code = this.channel.getCode();
        cell(15,3).setCellValue(code);
        cell(15,25).setCellValue(code);

        String rangeMin = VariableConverter.roundingDouble(this.channel.getRangeMin(), Locale.GERMAN);
        cell(16,10).setCellValue(rangeMin);

        String rangeMax = VariableConverter.roundingDouble(this.channel.getRangeMax(), Locale.GERMAN);
        cell(16,12).setCellValue(rangeMax);

        this.measurementValue = this.channel.getMeasurement().getValue();
        cell(16,14).setCellValue(this.measurementValue);
        cell(17,18).setCellValue(this.measurementValue);
        cell(20,18).setCellValue(this.measurementValue);
        cell(19,39).setCellValue(this.measurementValue);
        cell(23,36).setCellValue(this.measurementValue);
        cell(26,36).setCellValue(this.measurementValue);
        cell(27,36).setCellValue(this.measurementValue);
        cell(28,36).setCellValue(this.measurementValue);
        cell(29,36).setCellValue(this.measurementValue);

        String errorPercent = VariableConverter.roundingDouble2(this.channel.getAllowableErrorPercent(), Locale.GERMAN);
        cell(17,12).setCellValue(errorPercent);
        cell(34,34).setCellValue(errorPercent);

        String error = VariableConverter.roundingDouble2(this.channel.getAllowableError(), Locale.GERMAN);
        cell(17,16).setCellValue(error);

        String frequency = VariableConverter.roundingDouble(this.channel.getFrequency(), Locale.GERMAN);
        cell(26,40).setCellValue(frequency);
        cell(26,41).setCellValue(Strings.YEAR_WORD(this.channel.getFrequency()));

        String nextDate;
        if (this.result.goodChannel()){
            long l = (long) (31536000000L * this.channel.getFrequency());
            Calendar nextDateCal = new GregorianCalendar();
            nextDateCal.setTimeInMillis(this.checkDate.getTimeInMillis() + l);
            nextDate = VariableConverter.dateToString(nextDateCal);
        }else {
            nextDate = Strings.EXTRAORDINARY;
        }
        cell(35,36).setCellValue(nextDate);

        putSensorData();
        putResult();
    }

    @Override
    public void putSensorData() {
        Sensor sensor = this.channel.getSensor();

        String type = sensor.getType().getType();
        cell(19,11).setCellValue(type);

        double eP = (sensor.getError(this.channel) / (this.channel.getRange() / 100));
        String errorPercent = VariableConverter.roundingDouble2(eP, Locale.GERMAN);
        cell(20,12).setCellValue(errorPercent);

        String error = VariableConverter.roundingDouble2(sensor.getError(this.channel), Locale.GERMAN);
        cell(20,16).setCellValue(error);
    }

    @Override
    public void putCalibratorData() {
        Calibrator calibrator = (Calibrator) this.values.getValue(Value.CALIBRATOR);

        String name;
        if (calibrator.getName() == CalibratorType.FLUKE750PD2_small){
            name = CalibratorType.FLUKE750PD2.getType();
        }else {
            name = calibrator.getName().getType();
        }
        cell(16,39).setCellValue(name);

        String number = calibrator.getNumber();
        cell(17,27).setCellValue(number);

        String certificate = calibrator.getCertificate().getFullName();
        cell(18,30).setCellValue(certificate);

        String errorPercent = VariableConverter.roundingDouble2(this.result.getErrorCalibrator()[1], Locale.GERMAN);
        cell(19,31).setCellValue(errorPercent);

        String error;
        if (this.result.getErrorCalibrator()[0] < 0.01){
            error = VariableConverter.roundingDouble3(this.result.getErrorCalibrator()[0], Locale.GERMAN);
        }else {
            error = VariableConverter.roundingDouble2(this.result.getErrorCalibrator()[0], Locale.GERMAN);
        }
        cell(19,37).setCellValue(error);
    }

    @Override
    public void putResult() {
        double value5 = ((channel.getRange() / 100) * 5) + channel.getRangeMin();
        double value50 = ((channel.getRange() / 100) * 50) + channel.getRangeMin();
        double value95 = ((channel.getRange() / 100) * 95) + channel.getRangeMin();
        Calibrator calibrator = (Calibrator) this.values.getValue(Value.CALIBRATOR);

        if (calibrator.getName() == CalibratorType.FLUKE718_30G){
            double maxCalibratorPower = new PressureConverter(MeasurementConstants.KG_SM2, this.channel.getMeasurement().getValueConstant()).get(-0.8);
            if (value5 < maxCalibratorPower){
                value5 = maxCalibratorPower;
            }
        }

        cell(29, 5).setCellValue(VariableConverter.roundingDouble2(value5, Locale.GERMAN));
        cell(31, 5).setCellValue(VariableConverter.roundingDouble2(value50, Locale.GERMAN));
        cell(33, 5).setCellValue(VariableConverter.roundingDouble2(value95, Locale.GERMAN));

        double[][]measurementValues = this.measurementValues();
        int y = 8;
        for (double[] value : measurementValues) {
            for (int z = 0; z < 6; z++) {
                int n = 29 + z;
                cell(n, y).setCellValue(VariableConverter.roundingDouble3(value[z + 1], Locale.GERMAN));
            }
            if (y == 8) {
                y = 11;
            } else if (y == 11) {
                y = 13;
            } else if (y == 13) {
                y = 15;
            } else if (y == 15) {
                y = 18;
            }
        }

        String u;
        if (this.result.getExtendedIndeterminacy() < 0.01 && this.result.getExtendedIndeterminacy() > -0.01) {
            u = VariableConverter.roundingDouble3(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }else {
            u = VariableConverter.roundingDouble2(this.result.getExtendedIndeterminacy(), Locale.GERMAN);
        }
        cell(23,34).setCellValue(u);

        String errorReduced;
        if (this.result.getErrorInRange() < 0.01 && this.result.getErrorInRange() > -0.01) {
            errorReduced = VariableConverter.roundingDouble3(this.result.getErrorInRange(), Locale.GERMAN);
        }else {
            errorReduced = VariableConverter.roundingDouble2(this.result.getErrorInRange(), Locale.GERMAN);
        }

        String absoluteError;
        if (this.result.getAbsoluteErrorWithSensorError() < 0.01 && this.result.getAbsoluteErrorWithSensorError() > -0.01) {
            absoluteError = VariableConverter.roundingDouble3(this.result.getAbsoluteErrorWithSensorError(), Locale.GERMAN);
        }else {
            absoluteError = VariableConverter.roundingDouble2(this.result.getAbsoluteErrorWithSensorError(), Locale.GERMAN);
        }
        cell(25,34).setCellValue(errorReduced);
        cell(34,38).setCellValue(errorReduced);
        cell(26,34).setCellValue(absoluteError);

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
        cell(27,33).setCellValue(s5);
        cell(28,33).setCellValue(s50);
        cell(29,33).setCellValue(s95);

        if (!this.result.goodChannel()){
            cell(33,25).setCellValue("не придатним до експлуатації");
            cell(34,32).setCellValue("більше");
        }else {
            cell(33,25).setCellValue("придатним до експлуатації");
            cell(34,32).setCellValue("менше");
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
        cell(38,38).setCellValue(headOfArea);
        if (!this.result.goodChannel()){
            cell(28,59).setCellValue(headOfArea);
        }

        String headOfMetrologyArea = this.values.getStringValue(Value.HEAD_OF_METROLOGY_NAME);
        if (headOfMetrologyArea == null){
            headOfMetrologyArea = "________________";
        }
        cell(38,16).setCellValue(headOfMetrologyArea);
        cell(40,38).setCellValue(headOfMetrologyArea);
        if (!this.result.goodChannel()){
            cell(30,59).setCellValue(headOfMetrologyArea);
            cell(40,59).setCellValue(headOfMetrologyArea);
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
        cell(42,38).setCellValue(calculaterName);
        if (!this.result.goodChannel()){
            cell(32,59).setCellValue(calculaterName);
        }

        String calculaterPosition = this.values.getStringValue(Value.CALCULATER_POSITION);
        if (calculaterPosition == null){
            calculaterPosition = "________________";
        }
        cell(42,22).setCellValue(calculaterPosition);
        if (!this.result.goodChannel()){
            cell(32,45).setCellValue(calculaterPosition);
        }

        if (!this.result.goodChannel()){
            String headOfASUTPDepartment = this.values.getStringValue(Value.HEAD_OF_DEPARTMENT);
            if (headOfASUTPDepartment == null){
                headOfASUTPDepartment = "________________";
            }
            cell(26,59).setCellValue(headOfASUTPDepartment);

            cell(9,52).setCellValue(this.numberOfReference);

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
