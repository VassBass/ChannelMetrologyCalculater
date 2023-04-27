package service.sensor_types.list;

import repository.RepositoryFactory;
import service.sensor_types.info.SensorTypesInfoExecutor;
import service.sensor_types.list.ui.SensorTypesListContext;
import service.sensor_types.list.ui.SensorTypesListTable;
import service.sensor_types.list.ui.swing.SwingSensorTypesListDialog;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SwingSensorTypesListManager implements SensorTypesListManager {

    private final RepositoryFactory repositoryFactory;
    private final SensorTypesListContext context;
    private SwingSensorTypesListDialog dialog;

    public SwingSensorTypesListManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull SensorTypesListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    public void registerDialog(SwingSensorTypesListDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void changeMeasurement() {
        if (Objects.nonNull(dialog)) dialog.refresh();
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    @Override
    public void clickChange() {
        SensorTypesListTable table = context.getElement(SensorTypesListTable.class);
        String oldType = table.getSelectedType();
        if (Objects.nonNull(oldType)) {
            new SensorTypesInfoExecutor(dialog, repositoryFactory, oldType).execute();
        }
    }
}
