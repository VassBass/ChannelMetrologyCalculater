package service.control_points.list;

import model.dto.ControlPoints;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import service.control_points.info.ControlPointsInfoExecuter;
import service.control_points.list.ui.ControlPointsListContext;
import service.control_points.list.ui.ControlPointsListSortPanel;
import service.control_points.list.ui.ControlPointsListTable;
import service.control_points.list.ui.swing.SwingControlPointsListDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SwingControlPointsListManager implements ControlPointsListManager {
    private final RepositoryFactory repositoryFactory;
    private final ControlPointsListContext context;

    private SwingControlPointsListDialog dialog;

    public SwingControlPointsListManager(@Nonnull RepositoryFactory repositoryFactory,
                                         @Nonnull ControlPointsListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    @Override
    public void showAllControlPointsInTable() {
        ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
        ControlPointsListTable table = context.getElement(ControlPointsListTable.class);
        table.setControlPointsList(new ArrayList<>(repository.getAll()));
    }

    @Override
    public void showSortedControlPointsInTable() {
        ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
        ControlPointsListTable table = context.getElement(ControlPointsListTable.class);
        ControlPointsListSortPanel sortPanel = context.getElement(ControlPointsListSortPanel.class);
        String sensorType = sortPanel.getSelectedSensorType();
        if (sensorType.isEmpty()) {
            table.setControlPointsList(Collections.emptyList());
        } else {
            table.setControlPointsList(new ArrayList<>(repository.getAllBySensorType(sensorType)));
        }
    }

    @Override
    public void shutdownService() {
        dialog.shutdown();
    }

    @Override
    public void removeControlPoints() {
        ControlPointsListTable table = context.getElement(ControlPointsListTable.class);
        String selectedCP = table.getSelectedControlPointsName();
        if (Objects.nonNull(selectedCP)) {
            String message = String.format("Ви впевнені що хочете видалити контрольні точки для \"%s\"?", selectedCP);
            int result = JOptionPane.showConfirmDialog(dialog, message, "Видалення", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
                if (repository.removeByName(selectedCP)) {
                    JOptionPane.showMessageDialog(dialog, "Операція завершена успішно", "Успіх", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Виникла помилка. Спробуйте ще раз", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
                dialog.refresh();
            }
        }
    }

    @Override
    public void showControlPointsDetails() {
        ControlPointsListTable table = context.getElement(ControlPointsListTable.class);
        String selectedCP = table.getSelectedControlPointsName();
        if (Objects.nonNull(selectedCP)) {
            ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
            ControlPoints cp = repository.get(selectedCP);
            if (Objects.nonNull(cp)) new ControlPointsInfoExecuter(repositoryFactory, dialog, this, cp).execute();
        }
    }

    @Override
    public void addControlPoints() {
        new ControlPointsInfoExecuter(repositoryFactory, dialog, this, null).execute();
    }

    @Override
    public void updateControlPointsList() {
        dialog.refresh();
    }

    public void registerDialog(@Nonnull SwingControlPointsListDialog dialog) {
        this.dialog = dialog;
    }
}
