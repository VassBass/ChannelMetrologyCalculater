package service.sensor_types.info;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.ui.DefaultDialog;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_types.info.ui.SensorTypesInfoContext;
import service.sensor_types.info.ui.SensorTypesInfoTypePanel;
import service.sensor_types.info.ui.swing.SwingSensorTypesInfoDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SwingSensorTypesInfoManager implements SensorTypesInfoManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingSensorTypesInfoManager.class);

    private static final String CHANGE_SUCCESS = "changeSuccess";

    private final RepositoryFactory repositoryFactory;
    private final DefaultDialog parentDialog;
    private final SensorTypesInfoContext context;
    private SwingSensorTypesInfoDialog dialog;
    private final String oldType;

    private final Map<String, String> labels;

    public SwingSensorTypesInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull DefaultDialog parentDialog,
                                       @Nonnull SensorTypesInfoContext context,
                                       @Nonnull String oldType) {
        this.repositoryFactory = repositoryFactory;
        this.parentDialog = parentDialog;
        this.context = context;
        this.oldType = oldType;
        labels = Labels.getRootLabels();
    }

    public void registerDialog(SwingSensorTypesInfoDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void clickCancel() {
        dialog.shutdown();
    }

    @Override
    public void clickRefresh() {
        SensorTypesInfoTypePanel typePanel = context.getElement(SensorTypesInfoTypePanel.class);
        typePanel.refreshType();
    }

    @Override
    public void clickSave() {
        SensorTypesInfoTypePanel typePanel = context.getElement(SensorTypesInfoTypePanel.class);
        String newType = typePanel.getType();
        if (newType.isEmpty()) {
            dialog.refresh();
        } else {
            new Worker(oldType, newType).execute();
        }
    }

    private class Worker extends SwingWorker<Boolean, Void> {
        private final LoadingDialog loadingDialog;

        private final String oldType, newType;

        public Worker(String oldType, String newType) {
            super();
            this.oldType = oldType;
            this.newType = newType;

            loadingDialog = new LoadingDialog(dialog);
            loadingDialog.showing();
        }

        @Override
        protected Boolean doInBackground() {
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
            ControlPointsRepository controlPointsRepository = repositoryFactory.getImplementation(ControlPointsRepository.class);
            SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);

            if (!sensorRepository.changeSensorType(oldType, newType)) return false;
            if (!controlPointsRepository.changeSensorType(oldType, newType)) return false;
            return sensorErrorRepository.changeSensorType(oldType, newType);
        }

        @Override
        protected void done() {
            loadingDialog.shutdown();
            try {
                if (get()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            Messages.getMessages(SwingSensorTypesInfoManager.class).get(CHANGE_SUCCESS),
                            labels.get(RootLabelName.SUCCESS),
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dialog.shutdown();
                    parentDialog.refresh();
                } else errorReaction();
            } catch (InterruptedException | ExecutionException e) {
                logger.warn(Messages.Log.EXCEPTION_THROWN, e);
                errorReaction();
            }
        }

        private void errorReaction() {
            JOptionPane.showMessageDialog(
                    dialog,
                    Messages.getRootMessages().get(RootMessageName.ERROR_TRY_AGAIN),
                    labels.get(RootLabelName.ERROR),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
