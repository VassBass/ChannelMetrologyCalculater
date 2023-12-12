package service.person.info;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.dto.Person;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.person.info.ui.*;
import service.person.info.ui.swing.*;
import service.person.list.ui.swing.SwingPersonListDialog;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Map;
import java.util.Objects;

public class SwingPersonInfoManager implements PersonInfoManager {

    private final RepositoryFactory repositoryFactory;
    private final SwingPersonListDialog parentDialog;
    private final PersonInfoContext context;
    private final Person oldPerson;
    private SwingPersonInfoDialog dialog;

    private final Map<String, String> labels;
    private final Map<String, String> messages;

    public SwingPersonInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                  @Nonnull SwingPersonListDialog parentDialog,
                                  @Nonnull PersonInfoContext context,
                                  @Nullable Person oldPerson) {
        labels = Labels.getRootLabels();
        messages = Messages.getRootMessages();

        this.repositoryFactory = repositoryFactory;
        this.parentDialog = parentDialog;
        this.context = context;
        this.oldPerson = oldPerson;
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    @Override
    public void clickRefresh() {
        if (Objects.nonNull(oldPerson)) {
            SwingPersonInfoSurnamePanel surnamePanel = context.getElement(SwingPersonInfoSurnamePanel.class);
            SwingPersonInfoNamePanel namePanel = context.getElement(SwingPersonInfoNamePanel.class);
            SwingPersonInfoPatronymicPanel patronymicPanel = context.getElement(SwingPersonInfoPatronymicPanel.class);
            SwingPersonInfoPositionPanel positionPanel = context.getElement(SwingPersonInfoPositionPanel.class);

            surnamePanel.setSurname(oldPerson.getSurname());
            namePanel.setPersonName(oldPerson.getName());
            patronymicPanel.setPatronymic(oldPerson.getPatronymic());
            positionPanel.setPosition(oldPerson.getPosition());
        }
    }

    @Override
    public void clickSave() {
        PersonRepository personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        PersonInfoNamePanel namePanel = context.getElement(PersonInfoNamePanel.class);
        PersonInfoSurnamePanel surnamePanel = context.getElement(PersonInfoSurnamePanel.class);
        PersonInfoPatronymicPanel patronymicPanel = context.getElement(PersonInfoPatronymicPanel.class);
        PersonInfoPositionPanel positionPanel = context.getElement(PersonInfoPositionPanel.class);

        String name = namePanel.getPersonName();
        String surname = surnamePanel.getSurname();
        String patronymic = patronymicPanel.getPatronymic();
        String position = positionPanel.getPosition();

        if (StringHelper.nonEmpty(name, surname, patronymic, position)) {
            if (Objects.nonNull(oldPerson)) {
                oldPerson.setName(name);
                oldPerson.setSurname(surname);
                oldPerson.setPatronymic(patronymic);
                oldPerson.setPosition(position);

                if (personRepository.set(oldPerson)) successAction();
                else errorAction();
            } else {
                Person person = new Person();
                person.setName(name);
                person.setSurname(surname);
                person.setPatronymic(patronymic);
                person.setPosition(position);

                if (personRepository.add(person)) successAction();
                else errorAction();
            }
        } else {
            dialog.refresh();
        }
    }

    private void successAction() {
        JOptionPane.showMessageDialog(
                dialog,
                messages.get(RootMessageName.DATA_SAVE_SUCCESS),
                labels.get(RootLabelName.SUCCESS),
                JOptionPane.INFORMATION_MESSAGE
        );
        dialog.shutdown();
        parentDialog.refresh();
    }

    private void errorAction() {
        JOptionPane.showMessageDialog(
                dialog,
                messages.get(RootMessageName.ERROR_TRY_AGAIN),
                labels.get(RootLabelName.ERROR),
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void registerDialog(SwingPersonInfoDialog dialog) {
        this.dialog = dialog;
    }
}
