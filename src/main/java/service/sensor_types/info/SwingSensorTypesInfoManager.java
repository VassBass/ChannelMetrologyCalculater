package service.sensor_types.info;

import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import model.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_types.info.ui.SensorTypesInfoContext;
import service.sensor_types.info.ui.SensorTypesInfoTypePanel;
import service.sensor_types.info.ui.swing.SwingSensorTypesInfoDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SwingSensorTypesInfoManager implements SensorTypesInfoManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingSensorTypesInfoManager.class);

    private final RepositoryFactory repositoryFactory;
    private final UI parentDialog;
    private final SensorTypesInfoContext context;
    private SwingSensorTypesInfoDialog dialog;
    private final String oldType;

    public SwingSensorTypesInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull UI parentDialog,
                                       @Nonnull SensorTypesInfoContext context,
                                       @Nonnull String oldType) {
        this.repositoryFactory = repositoryFactory;
        this.parentDialog = parentDialog;
        this.context = context;
        this.oldType = oldType;
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
        private final DialogWrapper loadingDialog;

        private final String oldType, newType;

        public Worker(String oldType, String newType) {
            super();
            this.oldType = oldType;
            this.newType = newType;

            LoadingDialog lDialog = LoadingDialog.getInstance();
            loadingDialog = new DialogWrapper(dialog, lDialog, ScreenPoint.center(dialog, lDialog));
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
                    String message = "Тип ПВП успішно змінено";
                    JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
                    dialog.shutdown();
                    parentDialog.refresh();
                } else errorReaction();
            } catch (InterruptedException | ExecutionException e) {
                logger.warn("Exception was thrown", e);
                errorReaction();
            }
        }

        private void errorReaction() {
            String message = "Виникла помилка. Спробуйте ще";
            JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
