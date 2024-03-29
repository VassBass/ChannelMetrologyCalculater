package service.control_points.info;

import localization.Messages;
import model.dto.ControlPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.control_points.info.ui.ControlPointsInfoContext;
import service.control_points.info.ui.swing.SwingControlPointsInfoDialog;
import service.control_points.list.ControlPointsListManager;
import service.control_points.list.ui.swing.SwingControlPointsListDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ControlPointsInfoExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsInfoExecutor.class);

    private final RepositoryFactory repositoryFactory;
    private final SwingControlPointsListDialog parentDialog;
    private final ControlPointsListManager parentManager;
    private final ControlPoints oldCP;

    public ControlPointsInfoExecutor(@Nonnull RepositoryFactory repositoryFactory,
                                     @Nonnull SwingControlPointsListDialog parentDialog,
                                     @Nonnull ControlPointsListManager parentManager,
                                     @Nullable ControlPoints oldCP){
        this.repositoryFactory = repositoryFactory;
        this.parentDialog = parentDialog;
        this.parentManager = parentManager;
        this.oldCP = oldCP;
    }

    @Override
    public void execute() {
        ControlPointsInfoContext context = new ControlPointsInfoContext(repositoryFactory, oldCP);
        SwingControlPointsInfoManager manager = new SwingControlPointsInfoManager(repositoryFactory, parentManager, context, oldCP);
        context.registerManager(manager);
        ControlPointsInfoConfigHolder configHolder = new PropertiesControlPointsInfoConfigHolder();
        SwingControlPointsInfoDialog dialog = new SwingControlPointsInfoDialog(parentDialog, context, configHolder);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
