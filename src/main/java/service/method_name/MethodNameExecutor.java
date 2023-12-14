package service.method_name;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.method_name.ui.MethodNameContext;
import service.method_name.ui.swing.SwingMethodNameDialog;

import javax.annotation.Nonnull;

public class MethodNameExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MethodNameExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public MethodNameExecutor(@Nonnull ApplicationScreen applicationScreen,
                              @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        MethodNameConfigHolder configHolder = new PropertiesMethodNameConfigHolder();
        MethodNameContext context = new MethodNameContext(repositoryFactory);
        SwingMethodNameManager manager = new SwingMethodNameManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingMethodNameDialog dialog = new SwingMethodNameDialog(applicationScreen, configHolder, context);
        manager.registerDialog(dialog);

        manager.changeMeasurementName();
        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
