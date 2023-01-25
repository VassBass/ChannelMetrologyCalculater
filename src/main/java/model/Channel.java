package model;

import converters.VariableConverter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.Serializable;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = channels
 */
public class Channel implements Serializable {

    public Channel(){}

    public Channel(@Nonnull String code){
        this.code = code;
    }

    /**
     * DB field = code (primary key)[TEXT]
     */
    @Nonnull private String code = EMPTY;

    /**
     * DB field = name [TEXT]
     */
    @Nonnull private String name = EMPTY;

    /**
     * Foreign key = measurement_value [TEXT]
     *
     * @see Measurement
     */
    private String measurementValue = EMPTY;

    /**
     * DB field department [TEXT]
     */
    private String department = EMPTY;

    /**
     * DB field = area [TEXT]
     */
    private String area = EMPTY;

    /**
     * DB field = process [TEXT]
     */
    private String process = EMPTY;

    /**
     * DB field = installation [TEXT]
     */
    private String installation = EMPTY;

    /**
     * DB field = date [TEXT]
     * String format - DD.MM.YYYY
     *
     * @see VariableConverter#dateToString(Calendar)
     * @see VariableConverter#stringToDate(String)
     * @see VariableConverter#stringToDateString(String)
     */
    @Nonnull private String date = VariableConverter.dateToString(Calendar.getInstance());

    /**
     * DB field = frequency [REAL]
     */
    private double frequency = 0D;

    /**
     * DB field = technology_number [TEXT]
     */
    private String technologyNumber = EMPTY;

    /**
     * DB field = sensor [TEXT]
     *
     * @see Sensor
     */
    private String sensorName = EMPTY;

    /**
     * DB field = protocol_number [TEXT]
     */
    private String numberOfProtocol = EMPTY;

    /**
     * DB field = reference [TEXT]
     */
    private String reference = EMPTY;

    /**
     * DB field = range_min [REAL]
     */
    private double rangeMin = 0D;

    /**
     * DB field = range_max [REAL]
     */
    private double rangeMax = 0D;

    /**
     * DB field = allowable_error_percent [REAL]
     */
    private double allowableErrorPercent = 0D;

    /**
     * DB field = allowable_error_value [REAL]
     */
    private double allowableError = 0D;

    /**
     * DB field = suitability [TEXT]
     */
    private boolean suitability = true;

    /**
     * DB field = control_points [TEXT{Json}]
     * key - percent value of point
     * value - value of point
     */
    private Map<Double, Double> controlPoints = new HashMap<>();

    @Nonnull public String getCode() {return code;}
    @Nonnull public String getName() {return name;}
    public String getMeasurementValue() {return measurementValue;}
    public String getDepartment() {return department;}
    public String getArea() {return area;}
    public String getProcess() {return process;}
    public String getInstallation() {return installation;}
    @Nonnull public String getDate() {return date;}
    public String getTechnologyNumber() {return technologyNumber;}
    public String getSensorName() {return sensorName;}
    public String getNumberOfProtocol() {return numberOfProtocol;}
    public double getFrequency() {return frequency;}
    public double getRangeMin() {return rangeMin;}
    public double getRangeMax() {return rangeMax;}
    public String getReference(){return reference;}
    public double getAllowableErrorPercent(){return allowableErrorPercent;}
    public double getAllowableError(){return allowableError;}
    public boolean isSuitability(){return suitability;}
    public Map<Double, Double> getControlPoints(){
        return controlPoints;
    }

    /**
     * @return date of next channel control or null if {@link #suitability} = false
     *
     * @see #frequency
     * @see #suitability
     * @see #date
     */
    public Calendar calculateNextDate() {
        if (this.suitability) {
            long l = (long) (31536000000L * frequency);
            Calendar nextDate = new GregorianCalendar();
            nextDate.setTimeInMillis(VariableConverter.stringToDate(this.date).getTimeInMillis() + l);
            return nextDate;
        }else{
            return null;
        }
    }

    /**
     * @return string of full channel path
     * format {@link #department}/{@link #area}/{@link #process}/{@link #installation}
     */
    public String createFullPath() {
        StringBuilder builder = new StringBuilder();
        if (this.department!=null) {builder.append(this.department);}
        if (this.area!=null) {
            if (this.department!=null) {builder.append(" ").append(File.separator).append(" ");}
            builder.append(this.area);
        }
        if (this.process!=null) {
            if (this.department!=null||this.area!=null) {builder.append(" ").append(File.separator).append(" ");}
            builder.append(this.process);
        }
        if (this.installation!=null) {
            if (this.department!=null||this.area!=null||this.process!=null) {builder.append(" ").append(File.separator).append(" ");}
            builder.append(this.installation);
        }
        return builder.toString();
    }

