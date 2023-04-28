package service.person.info.ui.swing;

import model.dto.Person;
import model.ui.TitledComboBox;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.person.info.ui.PersonInfoPositionPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwingPersonInfoPositionPanel extends TitledComboBox implements PersonInfoPositionPanel {
    private static final String TITLE_TEXT = "Посада";

    public SwingPersonInfoPositionPanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(true, TITLE_TEXT, Color.BLACK);
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        this.setList(personRepository.getAll().stream()
                .map(Person::getPosition)
                .filter(Objects::nonNull)
                .filter(StringHelper::nonEmpty)
                .collect(Collectors.toList()));
    }


    @Override
    public String getPosition() {
        String position = this.getSelectedString();
        if (position.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return position;
    }

    @Override
    public void setPosition(String position) {
        this.setSelectedItem(position);
    }
}
