package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
     * DB fields = surname, name, patronymic, position [TEXT]
     */
    private String surname, name, patronymic, position;

    public String getSurname(){return this.surname;}
    public String getName(){return this.name;}
    public String getPatronymic(){return this.patronymic;}
    public String getPosition(){return this.position;}

    /**
     * @return full name of person
     * format {@link #name} + in upper case {@link #surname}
     */
    public String _getFullName(){
        return this.name + " " + this.surname.toUpperCase(Locale.ROOT);
    }

    public void setSurname(String surname){this.surname = surname;}
    public void setName(String name){this.name = name;}
    public void setPatronymic(String patronymic){this.patronymic = patronymic;}
    public void setPosition(String position){this.position = position;}

    @Override
    public int hashCode() {
        return Objects.hash(this.surname, this.name, this.patronymic, this.position);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        Person person = (Person) obj;
        return person.getName().equals(this.name)
                && person.getSurname().equals(this.surname)
                && person.getPatronymic().equals(this.patronymic)
                && person.getPosition().equals(this.position);
    }

    /**
     * @return {@link Person} in JsonString
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
     * @param json {@link Person} in JsonString
     *
     * @see com.fasterxml.jackson.core
     *
     * @return {@link Person}
     *
     * @throws JsonProcessingException - if jackson can't transform String to Person
     */
    public static Person fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Person.class);
    }
}