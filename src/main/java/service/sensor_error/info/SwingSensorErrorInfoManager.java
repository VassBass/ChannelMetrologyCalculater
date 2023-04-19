package service.sensor_error.info;

import model.dto.SensorError;
import repository.RepositoryFactory;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_error.info.ui.SensorErrorInfoContext;
import service.sensor_error.info.ui.SensorErrorInfoErrorPanel;
import service.sensor_error.info.ui.SensorErrorInfoSensorPanel;
import service.sensor_error.info.ui.swing.SwingSensorErrorInfoDialog;
import service.sensor_error.list.SensorErrorListManager;
import util.DoubleHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Objects;

public class SwingSensorErrorInfoManager implements SensorErrorInfoManager {
    private SwingSensorErrorInfoDialog dialog;

    private final RepositoryFactory repositoryFactory;
    private final SensorErrorListManager parentManager;
    private final SensorErrorInfoContext context;
    private final SensorError oldError;

    public SwingSensorErrorInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull SensorErrorListManager parentManager,
                                       @Nonnull SensorErrorInfoContext context,
                                       @Nullable SensorError oldError) {
        this.repositoryFactory = repositoryFactory;
        this.parentManager = parentManager;
        this.context = context;
        this.oldError = oldError;
    }

    public void registerDialog(@Nonnull SwingSensorErrorInfoDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    @Override
    public void clickRemove() {
        if (Objects.nonNull(oldError)) {
            String id = oldError.getId();
            String message = String.format("Ви впевнені що хочете видалити похибку ПВП для \"%s\"?", oldError);
            int result = JOptionPane.showConfirmDialog(dialog, message, "Видалити", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                SensorErrorRepository repository = repositoryFactory.getImplementation(SensorErrorRepository.class);
                if (repository.removeById(id)) {
                    message = "Видалення пройшло успішно";
                    JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    message = "При видаленні виникла помилка";
                    JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
                }
                dialog.shutdown();
                parentManager.refreshDialog();
            }
        }
    }

    @Override
    public void clickSave() {
        SensorErrorInfoSensorPanel sensorPanel = context.getElement(SensorErrorInfoSensorPanel.class);
        SensorErrorInfoErrorPanel errorPanel = context.getElement(SensorErrorInfoErrorPanel.class);

        String type = sensorPanel.getSensorType();
        double rangeMin = sensorPanel.getRangeMin();
        double rangeMax = sensorPanel.getRangeMax();
        String measurementValue = sensorPanel.getMeasurementValue();
        String errorFormula = errorPanel.getErrorFormula();

        if (StringHelper.nonEmpty(type, measurementValue, errorFormula) && DoubleHelper.nonNaN(rangeMin, rangeMax)) {
            SensorErrorRepository repository = repositoryFactory.getImplementation(SensorErrorRepository.class);
            SensorError newError = SensorError.create(type, rangeMin, rangeMax, measurementValue, errorFormula);
            if (Objects.isNull(oldError)) {
                if (repository.isExists(newError.getId())) {
                    String message = "Похибка для такого типу ПВП у данному діапазоні вже існує у базі";
                    JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
                } else if (repository.add(newError)) {
                    String message = "Похибка успішно додана";
                    JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
                    dialog.shutdown();
                    parentManager.refreshDialog();
                } else {
                    String message = "Виникла помилка. Спробуйте ще раз";
                    JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
                    dialog.refresh();
                }
            } else {
                if (repository.isExists(oldError.getId(), newError.getId())) {
                    String message = "Похибка для такого типу ПВП у данному діапазоні вже існує у базі";
                    JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
                } else if (repository.set(oldError.getId(), newError)) {
                    String message = "Похибка успішно змінена";
                    JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
                    dialog.shutdown();
                    parentManager.refreshDialog();
                } else {
                    String message = "Виникла помилка. Спробуйте ще раз";
                    JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
                    dialog.refresh();
                }
            }
        } else {
            dialog.refresh();
        }
    }
}
