package service.person.info.ui.swing;

import localization.Labels;
import model.dto.Person;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.person.info.PersonInfoConfigHolder;
import service.person.info.ui.PersonInfoContext;
import service.person.list.ui.swing.SwingPersonListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class SwingPersonInfoDialog extends DefaultDialog {
    private static final String NEW_EMPLOYEE = "newEmployee";
    private static final String EMPLOYEE_WITH_ID = "employeeWithId";

    private static final Map<String, String> labels = Labels.getLabels(SwingPersonInfoDialog.class);

    public SwingPersonInfoDialog(@Nonnull SwingPersonListDialog parentDialog,
                                 @Nonnull PersonInfoConfigHolder configHolder,
                                 @Nonnull PersonInfoContext context,
                                 @Nullable Person oldPerson) {
        super(parentDialog,
                Objects.isNull(oldPerson) ?
                        labels.get(NEW_EMPLOYEE) :
                        String.format(labels.get(EMPLOYEE_WITH_ID), oldPerson.getId()));

        SwingPersonInfoSurnamePanel surnamePanel = context.getElement(SwingPersonInfoSurnamePanel.class);
        SwingPersonInfoNamePanel namePanel = context.getElement(SwingPersonInfoNamePanel.class);
        SwingPersonInfoPatronymicPanel patronymicPanel = context.getElement(SwingPersonInfoPatronymicPanel.class);
        SwingPersonInfoPositionPanel positionPanel = context.getElement(SwingPersonInfoPositionPanel.class);
        SwingPersonInfoButtonsPanel buttonsPanel = context.getElement(SwingPersonInfoButtonsPanel.class);

        if (Objects.nonNull(oldPerson)) {
            surnamePanel.setSurname(oldPerson.getSurname());
            namePanel.setPersonName(oldPerson.getName());
            patronymicPanel.setPatronymic(oldPerson.getPatronymic());
            positionPanel.setPosition(oldPerson.getPosition());
        }

        DefaultPanel panel = new DefaultPanel();
        panel.add(surnamePanel, new CellBuilder().y(0).build());
        panel.add(namePanel, new CellBuilder().y(1).build());
        panel.add(patronymicPanel, new CellBuilder().y(2).build());
        panel.add(positionPanel, new CellBuilder().y(3).build());
        panel.add(buttonsPanel, new CellBuilder().y(4).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parentDialog, this));
        this.setContentPane(panel);
    }
}
