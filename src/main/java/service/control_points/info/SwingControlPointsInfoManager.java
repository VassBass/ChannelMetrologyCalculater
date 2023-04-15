package service.control_points.info;

import model.dto.ControlPoints;
import model.dto.builder.ControlPointsBuilder;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import service.control_points.info.ui.ControlPointsInfoContext;
import service.control_points.info.ui.ControlPointsInfoRangePanel;
import service.control_points.info.ui.ControlPointsInfoSensorTypePanel;
import service.control_points.info.ui.ControlPointsInfoValuesPanel;
import service.control_points.info.ui.swing.SwingControlPointsInfoDialog;
import service.control_points.list.ControlPointsListManager;
import util.DoubleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Map;
import java.util.Objects;

import static javax.swing.JOptionPane.*;

public class SwingControlPointsInfoManager implements ControlPointsInfoManager {
    private final RepositoryFactory repositoryFactory;
    private final ControlPointsListManager parentManager;
    private final ControlPointsInfoContext context;
    private final ControlPoints oldCP;

    private SwingControlPointsInfoDialog dialog;

    public SwingControlPointsInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                         @Nonnull ControlPointsListManager parentManager,
                                         @Nonnull ControlPointsInfoContext context,
                                         @Nullable ControlPoints oldCP) {
        this.repositoryFactory = repositoryFactory;
        this.parentManager = parentManager;
        this.context = context;
        this.oldCP = oldCP;
    }

    @Override
    public void closeDialog() {
        dialog.shutdown();
    }

    @Override
    public void removeControlPoints() {
        if (Objects.nonNull(oldCP)) {
            String name = oldCP.getName();
            String message = String.format("Ви впевнені що хочете видалити контрольні точки для \"%s\"?", name);
            int result = JOptionPane.showConfirmDialog(dialog, message, "Видалення", YES_NO_OPTION);
            if (result == 0) {
                ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
                if (repository.removeByName(name)) {
                    message = "Видалення пройшло успішно";
                    JOptionPane.showMessageDialog(dialog, message, "Успіх", INFORMATION_MESSAGE);
                } else {
                    message = "Виникла помилка. Спробуйте ще раз";
                    JOptionPane.showMessageDialog(dialog, message, "Помилка", ERROR_MESSAGE);
                }
                dialog.shutdown();
                parentManager.updateControlPointsList();
            }
        }
    }

    @Override
    public void saveControlPoints() {
        ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
        ControlPointsInfoSensorTypePanel sensorTypePanel = context.getElement(ControlPointsInfoSensorTypePanel.class);
        ControlPointsInfoRangePanel rangePanel = context.getElement(ControlPointsInfoRangePanel.class);
        ControlPointsInfoValuesPanel valuesPanel = context.getElement(ControlPointsInfoValuesPanel.class);

        String sensorType = sensorTypePanel.getSensorType();
        double rangeMin = rangePanel.getRangeMin();
        double rangeMax = rangePanel.getRangeMax();
        Map<Double, Double> values = valuesPanel.getValues();
        if (sensorType.isEmpty() ||
                Objects.isNull(values) || values.isEmpty() ||
                DoubleHelper.anyNaN(rangeMin, rangeMax)) {
            dialog.refresh();
            return;
        }

        String name = ControlPoints.createName(sensorType, rangeMin, rangeMax);

        ControlPoints newCp = new ControlPointsBuilder(name).setSensorType(sensorType).setPoints(values).build();
        if (Objects.nonNull(oldCP)) {
            if (repository.set(oldCP, newCp)) {
                String message = "Контрольні точки успішно змінені";
                JOptionPane.showMessageDialog(dialog, message, "Успіх", INFORMATION_MESSAGE);
                dialog.shutdown();
                parentManager.updateControlPointsList();
            } else showExistMessage();
        } else {
            if (repository.add(newCp)) {
                String message = "Контрольні точки успішно збережені";
                JOptionPane.showMessageDialog(dialog, message, "Успіх", INFORMATION_MESSAGE);
                dialog.shutdown();
                parentManager.updateControlPointsList();
            } else showExistMessage();
        }
    }

    @Override
    public void updateDialog() {
        dialog.refresh();
    }

    private void showExistMessage() {
        String message = "Контрольні точки для такого типу ПВП та в цьому діапазоні вже існують";
        JOptionPane.showMessageDialog(dialog, message, "Помилка", ERROR_MESSAGE);
    }

    public void registerDialog(SwingControlPointsInfoDialog dialog) {
        this.dialog = dialog;
    }
}
