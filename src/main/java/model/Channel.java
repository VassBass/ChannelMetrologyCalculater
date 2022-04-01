package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.VariableConverter;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

//DB table = channels
public class Channel implements Serializable {

    //DB field = code (primary key)[TEXT]
    private String code = "";

    //DB field = name [TEXT]
    private String name = "";

    //DB field = measurement [TEXT{Json}]
    private Measurement measurement = null;

    //DB field department [TEXT]
    private String department = "";

    //DB field = area [TEXT]
    private String area = "";

    //DB field = process [TEXT]
    private String process = "";

    //DB field = installation [TEXT]
    private String installation = "";

    //DB field = date [TEXT{DD.MM.YYYY}]
    private String date = VariableConverter.dateToString(Calendar.getInstance());

    //DB field = frequency [REAL]
    private double frequency = 0D;

    //DB field = technology_number [TEXT]
    private String technologyNumber = "";

    //DB field = sensor [TEXT{Json}]
    private Sensor sensor = null;

    //DB field = protocol_number [TEXT]
    private String numberOfProtocol = "";

    //DB field = reference [TEXT]
    private String reference = "";

    //DB field = range_min [REAL]
    private double rangeMin = 0D;

    //DB field = range_max [REAL]
    private double rangeMax = 0D;

    //DB field = allowable_error_percent [REAL]
    private double allowableErrorPercent = 0D;

    //DB field = allowable_error_value [REAL]
    private double allowableError = 0D;

    //DB field = suitability [TEXT]
    private boolean suitability = true;

    //Getters
    public String getCode() {return this.code;}
    public String getName() {return this.name;}
    public Measurement getMeasurement() {return this.measurement;}
    public String getDepartment() {return this.department;}
    public String getArea() {return this.area;}
    public String getProcess() {return this.process;}
    public String getInstallation() {return this.installation;}
    public String getDate() {return this.date;}
    public String getTechnologyNumber() {return this.technologyNumber;}
    public Sensor getSensor() {return this.sensor;}
    public String getNumberOfProtocol() {return this.numberOfProtocol;}
    public double getFrequency() {return this.frequency;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public String getReference(){return this.reference;}
    public double getAllowableErrorPercent(){return this.allowableErrorPercent;}
    public double getAllowableError(){return this.allowableError;}
    public boolean isSuitability(){return this.suitability;}

    public Calendar getNextDate() {
        if (this.suitability) {
            long l = (long) (31536000000L * frequency);
            Calendar nextDate = new GregorianCalendar();
            nextDate.setTimeInMillis(VariableConverter.stringToDate(this.date).getTimeInMillis() + l);
            return nextDate;
        }else{
            return null;
        }
    }

    public String getFullPath() {
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

    public double getRange(){
        return this.rangeMax - this.rangeMin;
    }

    //Setters
    public void setCode(String code) {this.code = code;}
    public void setName(String name) {this.name = name;}
    public void setMeasurement(Measurement measurement) {this.measurement = measurement;}
    public void setDepartment(String department) {this.department = department;}
    public void setArea(String area) {this.area = area;}
    public void setProcess(String process) {this.process = process;}
    public void setInstallation(String installation) {this.installation = installation;}
    public void setDate(String date) {this.date = date;}
    public void setFrequency(double frequency) { this.frequency = frequency;}
    public void setTechnologyNumber(String number) {this.technologyNumber = number;}
    public void setSensor(Sensor sensor) {this.sensor = sensor;}
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;

        Channel in = (Channel) obj;
        return in.getCode().equals(this.code);
    }

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

    public static Channel fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Channel.class);
    }

    public Channel copyFrom(Channel channel){
        this.code = channel.getCode();
        this.name = channel.getName();
        this.measurement = channel.getMeasurement();
        this.department = channel.getDepartment();
        this.area = channel.getArea();
        this.process = channel.getProcess();
        this.installation = channel.getInstallation();
        this.date = channel.getDate();
        this.frequency = channel.getFrequency();
        this.technologyNumber = channel.getTechnologyNumber();
        this.sensor = channel.getSensor();
        this.numberOfProtocol = channel.getNumberOfProtocol();
        this.reference = channel.getReference();
        this.rangeMin = channel.getRangeMin();
        this.rangeMax = channel.getRangeMax();
        this.allowableErrorPercent = channel.getAllowableErrorPercent();
        this.allowableError = channel.allowableError;
        this.suitability = channel.suitability;
        return this;
    }
}