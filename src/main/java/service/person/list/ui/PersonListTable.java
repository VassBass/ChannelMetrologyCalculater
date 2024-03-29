package service.person.list.ui;

import model.dto.Person;

import javax.annotation.Nullable;

public interface PersonListTable {
    @Nullable Person getSelectedPerson();
    void updateContent();
}
