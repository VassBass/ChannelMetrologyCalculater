package model;

import java.util.Locale;
import java.util.Objects;

/**
 * DB table = persons
 */
public class Person {
    public static final String JUNIOR_ENGINEER = "Мол.інженер з АСКВ";
    public static final String ELECTRONIC_ENGINEER = "Інженер-електронік";
    public static final String ENGINEER_ASKV = "Інженер з АСКВ";
    public static final String HEAD_OF_AREA = "Начальник дільниці";
    public static final String HEAD_OF_DEPARTMENT_ASUTP = "Начальник ЦАСУ ТП";

    /**
     * DB field = id (primary key)[INTEGER]
     */
    private int id = -1;

    /**
     * DB fields = surname, name, patronymic, position [TEXT]
     */
    private String surname, name, patronymic, position;

    public Person(){}
    public Person(int id){
        this.id = id;
    }

    public int getId(){return this.id;}
    public String getSurname(){return this.surname;}
    public String getName(){return this.name;}
    public String getPatronymic(){return this.patronymic;}
    public String getPosition(){return this.position;}

    /**
     * @return full name of person
     * format {@link #name} + in upper case {@link #surname}
     */
    public String getFullName(){
        return this.name + " " + this.surname.toUpperCase(Locale.ROOT);
    }

    public void setId(int id){this.id = id;}
    public void setSurname(String surname){this.surname = surname;}
    public void setName(String name){this.name = name;}
    public void setPatronymic(String patronymic){this.patronymic = patronymic;}
    public void setPosition(String position){this.position = position;}

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.surname, this.name, this.patronymic, this.position);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        Person person = (Person) obj;
        return person.getId() == this.id;
    }

    public boolean isMatch(Person person){
        return this.name.equals(person.getName())
                && this.surname.equals(person.getSurname())
                && this.patronymic.equals(person.patronymic)
                && this.position.equals(person.getPosition());
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getFullName(), getPosition());
    }
}