package service.calculation.persons.ui.swing;

import localization.Labels;
import model.dto.Person;
import model.ui.ButtonCell;
import model.ui.DefaultComboBox;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.calculation.persons.CalculationPersonValuesBuffer;
import service.calculation.protocol.Protocol;
import service.calculation.persons.ui.CalculationPersonsHeadsPanel;
import util.ObjectHelper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static model.ui.ButtonCell.SIMPLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalculationPersonsHeadsPanel extends TitledPanel implements CalculationPersonsHeadsPanel {
    private static final String HEADS = "heads";
    private static final String HEAD_OF_METROLOGY_DEPARTMENT = "headOfMetrologyDepartment";
    private static final String HEAD_OF_ASCP_DEPARTMENT = "headOfASCPDepartment";

    private static final Map<String, String> labels = Labels.getLabels(SwingCalculationPersonsHeadsPanel.class);

    private final DefaultComboBox headOfMetrologyDepartmentName;
    private final DefaultTextField headOfCheckedChannelDepartmentPosition;
    private final DefaultComboBox headOfCheckedChannelDepartmentName;
    private final DefaultComboBox headOfASPCDepartmentName;

    public SwingCalculationPersonsHeadsPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull Protocol protocol) {
        super(labels.get(HEADS));
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();
        CalculationPersonValuesBuffer buffer = CalculationPersonValuesBuffer.getInstance();

        List<Person> persons = new ArrayList<>(personRepository.getAll());
        List<String> personsName = persons.stream().map(Person::createFullName).collect(Collectors.toList());
        personsName.add(0, EMPTY);

        String headOfMetrologyDepartment = labels.get(HEAD_OF_METROLOGY_DEPARTMENT);
        ButtonCell headOfMetrologyDepartmentLabel = new ButtonCell(SIMPLE, headOfMetrologyDepartment);

        headOfMetrologyDepartmentName = new DefaultComboBox(true);
        headOfMetrologyDepartmentName.setList(personsName);
        if (Objects.isNull(buffer.getHeadOfMetrologyDepartment())) {
            int index = -1;
            for (int i = 0; i < persons.size(); i++) {
                if (persons.get(i).getPosition().equalsIgnoreCase(headOfMetrologyDepartment)) {
                    index = i;
                    break;
                }
            }
            headOfMetrologyDepartmentName.setSelectedIndex(++index);
        } else {
            headOfMetrologyDepartmentName.setSelectedItem(buffer.getHeadOfMetrologyDepartment());
        }

        headOfCheckedChannelDepartmentPosition = new DefaultTextField(10);
        if (Objects.nonNull(buffer.getHeadOfCheckedChannelDepartmentPosition())) {
            headOfCheckedChannelDepartmentPosition.setText(buffer.getHeadOfCheckedChannelDepartmentPosition());
        }

        headOfCheckedChannelDepartmentName = new DefaultComboBox(true);
        headOfCheckedChannelDepartmentName.setList(personsName);
        if (Objects.nonNull(buffer.getHeadOfCheckedChannelDepartmentName())) {
            headOfCheckedChannelDepartmentName.setSelectedItem(buffer.getHeadOfCheckedChannelDepartmentName());
        }
        headOfCheckedChannelDepartmentName.addItemListener(e -> {
            int index = headOfCheckedChannelDepartmentName.getSelectedIndex() - 1;
            if (index >= 0 && index < persons.size()) {
                headOfCheckedChannelDepartmentPosition.setText(persons.get(index).getPosition());
            }
        });


        ButtonCell headOfASPCDepartmentLabel;
        if (!suitable) {
            String headOfASCPDepartment = labels.get(HEAD_OF_ASCP_DEPARTMENT);
            headOfASPCDepartmentLabel = new ButtonCell(SIMPLE, headOfASCPDepartment);
            headOfASPCDepartmentName = new DefaultComboBox(true);
            headOfASPCDepartmentName.setList(personsName);
            if (Objects.isNull(buffer.getHeadOfASPCDepartment())) {
                int index = -1;
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getPosition().equalsIgnoreCase(headOfASCPDepartment)) {
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
        this.add(headOfCheckedChannelDepartmentPosition, new CellBuilder().x(0).y(1).build());
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
    public Map.Entry<String, String> getHeadOfCheckedChannelDepartment() {
        String name = headOfCheckedChannelDepartmentName.getSelectedString();
        String position = headOfCheckedChannelDepartmentPosition.getText();
        return new AbstractMap.SimpleEntry<>(name, position);
    }

    @Override
    public String getHeadOfASPCDepartment() {
        return Objects.nonNull(headOfASPCDepartmentName) ? headOfASPCDepartmentName.getSelectedString() : EMPTY;
    }
}
