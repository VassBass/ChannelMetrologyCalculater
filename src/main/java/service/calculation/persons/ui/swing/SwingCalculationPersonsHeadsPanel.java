package service.calculation.persons.ui.swing;

import model.dto.Person;
import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.calculation.persons.CalculationPersonValuesBuffer;
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
    private final DefaultComboBox headOfCheckedChannelDepartmentName;
    private final DefaultComboBox headOfASPCDepartmentName;

    public SwingCalculationPersonsHeadsPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull Protocol protocol) {
        super(TITLE_TEXT);
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        String department = protocol.getChannel().getDepartment();
        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();
        CalculationPersonValuesBuffer buffer = CalculationPersonValuesBuffer.getInstance();

        List<Person> persons = new ArrayList<>(personRepository.getAll());
        List<String> personsName = persons.stream().map(Person::createFullName).collect(Collectors.toList());
        personsName.add(0, EMPTY);

        ButtonCell headOfMetrologyDepartmentLabel = new ButtonCell(SIMPLE, HEAD_OF_METROLOGY_DEPARTMENT_LABEL);
        ButtonCell headOfCheckedChannelDepartmentLabel = new ButtonCell(SIMPLE, HEAD_OF_CHECKED_CHANNEL_DEPARTMENT_LABEL_PREFIX + department);

        headOfMetrologyDepartmentName = new DefaultComboBox(true);
        headOfMetrologyDepartmentName.setList(personsName);
        if (Objects.isNull(buffer.getHeadOfMetrologyDepartment())) {
            int index = -1;
            for (int i = 0; i < persons.size(); i++) {
                if (persons.get(i).getPosition().equalsIgnoreCase(HEAD_OF_METROLOGY_DEPARTMENT_LABEL)) {
                    index = i;
                    break;
                }
            }
            headOfMetrologyDepartmentName.setSelectedIndex(++index);
        } else {
            headOfMetrologyDepartmentName.setSelectedItem(buffer.getHeadOfMetrologyDepartment());
        }

        headOfCheckedChannelDepartmentName = new DefaultComboBox(true);
        headOfCheckedChannelDepartmentName.setList(personsName);
        if (Objects.isNull(buffer.getHeadOfCheckedChannelDepartment())) {
            int index = -1;
            for (int i = 0; i < persons.size(); i++) {
                if (persons.get(i).getPosition().contains(HEAD_OF_CHECKED_CHANNEL_DEPARTMENT_LABEL_PREFIX)) {
                    index = i;
                    break;
                }
            }
            headOfCheckedChannelDepartmentName.setSelectedIndex(++index);
        } else {
            headOfCheckedChannelDepartmentName.setSelectedItem(buffer.getHeadOfCheckedChannelDepartment());
        }

        ButtonCell headOfASPCDepartmentLabel;
        if (!suitable) {
            headOfASPCDepartmentLabel = new ButtonCell(SIMPLE, HEAD_OF_ASPC_DEPARTMENT_LABEL);
            headOfASPCDepartmentName = new DefaultComboBox(true);
            headOfASPCDepartmentName.setList(personsName);
            if (Objects.isNull(buffer.getHeadOfASPCDepartment())) {
                int index = -1;
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getPosition().equalsIgnoreCase(HEAD_OF_ASPC_DEPARTMENT_LABEL)) {
                        index = i;
                        break;
                    }
                }
                headOfASPCDepartmentName.setSelectedIndex(++index);
            } else {
                headOfASPCDepartmentName.setSelectedItem(buffer.getHeadOfASPCDepartment());
            }
        } else {
            headOfASPCDepartmentLabel = null;
            headOfASPCDepartmentName = null;
        }

        this.add(headOfMetrologyDepartmentLabel, new CellBuilder().x(0).y(0).build());
        this.add(headOfMetrologyDepartmentName, new CellBuilder().x(1).y(0).build());
        this.add(headOfCheckedChannelDepartmentLabel, new CellBuilder().x(0).y(1).build());
        this.add(headOfCheckedChannelDepartmentName, new CellBuilder().x(1).y(1).build());
        if (!suitable && ObjectHelper.nonNull(headOfASPCDepartmentLabel, headOfASPCDepartmentName)) {
            this.add(headOfASPCDepartmentLabel, new CellBuilder().x(0).y(2).build());
            this.add(headOfASPCDepartmentName, new CellBuilder().x(1).y(2).build());
        }
    }

    @Override
    public String getHeadOfMetrologyDepartment() {
        return headOfMetrologyDepartmentName.getSelectedString();
    }

    @Override
    public String getHeadOfCheckedChannelDepartment() {
        return headOfCheckedChannelDepartmentName.getSelectedString();
    }

    @Override
    public String getHeadOfASPCDepartment() {
        return Objects.nonNull(headOfASPCDepartmentName) ? headOfASPCDepartmentName.getSelectedString() : EMPTY;
    }
}
