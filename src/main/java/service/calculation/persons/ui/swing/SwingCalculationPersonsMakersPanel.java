package service.calculation.persons.ui.swing;

import model.dto.Person;
import model.ui.DefaultComboBox;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.calculation.persons.ui.CalculationPersonsMakersPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalculationPersonsMakersPanel extends TitledPanel implements CalculationPersonsMakersPanel {
    private static final String TITLE_TEXT = "Виконувачі КМХ";

    private final List<Person> personList;

    private final DefaultComboBox firstPersonName;
    private final DefaultComboBox secondPersonName;
    private final DefaultTextField firstPersonPosition;
    private final DefaultTextField secondPersonPosition;

    public SwingCalculationPersonsMakersPanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(TITLE_TEXT, Color.BLACK);

        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        personList = new ArrayList<>(personRepository.getAll());
        List<String> personsNames = personList.stream().map(Person::createFullName).collect(Collectors.toList());
        personsNames.add(0, EMPTY);

        firstPersonName = new DefaultComboBox(true);
        firstPersonName.setList(personsNames);
        firstPersonPosition = new DefaultTextField(10);
        firstPersonName.addItemListener(e -> {
            int index = firstPersonName.getSelectedIndex() - 1;
            if (index >= 0 && index < personList.size()) {
                firstPersonPosition.setText(personList.get(index).getPosition());
            }
        });

        secondPersonName = new DefaultComboBox(true);
        secondPersonName.setList(personsNames);
        secondPersonPosition = new DefaultTextField(10);
        secondPersonName.addItemListener(e -> {
            int index = secondPersonName.getSelectedIndex() - 1;
            if (index >= 0 && index < personList.size()) {
                secondPersonPosition.setText(personList.get(index).getPosition());
            }
        });

        this.add(firstPersonName, new CellBuilder().x(0).y(0).build());
        this.add(firstPersonPosition, new CellBuilder().x(1).y(0).build());
        this.add(secondPersonName, new CellBuilder().x(0).y(1).build());
        this.add(secondPersonPosition, new CellBuilder().x(1).y(1).build());
    }

    @Override
    public List<Map.Entry<String, String>> getMakers() {
        List<Map.Entry<String, String>> result = new ArrayList<>();

        String firstPersonName = this.firstPersonName.getSelectedItem();
        String firstPersonPosition = this.firstPersonPosition.getText();
        if (StringHelper.nonEmpty(firstPersonName, firstPersonPosition)) {
            result.add(new AbstractMap.SimpleEntry<>(firstPersonName, firstPersonPosition));
        }

        String secondPersonName = this.secondPersonName.getSelectedItem();
        String secondPersonPosition = this.secondPersonPosition.getText();
        if (StringHelper.nonEmpty(secondPersonName, secondPersonPosition)) {
            result.add(new AbstractMap.SimpleEntry<>(secondPersonName, secondPersonPosition));
        }

        return result;
    }
}
