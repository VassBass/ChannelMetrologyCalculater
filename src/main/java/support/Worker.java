package support;

import java.io.Serializable;

public class Worker implements Serializable {

    protected String surname = null;
    protected String name = null;
    protected String patronymic = null;
    protected String position = null;

    public String getSurname(){return this.surname;}
    public String getName(){return this.name;}
    public String getPatronymic(){return this.patronymic;}
    public String getPosition(){return this.position;}

    public String getFullName(){
        StringBuilder builder = new StringBuilder();
        char[]nameChars = name.toCharArray();
        builder.append(String.valueOf(nameChars[0]).toUpperCase());
        builder.append(".");
        char[]patronymicChars = patronymic.toCharArray();
        builder.append(String.valueOf(patronymicChars[0]).toUpperCase());
        builder.append(".");
        builder.append(surname);
        return builder.toString();
    }

    public void setSurname(String surname){this.surname = surname;}
    public void setName(String name){this.name = name;}
    public void setPatronymic(String patronymic){this.patronymic = patronymic;}
    public void setPosition(String position){this.position = position;}

    public boolean equalsPerson(Worker person){
        return this.name.equals(person.getName())
                && this.surname.equals(person.getSurname())
                && this.patronymic.equals(person.getPatronymic());
    }
}