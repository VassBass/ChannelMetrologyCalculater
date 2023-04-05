package service.calculation.protocol;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Person;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Protocol implements Serializable {
    private static final long serialVersionUID = 6L;

    private final Channel channel;

    private String date;
    private String number;
    private Calibrator calibrator;
    private int temperature;
    private int humidity;
    private int pressure;
    private double alarm = Double.NaN;
    private TreeMap<Double, Double> input;
    private TreeMap<Double, double[]> output;
    private double extendedIndeterminacy;
    private double absoluteError;
    private double relativeError;
    private TreeMap<Double, Double> systematicErrors;
    private String conclusion;
    private int valuesDecimalPoint = 2;
    private int percentsDecimalPoint = 2;
    private String referenceNumber;
    private List<Map.Entry<String, String>> makers;
    private Map.Entry<String, String> former;
    private String headOfMetrologyDepartment;
    private String headOfCheckedChannelDepartment;
    private String headOfASPCDepartment;

    /**
     * @return list of mappings of makers where key = full name, value position
     * @see Person
     * @see Person#createFullName()
     */
    public List<Map.Entry<String, String>> getMakers() {
        return makers;
    }

    public void setMakers(List<Map.Entry<String, String>> makers) {
        this.makers = makers;
    }

    /**
     * @return list of mappings of makers where key = full name, value position
     * @see Person
     * @see Person#createFullName()
     */
    public Map.Entry<String, String> getFormer() {
        return former;
    }

    public void setFormer(Map.Entry<String, String> former) {
        this.former = former;
    }

    public String getHeadOfMetrologyDepartment() {
        return headOfMetrologyDepartment;
    }

    public void setHeadOfMetrologyDepartment(String headOfMetrologyDepartment) {
        this.headOfMetrologyDepartment = headOfMetrologyDepartment;
    }

    public String getHeadOfCheckedChannelDepartment() {
        return headOfCheckedChannelDepartment;
    }

    public void setHeadOfCheckedChannelDepartment(String headOfCheckedChannelDepartment) {
        this.headOfCheckedChannelDepartment = headOfCheckedChannelDepartment;
    }

    public String getHeadOfASPCDepartment() {
        return headOfASPCDepartment;
    }

    public void setHeadOfASPCDepartment(String headOfASPCDepartment) {
        this.headOfASPCDepartment = headOfASPCDepartment;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getValuesDecimalPoint() {
        return valuesDecimalPoint;
    }

    public void setValuesDecimalPoint(int valuesDecimalPoint) {
        this.valuesDecimalPoint = valuesDecimalPoint;
    }

    public int getPercentsDecimalPoint() {
        return percentsDecimalPoint;
    }

    public void setPercentsDecimalPoint(int percentsDecimalPoint) {
        this.percentsDecimalPoint = percentsDecimalPoint;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public double getExtendedIndeterminacy() {
        return extendedIndeterminacy;
    }

    public void setExtendedIndeterminacy(double extendedIndeterminacy) {
        this.extendedIndeterminacy = extendedIndeterminacy;
    }

    public double getAbsoluteError() {
        return absoluteError;
    }

    public void setAbsoluteError(double absoluteError) {
        this.absoluteError = absoluteError;
    }

    public double getRelativeError() {
        return relativeError;
    }

    public void setRelativeError(double relativeError) {
        this.relativeError = relativeError;
    }

    public TreeMap<Double, Double> getSystematicErrors() {
        return systematicErrors;
    }

    public void setSystematicErrors(TreeMap<Double, Double> systematicErrors) {
        this.systematicErrors = systematicErrors;
    }

    public Protocol(@Nonnull Channel channel) {
        this.channel = channel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Calibrator getCalibrator() {
        return calibrator;
    }

    public void setCalibrator(Calibrator calibrator) {
        this.calibrator = calibrator;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public Channel getChannel() {
        return channel;
    }

    public double getAlarm() {
        return alarm;
    }

    public void setAlarm(double alarm) {
        this.alarm = alarm;
    }

    public TreeMap<Double, Double> getInput() {
        return input;
    }

    public void setInput(TreeMap<Double, Double> input) {
        this.input = input;
    }

    public TreeMap<Double, double[]> getOutput() {
        return output;
    }

    public void setOutput(TreeMap<Double, double[]> output) {
        this.output = output;
    }
}
