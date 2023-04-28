package service.person.list;

import service.person.list.ui.PersonListContext;
import service.person.list.ui.swing.SwingPersonListDialog;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SwingPersonListManager implements PersonListManager {

    private final PersonListContext context;
    private SwingPersonListDialog dialog;

    public SwingPersonListManager(@Nonnull PersonListContext context) {
        this.context = context;
    }

    @Override
    public void clickClose() {
        if (Objects.nonNull(dialog)) dialog.shutdown();
    }

    @Override
    public void clickChange() {
    }

    @Override
    public void clickAdd() {

    }

    public void registerDialog(SwingPersonListDialog dialog) {
        this.dialog = dialog;
    }
}
