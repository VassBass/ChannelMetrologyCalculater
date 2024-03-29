package service.control_points.list;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.control_points.list.ui.ControlPointsListContext;
import service.control_points.list.ui.swing.SwingControlPointsListDialog;

import javax.annotation.Nonnull;

public class ControlPointsListExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsListExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public ControlPointsListExecutor(@Nonnull ApplicationScreen applicationScreen,
                                     @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        ControlPointsListConfigHolder configHolder = new PropertiesControlPointsListConfigHolder();
        ControlPointsListContext context = new ControlPointsListContext(repositoryFactory);
        SwingControlPointsListManager manager = new SwingControlPointsListManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingControlPointsListDialog dialog = new SwingControlPointsListDialog(applicationScreen, configHolder, context, manager);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
