package application;

import model.ui.ApplicationLogo;
import repository.RepositoryFactory;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;
import service.calibrator.list.CalibratorListInitializer;
import service.channel.list.SwingChannelListExecuter;
import service.control_points.list.ControlPointsListInitializer;
import service.importer.SwingImporterInitializer;
import service.sensor_error.list.SensorErrorListInitializer;
import service.sensor_types.list.SensorTypesInitializer;

import javax.swing.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ApplicationExecuter extends SwingWorker<Void, String> {
    private final ApplicationLogo logo;
    private final ApplicationConfigHolder applicationConfigHolder;
    private ApplicationScreen applicationScreen;

    public ApplicationExecuter() {
        applicationConfigHolder = new PropertiesApplicationConfigHolder();
        logo = new ApplicationLogo(applicationConfigHolder);
        logo.showing();
    }

    @Override
    protected Void doInBackground() {
        new ApplicationInitializer().init();

        String message = "Initialization of main screen";
        publish(message);
        applicationScreen = new ApplicationScreen(applicationConfigHolder);

        message = "Initialization of repository";
        publish(message);
        RepositoryConfigHolder repositoryConfigHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector repositoryDBConnector = new SqliteRepositoryDBConnector(repositoryConfigHolder);
        RepositoryFactory repositoryFactory = new RepositoryFactory(repositoryConfigHolder, repositoryDBConnector);

        message = "Execution of channel list service";
        publish(message);
        new SwingChannelListExecuter(applicationScreen, repositoryFactory).execute();

        message = "Initialization of calibrator service";
        publish(message);
        new CalibratorListInitializer(applicationScreen, repositoryFactory).init();

        message = "Initialization of import service";
        publish(message);
        new SwingImporterInitializer(applicationScreen, repositoryFactory).init();

        message = "Initialization of sensor error service";
        publish(message);
        new SensorErrorListInitializer(applicationScreen, repositoryFactory).init();

        message = "Initialization of sensor types service";
        publish(message);
        new SensorTypesInitializer(applicationScreen, repositoryFactory).init();

        message = "Initialization of control points service";
        publish(message);
        new ControlPointsListInitializer(applicationScreen, repositoryFactory).init();

        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        String message = chunks.get(chunks.size() - 1);
        logo.setMessage(message == null ? EMPTY : message);
    }

    @Override
    protected void done() {
        logo.shutdown();
        if (applicationScreen == null) {
            String message = "Виникла помилка при ініціалізації. Спробуйте ще." +
                    "\nЯкщо помилка не зникне перевстановіть програму або зверніться до розробника." +
                    "\nvassbassapp@gmail.com";
            JOptionPane.showMessageDialog(logo, message, "ERROR!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } else {
            applicationScreen.showing();
        }
    }
}
