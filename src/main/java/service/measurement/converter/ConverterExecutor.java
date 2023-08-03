package service.measurement.converter;

import application.ApplicationScreen;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.measurement.converter.service.Converter;
import service.measurement.converter.service.DefaultConverter;
import service.measurement.converter.ui.ConverterContext;
import service.measurement.converter.ui.swing.ConverterDialog;

import javax.annotation.Nonnull;

public class ConverterExecutor implements ServiceExecutor {

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public ConverterExecutor(@Nonnull ApplicationScreen applicationScreen,
                             @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        ConverterConfigHolder configHolder = new PropertiesConverterConfigHolder();
        ConverterContext context = new ConverterContext(repositoryFactory);
        Converter converter = new DefaultConverter(repositoryFactory);
        SwingConverterManager manager = new SwingConverterManager(repositoryFactory, context, converter);
        context.registerManager(manager);
        ConverterDialog dialog = new ConverterDialog(applicationScreen, configHolder, context);
        manager.registerDialog(dialog);

        manager.updateMeasurementValues();
    }
}
