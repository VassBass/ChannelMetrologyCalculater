package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Locale;
import java.util.Objects;

public class Person {
    private int id;
    private String surname, name, patronymic, position;

    public int getId(){return this.id;}
    public String getSurname(){return this.surname;}
    public String getName(){return this.name;}
    public String getPatronymic(){return this.patronymic;}
    public String getPosition(){return this.position;}

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
        return this.id == person.getId();
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

    public static Person fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Person.class);
    }
}
