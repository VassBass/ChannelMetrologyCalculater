package service.control_points.info;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
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
    private static final String DELETE_QUESTION = "deleteQuestion";
    private static final String CHANGE_SUCCESS = "changeSuccess";
    private static final String SAVE_SUCCESS = "saveSuccess";
    private static final String CONTROL_POINTS_EXISTS = "controlPointsExists";

    private final Map<String, String> messages;
    Map<String, String> rootLabels;

    private final RepositoryFactory repositoryFactory;
    private final ControlPointsListManager parentManager;
    private final ControlPointsInfoContext context;
    private final ControlPoints oldCP;

    private SwingControlPointsInfoDialog dialog;

    public SwingControlPointsInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                         @Nonnull ControlPointsListManager parentManager,
                                         @Nonnull ControlPointsInfoContext context,
                                         @Nullable ControlPoints oldCP) {
        messages = Messages.getMessages(SwingControlPointsInfoManager.class);
        rootLabels = Labels.getRootLabels();

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
        Map<String, String> rootMessages = Messages.getRootMessages();

        if (Objects.nonNull(oldCP)) {
            String name = oldCP.getName();
            String message = String.format(messages.get(DELETE_QUESTION), name);
            int result = JOptionPane.showConfirmDialog(dialog, message, rootLabels.get(RootLabelName.DELETING), YES_NO_OPTION);
            if (result == 0) {
                ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);
                if (repository.removeByName(name)) {
                    message = rootMessages.get(RootMessageName.DELETING_SUCCESS);
                    JOptionPane.showMessageDialog(dialog, message, rootLabels.get(RootLabelName.SUCCESS), INFORMATION_MESSAGE);
                } else {
                    message = rootMessages.get(RootMessageName.ERROR_TRY_AGAIN);
                    JOptionPane.showMessageDialog(dialog, message, rootLabels.get(RootLabelName.ERROR), ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(dialog, messages.get(CHANGE_SUCCESS), rootLabels.get(RootLabelName.SUCCESS), INFORMATION_MESSAGE);
                dialog.shutdown();
                parentManager.updateControlPointsList();
            } else showExistMessage();
        } else {
            if (repository.add(newCp)) {
                JOptionPane.showMessageDialog(dialog, messages.get(SAVE_SUCCESS), rootLabels.get(RootLabelName.SUCCESS), INFORMATION_MESSAGE);
                dialog.shutdown();
                parentManager.updateControlPointsList();
            } else showExistMessage();
        }
    }

    @Override
    public void updateDialog() {
        if (Objects.nonNull(dialog)) dialog.refresh();
    }

    private void showExistMessage() {
        JOptionPane.showMessageDialog(dialog, messages.get(CONTROL_POINTS_EXISTS), rootLabels.get(RootLabelName.ERROR), ERROR_MESSAGE);
    }

    public void registerDialog(SwingControlPointsInfoDialog dialog) {
        this.dialog = dialog;
    }
}
