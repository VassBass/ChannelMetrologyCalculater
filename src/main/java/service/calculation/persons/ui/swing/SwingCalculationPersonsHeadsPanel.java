package service.calculation.persons.ui.swing;

import model.dto.Person;
import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.calculation.protocol.Protocol;
import service.calculation.persons.ui.CalculationPersonsHeadsPanel;
import util.ObjectHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.ui.ButtonCell.SIMPLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalculationPersonsHeadsPanel extends TitledPanel implements CalculationPersonsHeadsPanel {
    private static final String TITLE_TEXT = "Відповідальні особи";
    private static final String HEAD_OF_METROLOGY_DEPARTMENT_LABEL = "Начальник дільниці МЗтаП";
    private static final String HEAD_OF_CHECKED_CHANNEL_DEPARTMENT_LABEL_PREFIX = "Начальник дільниці АСУТП ";
    private static final String HEAD_OF_ASPC_DEPARTMENT_LABEL = "Начальник ЦАСУ ТП";

    private final DefaultComboBox headOfMetrologyDepartmentName;
    private final DefaultComboBox headofCheckedChannelDepartmentName;
    private final DefaultComboBox headofASPCDepartmentName;

    public SwingCalculationPersonsHeadsPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull Protocol protocol) {
        super(TITLE_TEXT);
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        String department = protocol.getChannel().getDepartment();
        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();

        List<Person> persons = new ArrayList<>(personRepository.getAll());
        List<String> personsName = persons.stream().map(Person::createFullName).collect(Collectors.toList());
        personsName.add(0, EMPTY);

        ButtonCell headofMetrologyDepartmentLabel = new ButtonCell(SIMPLE, HEAD_OF_METROLOGY_DEPARTMENT_LABEL);
        ButtonCell headofCheckedChannelDepartmentLabel = new ButtonCell(SIMPLE, HEAD_OF_CHECKED_CHANNEL_DEPARTMENT_LABEL_PREFIX + department);

        headOfMetrologyDepartmentName = new DefaultComboBox(true);
        headOfMetrologyDepartmentName.setList(personsName);
        int index = -1;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getPosition().equalsIgnoreCase(HEAD_OF_METROLOGY_DEPARTMENT_LABEL)) {
                index = i;
                break;
            }
        }
        headOfMetrologyDepartmentName.setSelectedIndex(++index);

        headofCheckedChannelDepartmentName = new DefaultComboBox(true);
        headofCheckedChannelDepartmentName.setList(personsName);
        index = -1;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getPosition().contains(HEAD_OF_CHECKED_CHANNEL_DEPARTMENT_LABEL_PREFIX)) {
                index = i;
                break;
            }
        }
        headofCheckedChannelDepartmentName.setSelectedIndex(++index);

        ButtonCell headofASPCDepartmentLabel;
        if (!suitable) {
            headofASPCDepartmentLabel = new ButtonCell(SIMPLE, HEAD_OF_ASPC_DEPARTMENT_LABEL);
            headofASPCDepartmentName = new DefaultComboBox(true);
            headofASPCDepartmentName.setList(personsName);
            index = -1;
            for (int i = 0; i < persons.size(); i++) {
                if (persons.get(i).getPosition().equalsIgnoreCase(HEAD_OF_ASPC_DEPARTMENT_LABEL)) {
                    index = i;
                    break;
                }
            }
            headofASPCDepartmentName.setSelectedIndex(++index);
        } else {
            headofASPCDepartmentLabel = null;
            headofASPCDepartmentName = null;
        }

        this.add(headofMetrologyDepartmentLabel, new CellBuilder().x(0).y(0).build());
        this.add(headOfMetrologyDepartmentName, new CellBuilder().x(1).y(0).build());
        this.add(headofCheckedChannelDepartmentLabel, new CellBuilder().x(0).y(1).build());
        this.add(headofCheckedChannelDepartmentName, new CellBuilder().x(1).y(1).build());
        if (!suitable && ObjectHelper.nonNull(headofASPCDepartmentLabel, headofASPCDepartmentName)) {
            this.add(headofASPCDepartmentLabel, new CellBuilder().x(0).y(2).build());
            this.add(headofASPCDepartmentName, new CellBuilder().x(1).y(2).build());
        }
    }

    @Override
    public String getHeadOfMetrologyDepartment() {
        return headOfMetrologyDepartmentName.getSelectedString();
    }

    @Override
    public String getHeadOfCheckedChannelDepartment() {
        return headofCheckedChannelDepartmentName.getSelectedString();
    }

    @Override
    public String getHeadOfASPCDepartment() {
        return Objects.nonNull(headofASPCDepartmentName) ? headofASPCDepartmentName.getSelectedString() : EMPTY;
    }
}
