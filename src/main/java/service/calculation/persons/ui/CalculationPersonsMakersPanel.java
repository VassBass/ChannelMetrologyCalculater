package service.calculation.persons.ui;

import model.dto.Person;

import java.util.List;
import java.util.Map;

public interface CalculationPersonsMakersPanel {
    /**
     * Mapping of Person. Key = Full name of person, Value = position of person
     * @return list of persons mappings
     * @see Person
     * @see Person#createFullName()
     * @see Person#getPosition()
     */
    List<Map.Entry<String, String>> getMakers();
}
