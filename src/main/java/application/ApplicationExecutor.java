package application;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
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

public class ApplicationExecutor extends SwingWorker<Void, String> {
    private static final String INIT_MAIN_SCREEN = "initMainScreen";
    private static final String INIT_REPOSITORY = "initRepository";
    private static final String EXEC_CHANNEL_LIST_SERVICE = "execChannelListService";
    private static final String INIT_MEASUREMENT_SERVICE = "initMeasurementService";
    private static final String INIT_CALIBRATOR_SERVICE = "initCalibratorService";
    private static final String INIT_IMPORT_SERVICE = "initImportService";
    private static final String INIT_SENSOR_ERROR_SERVICE = "initSensorErrorService";
    private static final String INIT_SENSORS_TYPES_SERVICE = "initSensorsTypesService";
    private static final String INIT_CONTROL_POINTS_SERVICE = "initControlPointsService";
    private static final String INIT_PERSON_SERVICE = "initPersonsService";
    private static final String INIT_CONVERTOR_TC_SERVICE = "initConvertorTcService";
    private static final String INIT_CALCULATION_METHODS_SERVICE = "initCalculationMethodsService";
    private static final String INIT_CONVERTER_SERVICE = "initConverterService";
    private static final String INIT_ARCHIVING_SERVICE = "initArchivingService";
    private static final String INIT_ERROR = "initError";

    private final Map<String, String> labels;
    private final Map<String, String> messages;

    private final ApplicationLogo logo;
    private final ApplicationConfigHolder applicationConfigHolder;
    private ApplicationScreen applicationScreen;

    public ApplicationExecutor() {
        labels = Labels.getRootLabels();
        messages = Messages.getMessages(ApplicationExecutor.class);
        applicationConfigHolder = new PropertiesApplicationConfigHolder();
        logo = new ApplicationLogo(applicationConfigHolder);
        logo.showing();
    }

    @Override
    protected Void doInBackground() throws Exception {
        new ApplicationInitializer().init();

        String message = messages.get(INIT_MAIN_SCREEN);
        publish(message);
        applicationScreen = new ApplicationScreen(applicationConfigHolder);

        message = messages.get(INIT_REPOSITORY);
        publish(message);
        RepositoryConfigHolder repositoryConfigHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector repositoryDBConnector = new SqliteRepositoryDBConnector(repositoryConfigHolder);
        RepositoryFactory repositoryFactory = new RepositoryFactory(repositoryConfigHolder, repositoryDBConnector);

        message = messages.get(EXEC_CHANNEL_LIST_SERVICE);
        publish(message);
        new SwingChannelListExecuter(applicationScreen, repositoryFactory).execute();

        message = messages.get(INIT_MEASUREMENT_SERVICE);
        publish(message);
        new MeasurementListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_CALIBRATOR_SERVICE);
        publish(message);
        new CalibratorListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_IMPORT_SERVICE);
        publish(message);
        new SwingImporterInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_SENSOR_ERROR_SERVICE);
        publish(message);
        new SensorErrorListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_SENSORS_TYPES_SERVICE);
        publish(message);
        new SensorTypesInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_CONTROL_POINTS_SERVICE);
        publish(message);
        new ControlPointsListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_PERSON_SERVICE);
        publish(message);
        new PersonListInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_CONVERTOR_TC_SERVICE);
        publish(message);
        new ConverterTcInitializer(applicationScreen).init();

        message = messages.get(INIT_CALCULATION_METHODS_SERVICE);
        publish(message);
        new MethodNameInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_CONVERTER_SERVICE);
        publish(message);
        new ConverterInitializer(applicationScreen, repositoryFactory).init();

        message = messages.get(INIT_ARCHIVING_SERVICE);
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
            JOptionPane.showMessageDialog(logo, messages.get(INIT_ERROR), labels.get(RootLabelName.ERROR), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } else {
            applicationScreen.showing();
        }
    }
}
