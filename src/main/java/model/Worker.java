package model;

import java.io.Serializable;
import java.util.Locale;

public class Worker implements Serializable {

    private String surname = null;
    private String name = null;
    private String patronymic = null;
    private String position = null;

    public String getSurname(){return this.surname;}
    public String getName(){return this.name;}
    public String getPatronymic(){return this.patronymic;}
    public String getPosition(){return this.position;}

    public String getFullName(){
        return this.name + " " + this.surname.toUpperCase(Locale.ROOT);
    }

    public void setSurname(String surname){this.surname = surname;}
    public void setName(String name){this.name = name;}
    public void setPatronymic(String patronymic){this.patronymic = patronymic;}
    public void setPosition(String position){this.position = position;}
}
