package service.calculation.persons.ui;

import model.dto.Person;

public interface CalculationPersonsHeadsPanel {
    /**
     * @return full name of head
     * @see Person#createFullName()
     */
    String getHeadOfMetrologyDepartment();
    String getHeadOfCheckedChannelDepartment();
    String getHeadOfASPCDepartment();
}
