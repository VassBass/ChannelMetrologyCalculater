package support;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import converters.VariableConverter;
import measurements.Measurement;

public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code = "";
    private String name = "";
    private Measurement measurement = null;
    private String department = "";
    private String area = "";
    private String process = "";
    private String installation = "";
    private Calendar date = Calendar.getInstance();
    private double frequency = 0D;
    private String numberT = "";
    private Sensor sensor = null;
    private String numberP = "";
    private String reference = "";
    private double rangeMin = 0D;
    private double rangeMax = 0D;
    private double allowableErrorPercent = 0D;
    private double allowableError = 0D;
    public boolean isGood = true;

    //Getters
    public String getCode() {return this.code;}
    public String getName() {return this.name;}
    public Measurement getMeasurement() {return this.measurement;}
    public String getDepartment() {return this.department;}
    public String getArea() {return this.area;}
    public String getProcess() {return this.process;}
    public String getInstallation() {return this.installation;}
    public Calendar getDate() {return this.date;}
    public String getTechnologyNumber() {return this.numberT;}
    public Sensor getSensor() {return this.sensor;}
    public String getNumberOfProtocol() {return this.numberP;}
    public double getFrequency() {return this.frequency;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public String getReference(){return this.reference;}
    public double getAllowableErrorPercent(){return this.allowableErrorPercent;}
    public double getAllowableError(){return this.allowableError;}

    public Calendar getNextDate() {
        if (this.isGood) {
            long l = (long) (31536000000L * frequency);
            Calendar nextDate = new GregorianCalendar();
            nextDate.setTimeInMillis(this.date.getTimeInMillis() + l);
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
    public void setDate(Calendar date) {this.date = date;}
    public void setFrequency(double frequency) { this.frequency = frequency;}
    public void setTechnologyNumber(String number) {this.numberT = number;}
    public void setSensor(Sensor sensor) {this.sensor = sensor;}
    public void setNumberOfProtocol(String number) {this.numberP = number;}
    public void setRangeMin(double rangeMin) {this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax) {this.rangeMax = rangeMax;}
    public void setReference(String reference){this.reference = reference;}

    public void setAllowableError(double percent, double value){
        this.allowableErrorPercent = percent;
        this.allowableError = value;
    }

    public void setRange(double min, double max){
        this.rangeMin = min;
        this.rangeMax = max;
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
        this.numberT = channel.getTechnologyNumber();
        this.sensor = channel.getSensor();
        this.numberP = channel.getNumberOfProtocol();
        this.reference = channel.getReference();
        this.rangeMin = channel.getRangeMin();
        this.rangeMax = channel.getRangeMax();
        this.allowableErrorPercent = channel.getAllowableErrorPercent();
        this.allowableError = channel.allowableError;
        this.isGood = channel.isGood;
        return this;
    }
}