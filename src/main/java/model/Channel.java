package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.VariableConverter;
import repository.impl.MeasurementRepositorySQLite;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * DB table = channels
 */
public class Channel implements Serializable {

    public Channel(){}

    public Channel(@Nonnull String code){this.code = code;}

    /**
     * DB field = code (primary key)[TEXT]
     */
    @Nonnull private String code = "";

    /**
     * DB field = name [TEXT]
     */
    @Nonnull private String name = "";

    /**
     * DB field = measurement_value [TEXT]
     *
     * @see Measurement
     */
    @Nonnull private String measurementValue = "";

    /**
     * DB field department [TEXT]
     */
    private String department = "";

    /**
     * DB field = area [TEXT]
     */
    private String area = "";

    /**
     * DB field = process [TEXT]
     */
    private String process = "";

    /**
     * DB field = installation [TEXT]
     */
    private String installation = "";

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
    private String technologyNumber = "";

    /**
     * DB field = sensor [TEXT{Json}]
     *
     * @see Sensor
     */
    @Nonnull private Sensor sensor = new Sensor();

    /**
     * DB field = protocol_number [TEXT]
     */
    private String numberOfProtocol = "";

    /**
     * DB field = reference [TEXT]
     */
    private String reference = "";

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

    @Nonnull public String getCode() {return this.code;}
    @Nonnull public String getName() {return this.name;}

    @Nonnull
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Measurement _getMeasurement() {
        if (this.measurementValue.equals(Measurement.M_S) || this.measurementValue.equals(Measurement.CM_S)){
            return new Measurement(Measurement.CONSUMPTION, this.measurementValue);
        }else {
            return MeasurementRepositorySQLite.getInstance().getWithLoggerTurnOff(this.measurementValue).get();
        }
    }

    @Nonnull public String getMeasurementValue(){return this.measurementValue;}
    public String getDepartment() {return this.department;}
    public String getArea() {return this.area;}
    public String getProcess() {return this.process;}
    public String getInstallation() {return this.installation;}
    @Nonnull public String getDate() {return this.date;}
    public String getTechnologyNumber() {return this.technologyNumber;}
    @Nonnull public Sensor getSensor() {return this.sensor;}
    public String getNumberOfProtocol() {return this.numberOfProtocol;}
    public double getFrequency() {return this.frequency;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public String getReference(){return this.reference;}
    public double getAllowableErrorPercent(){return this.allowableErrorPercent;}
    public double getAllowableError(){return this.allowableError;}
    public boolean isSuitability(){return this.suitability;}

    /**
     * @return date of next channel control or null if {@link #suitability} = false
     *
     * @see #frequency
     * @see #suitability
     * @see #date
     */
    public Calendar _getNextDate() {
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
    public String _getFullPath() {
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
    public double _getRange(){
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
    public void setSensor(@Nonnull Sensor sensor) {this.sensor = sensor;}
    public void setNumberOfProtocol(String number) {this.numberOfProtocol = number;}
    public void setRangeMin(double rangeMin) {this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax) {this.rangeMax = rangeMax;}
    public void setReference(String reference){this.reference = reference;}
    public void setSuitability(boolean suitability){this.suitability = suitability;}


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

    /**
     * @return {@link Channel} in JsonString
     * if 10 times in a row throws {@link JsonProcessingException} return null
     *
     * @see com.fasterxml.jackson.core
     */
    @Override
    public String toString() {
        int attempt = 0;
        while (attempt < 10) {
            try {
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                return writer.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                attempt++;
            }
        }
        return null;
    }

    /**
     * @param json {@link Channel} in JsonString
     *
     * @see com.fasterxml.jackson.core
     *
     * @return {@link Channel}
     *
     * @throws JsonProcessingException - if jackson can't transform String to Channel
     */
    public static Channel fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Channel.class);
    }

    /**
     * @param channel to copy from
     *
     * @return copy of Channel in @param
     */
    public Channel copyFrom(Channel channel){
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
        c.setSensor(channel.getSensor());
        c.setNumberOfProtocol(channel.getNumberOfProtocol());
        c.setReference(channel.getReference());
        c.setRange(channel.getRangeMin(), channel.getRangeMax());
        c.setAllowableError(channel.getAllowableErrorPercent(), channel.getAllowableError());
        c.setSuitability(channel.isSuitability());
        return c;
    }

    /**
     * @param channel to compare with this
     *
     * @return true if channels fields equal
     */
    public boolean isMatch(Channel channel){
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
                && this.sensor.isMatch(channel.getSensor())
                && this.numberOfProtocol.equals(channel.getNumberOfProtocol())
                && this.reference.equals(channel.getReference())
                && this.rangeMin == channel.getRangeMin()
                && this.rangeMax == channel.getRangeMax()
                && this.allowableErrorPercent == channel.getAllowableErrorPercent()
                && this.allowableError == channel.getAllowableError()
                && this.suitability == channel.isSuitability();
    }
}
