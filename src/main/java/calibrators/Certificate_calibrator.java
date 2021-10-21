package calibrators;

import converters.VariableConverter;

import java.io.Serializable;
import java.util.Calendar;

public class Certificate_calibrator implements Serializable {
    
    private String name = "";
    private Calendar date = Calendar.getInstance();
    private String company = "";
    
    //Getters
    public String getName(){return this.name;}
    public Calendar getDate(){return this.date;}
    public String getCompany(){return this.company;}
    
    //Setters
    public void setName(String name){this.name = name;}
    public void setDate(Calendar date){this.date = date;}
    public void setCompany(String company){this.company = company;}

    public String getFullName(){
        return this.name
                + " від "
                + VariableConverter.dateToString(this.date)
                + "р "
                + company;
    }
}
