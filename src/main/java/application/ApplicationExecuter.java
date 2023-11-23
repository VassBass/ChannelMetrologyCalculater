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
    private static final String INIT_MAIN_SCREEN = "Ініціалізація головного екрану";
    private static final String INIT_REPOSITORY = "Ініціалізація репозіторіїв";
    private static final String EXEC_CHANNEL_LIST_SERVICE = "Запуск сервісу \"список вимірювальних каналів\"";
    private static final String INIT_MEASUREMENT_SERVICE = "Ініціалізація сервісу \"вимірювання\"";
    private static final String INIT_CALIBRATOR_SERVICE = "Ініціалізація сервісу \"калібратор\"";
    private static final String INIT_IMPORT_SERVICE = "Ініціалізація сервісу \"імпорт\"";
    private static final String INIT_SENSOR_ERROR_SERVICE = "Ініціалізація сервісу \"похибка ПВП\"";
    private static final String INIT_SENSORS_TYPES_SERVICE = "Ініціалізація сервісу \"тип ПВП\"";
    private static final String INIT_CONTROL_POINTS_SERVICE = "Ініціалізація сервісу \"контрольні точки\"";
    private static final String INIT_PERSON_SERVICE = "Ініціалізація сервісу \"працівники\"";
    private static final String INIT_CONVERTOR_TC_SERVICE = "Ініціалізація сервісу \"перетворювач величин ТО\"";
    private static final String INIT_CALCULATION_METHODS_SERVICE = "Ініціалізація сервісу \"метод розрахунку\"";
    private static final String INIT_CONVERTER_SERVICE = "Ініціалізація сервісу \"перетворювач вимірювальних величин\"";
    private static final String INIT_ARCHIVING_SERVICE = "Ініціалізація сервісу \"архівування протоколів\"";

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

        String message = INIT_MAIN_SCREEN;
        publish(message);
        applicationScreen = new ApplicationScreen(applicationConfigHolder);

        message = INIT_REPOSITORY;
        publish(message);
        RepositoryConfigHolder repositoryConfigHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector repositoryDBConnector = new SqliteRepositoryDBConnector(repositoryConfigHolder);
        RepositoryFactory repositoryFactory = new RepositoryFactory(repositoryConfigHolder, repositoryDBConnector);

        message = EXEC_CHANNEL_LIST_SERVICE;
        publish(message);
        new SwingChannelListExecuter(applicationScreen, repositoryFactory).execute();

        message = INIT_MEASUREMENT_SERVICE;
        publish(message);
        new MeasurementListInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_CALIBRATOR_SERVICE;
        publish(message);
        new CalibratorListInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_IMPORT_SERVICE;
        publish(message);
        new SwingImporterInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_SENSOR_ERROR_SERVICE;
        publish(message);
        new SensorErrorListInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_SENSORS_TYPES_SERVICE;
        publish(message);
        new SensorTypesInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_CONTROL_POINTS_SERVICE;
        publish(message);
        new ControlPointsListInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_PERSON_SERVICE;
        publish(message);
        new PersonListInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_CONVERTOR_TC_SERVICE;
        publish(message);
        new ConverterTcInitializer(applicationScreen).init();

        message = INIT_CALCULATION_METHODS_SERVICE;
        publish(message);
        new MethodNameInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_CONVERTER_SERVICE;
        publish(message);
        new ConverterInitializer(applicationScreen, repositoryFactory).init();

        message = INIT_ARCHIVING_SERVICE;
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
