package service.calculation.persons.ui;

import model.dto.Person;

import javax.annotation.Nullable;

public interface CalculationPersonsHeadsPanel {
    @Nullable Person getHeadOfMetrologyDepartment();
    @Nullable Person getHeadOfCheckedChannelDepartment();
    @Nullable Person getHeadOfASPCDepartment();
}
