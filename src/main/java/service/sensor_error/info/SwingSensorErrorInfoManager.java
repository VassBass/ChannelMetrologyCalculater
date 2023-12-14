package service.sensor_error.info;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
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
import java.util.Map;
import java.util.Objects;

public class SwingSensorErrorInfoManager implements SensorErrorInfoManager {
    private static final String DELETE_QUESTION = "deleteQuestion";
    private static final String ERROR_EXISTS = "errorExists";
    private static final String ERROR_CHANGED = "errorChanged";

    private SwingSensorErrorInfoDialog dialog;

    private final RepositoryFactory repositoryFactory;
    private final SensorErrorListManager parentManager;
    private final SensorErrorInfoContext context;
    private final SensorError oldError;

    private final Map<String, String> messages;
    private final Map<String, String> rootMessages;
    private final Map<String, String> labels;

    public SwingSensorErrorInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull SensorErrorListManager parentManager,
                                       @Nonnull SensorErrorInfoContext context,
                                       @Nullable SensorError oldError) {
        this.repositoryFactory = repositoryFactory;
        this.parentManager = parentManager;
        this.context = context;
        this.oldError = oldError;
        messages = Messages.getMessages(SwingSensorErrorInfoManager.class);
        rootMessages = Messages.getRootMessages();
        labels = Labels.getRootLabels();
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
            String message = String.format(messages.get(DELETE_QUESTION), oldError);
            int result = JOptionPane.showConfirmDialog(dialog, message, labels.get(RootLabelName.DELETE), JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                SensorErrorRepository repository = repositoryFactory.getImplementation(SensorErrorRepository.class);
                if (repository.removeById(id)) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            rootMessages.get(RootMessageName.DELETING_SUCCESS),
                            labels.get(RootLabelName.SUCCESS),
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {
                    JOptionPane.showMessageDialog(dialog,
                            rootMessages.get(RootMessageName.ERROR_TRY_AGAIN),
                            labels.get(RootLabelName.ERROR), JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(
                            dialog,
                            messages.get(ERROR_EXISTS),
                            labels.get(RootLabelName.ERROR),
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (repository.add(newError)) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            rootMessages.get(RootMessageName.DATA_SAVE_SUCCESS),
                            labels.get(RootLabelName.SUCCESS),
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dialog.shutdown();
                    parentManager.refreshDialog();
                } else {
                    JOptionPane.showMessageDialog(
                            dialog,
                            rootMessages.get(RootMessageName.ERROR_TRY_AGAIN),
                            labels.get(RootLabelName.ERROR),
                            JOptionPane.ERROR_MESSAGE
                    );
                    dialog.refresh();
                }
            } else {
                if (repository.isExists(oldError.getId(), newError.getId())) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            messages.get(ERROR_EXISTS),
                            labels.get(RootLabelName.ERROR),
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (repository.set(oldError.getId(), newError)) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            messages.get(ERROR_CHANGED),
                            labels.get(RootLabelName.SUCCESS),
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dialog.shutdown();
                    parentManager.refreshDialog();
                } else {
                    JOptionPane.showMessageDialog(
                            dialog,
                            rootMessages.get(RootMessageName.ERROR_TRY_AGAIN),
                            labels.get(RootLabelName.ERROR),
                            JOptionPane.ERROR_MESSAGE
                    );
                    dialog.refresh();
                }
            }
        } else {
            dialog.refresh();
        }
    }
}
