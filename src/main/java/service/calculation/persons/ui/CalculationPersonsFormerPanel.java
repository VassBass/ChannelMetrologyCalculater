package service.calculation.persons.ui;

import model.dto.Person;

import java.util.Map;

public interface CalculationPersonsFormerPanel {
    /**
     * Mapping of Person. Key = Full name of person, Value = position of person
     * @return person mappings
     * @see Person
     * @see Person#createFullName()
     * @see Person#getPosition()
     */
    Map.Entry<String, String> getFormer();
}
