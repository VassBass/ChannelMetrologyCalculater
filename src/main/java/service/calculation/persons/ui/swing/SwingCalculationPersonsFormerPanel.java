package service.calculation.persons.ui.swing;

import localization.Labels;
import model.dto.Person;
import model.ui.DefaultComboBox;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.calculation.persons.CalculationPersonValuesBuffer;
import service.calculation.persons.ui.CalculationPersonsFormerPanel;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalculationPersonsFormerPanel extends TitledPanel implements CalculationPersonsFormerPanel {
    private static final String FORMER = "former";

    private final List<Person> personList;

    private final DefaultComboBox formerName;
    private final DefaultTextField formerPosition;

    public SwingCalculationPersonsFormerPanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(Labels.getLabels(SwingCalculationPersonsFormerPanel.class).get(FORMER));
        CalculationPersonValuesBuffer buffer = CalculationPersonValuesBuffer.getInstance();

        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        personList = new ArrayList<>(personRepository.getAll());

        formerName = new DefaultComboBox(true);
        formerPosition = new DefaultTextField(10);

        List<String> formersNames = personList.stream().map(Person::createFullName).collect(Collectors.toList());
        formersNames.add(0, EMPTY);
        formerName.setList(formersNames);
        formerName.addItemListener(e -> {
            int index = formerName.getSelectedIndex() - 1;
            if (index >= 0 && index < personList.size()) {
                formerPosition.setText(personList.get(index).getPosition());
            }
        });
        if (Objects.nonNull(buffer.getFormerName())) formerName.setSelectedItem(buffer.getFormerName());
        if (Objects.nonNull(buffer.getFormerPosition())) formerPosition.setText(buffer.getFormerPosition());

        this.add(formerName, new CellBuilder().x(0).build());
        this.add(formerPosition, new CellBuilder().x(1).build());
    }

    @Override
    public Map.Entry<String, String> getFormer() {
        return new AbstractMap.SimpleEntry<>(formerName.getSelectedString(), formerPosition.getText());
    }
}
