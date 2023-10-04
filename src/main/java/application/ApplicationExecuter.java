package application;

import localization.label.Labels;
import localization.message.Messages;
import model.ui.ApplicationLogo;
import repository.RepositoryFactory;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;
import service.calibrator.list.CalibratorListInitializer;
import service.certificate.archive.CertificateArchiveExecutor;
import service.channel.list.SwingChannelListExecuter;
import service.control_points.list.ControlPointsListInitializer;
import service.converter_tc.ConverterTcInitializer;
import service.importer.SwingImporterInitializer;
import service.measurement.converter.ConverterInitializer;
import service.measurement.list.MeasurementListInitializer;
import service.method_name.MethodNameInitializer;
import service.person.list.PersonListInitializer;
import service.sensor_error.list.SensorErrorListInitializer;
import service.sensor_types.list.SensorTypesInitializer;

import javax.swing.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ApplicationExecuter extends SwingWorker<Void, String> {
    private final Labels labels;
    private final Messages messages;

    private final ApplicationLogo logo;
    private final ApplicationConfigHolder applicationConfigHolder;
    private ApplicationScreen applicationScreen;

    public ApplicationExecuter() {
        labels = Labels.getInstance();
        messages = Messages.getInstance();

        applicationConfigHolder = new PropertiesApplicationConfigHolder();
        logo = new ApplicationLogo(applicationConfigHolder);
        logo.showing();
    }

    @Override
    protected Void doInBackground() throws Exception {
        new ApplicationInitializer().init();

        String message = messages.init_mainScreen;
        publish(message);
        applicationScreen = new ApplicationScreen(applicationConfigHolder);

        message = messages.init_repository;
        publish(message);
        RepositoryConfigHolder repositoryConfigHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector repositoryDBConnector = new SqliteRepositoryDBConnector(repositoryConfigHolder);
        RepositoryFactory repositoryFactory = new RepositoryFactory(repositoryConfigHolder, repositoryDBConnector);

        message = messages.exec_channelListService;
        publish(message);
        new SwingChannelListExecuter(applicationScreen, repositoryFactory).execute();

        message = messages.init_measurementService;
        publish(message);
        new MeasurementListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_calibratorService;
        publish(message);
        new CalibratorListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_importService;
        publish(message);
        new SwingImporterInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_sensorErrorService;
        publish(message);
        new SensorErrorListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_sensorsTypesService;
        publish(message);
        new SensorTypesInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_controlPointsService;
        publish(message);
        new ControlPointsListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_personService;
        publish(message);
        new PersonListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_convertorTCService;
        publish(message);
        new ConverterTcInitializer(applicationScreen).init();

        message = messages.init_calculationMethodsService;
        publish(message);
        new MethodNameInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_converterService;
        publish(message);
        new ConverterInitializer(applicationScreen, repositoryFactory).init();

        message = messages.init_archivingService;
        publish(message);
        new CertificateArchiveExecutor().execute();

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
            JOptionPane.showMessageDialog(logo, messages.init_error, labels.error, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } else {
            applicationScreen.showing();
        }
    }
}
