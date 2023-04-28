package service.person.list;

import model.dto.Person;
import repository.RepositoryFactory;
import service.person.info.PersonInfoExecutor;
import service.person.list.ui.PersonListContext;
import service.person.list.ui.PersonListTable;
import service.person.list.ui.swing.SwingPersonListDialog;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SwingPersonListManager implements PersonListManager {

    private final RepositoryFactory repositoryFactory;
    private final PersonListContext context;
    private SwingPersonListDialog dialog;

    public SwingPersonListManager(@Nonnull RepositoryFactory repositoryFactory,
                                  @Nonnull PersonListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    @Override
    public void clickClose() {
        if (Objects.nonNull(dialog)) dialog.shutdown();
    }

    @Override
    public void clickChange() {
        PersonListTable table = context.getElement(PersonListTable.class);
        Person selectedPerson = table.getSelectedPerson();
        if (Objects.nonNull(selectedPerson)) {
            new PersonInfoExecutor(dialog, repositoryFactory, selectedPerson).execute();
        }
    }

    @Override
    public void clickAdd() {
        new PersonInfoExecutor(dialog, repositoryFactory, null).execute();
    }

    public void registerDialog(SwingPersonListDialog dialog) {
        this.dialog = dialog;
    }
}
