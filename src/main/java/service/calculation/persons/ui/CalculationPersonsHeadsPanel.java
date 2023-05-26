package service.calculation.persons.ui;

import model.dto.Person;

import java.util.Map;

public interface CalculationPersonsHeadsPanel {
    /**
     * @return full name of head
     * @see Person#createFullName()
     */
    String getHeadOfMetrologyDepartment();

    Map.Entry<String, String> getHeadOfCheckedChannelDepartment();
    String getHeadOfASPCDepartment();
}