    /**
     * @return full channel range - {@link #rangeMax} minus {@link #rangeMin}
     */
    public double calculateRange(){
        return this.rangeMax - this.rangeMin;
    }

    public void setCode(@Nonnull String code) {this.code = code;}
    public void setName(@Nonnull String name) {this.name = name;}
    public void setMeasurementValue(@Nonnull String measurementValue) {this.measurementValue = measurementValue;}
    public void setDepartment(String department) {this.department = department;}
    public void setArea(String area) {this.area = area;}
    public void setProcess(String process) {this.process = process;}
    public void setInstallation(String installation) {this.installation = installation;}
    public void setDate(@Nonnull String date) {this.date = date;}
    public void setFrequency(double frequency) { this.frequency = frequency;}
    public void setTechnologyNumber(String number) {this.technologyNumber = number;}
    public void setSensorName(String sensorName) {this.sensorName = sensorName;}
    public void setNumberOfProtocol(String number) {this.numberOfProtocol = number;}
    public void setRangeMin(double rangeMin) {this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax) {this.rangeMax = rangeMax;}
    public void setReference(String reference){this.reference = reference;}
    public void setSuitability(boolean suitability){this.suitability = suitability;}
    public void setControlPoints(Map<Double, Double> controlPoints){this.controlPoints = controlPoints;}

    public void setAllowableError(double percent, double value){
        this.allowableErrorPercent = percent;
        this.allowableError = value;
    }

    public void setRange(double min, double max){
        this.rangeMin = min;
        this.rangeMax = max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.name, this.technologyNumber);
    }

    /**
     *
     * This method has been rewritten to work with ArrayList.
     *
     * @return true if channels codes is equal
     *
     * If you need to compare all fields of Channels use {@link #isMatch(Channel)}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;

        Channel in = (Channel) obj;
        return in.getCode().equals(this.code);
    }

    @Override
    public String toString() {
        return String.format("(%s)%s[%s]{%s}", code, name, technologyNumber, createFullPath());
    }

    /**
     * @param channel to copy from
     *
     * @return copy of Channel in @param
     */
    public Channel copyFrom(@Nonnull Channel channel){
        Channel c = new Channel();
        c.setCode(channel.getCode());
        c.setName(channel.getName());
        c.setMeasurementValue(channel.getMeasurementValue());
        c.setDepartment(channel.getDepartment());
        c.setArea(channel.getArea());
        c.setProcess(channel.getProcess());
        c.setInstallation(channel.getInstallation());
        c.setDate(channel.getDate());
        c.setFrequency(channel.getFrequency());
        c.setTechnologyNumber(channel.getTechnologyNumber());
        c.setSensorName(channel.getSensorName());
        c.setNumberOfProtocol(channel.getNumberOfProtocol());
        c.setReference(channel.getReference());
        c.setRange(channel.getRangeMin(), channel.getRangeMax());
        c.setAllowableError(channel.getAllowableErrorPercent(), channel.getAllowableError());
        c.setSuitability(channel.isSuitability());
        c.setControlPoints(channel.getControlPoints());
        return c;
    }

    /**
     * @param channel to compare with this
     *
     * @return true if channels fields equal
     */
    public boolean isMatch(Channel channel){
        if (channel == null) return false;
        if (this == channel) return true;

        return this.code.equals(channel.getCode())
                && this.name.equals(channel.getName())
                && this.measurementValue.equals(channel.getMeasurementValue())
                && this.department.equals(channel.getDepartment())
                && this.area.equals(channel.getArea())
                && this.process.equals(channel.getProcess())
                && this.installation.equals(channel.getInstallation())
                && this.date.equals(channel.getDate())
                && this.frequency == channel.getFrequency()
                && this.technologyNumber.equals(channel.getTechnologyNumber())
                && this.sensorName.equals(channel.getSensorName())
                && this.numberOfProtocol.equals(channel.getNumberOfProtocol())
                && this.reference.equals(channel.getReference())
                && this.rangeMin == channel.getRangeMin()
                && this.rangeMax == channel.getRangeMax()
                && this.allowableErrorPercent == channel.getAllowableErrorPercent()
                && this.allowableError == channel.getAllowableError()
                && this.suitability == channel.isSuitability()
                && this.controlPoints.equals(channel.getControlPoints());
    }
}
