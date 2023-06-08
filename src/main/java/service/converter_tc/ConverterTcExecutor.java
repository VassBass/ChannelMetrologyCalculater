package service.converter_tc;

import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceExecutor;
import service.converter_tc.converter.Converter;
import service.converter_tc.converter.Converter_GOST6651_2009;
import service.converter_tc.ui.ConverterTcContext;
import service.converter_tc.ui.swing.SwingConverterTcDialog;

import javax.annotation.Nonnull;

public class ConverterTcExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ConverterTcExecutor.class);

    private final ApplicationScreen applicationScreen;

    public ConverterTcExecutor(@Nonnull ApplicationScreen applicationScreen) {
        this.applicationScreen = applicationScreen;
    }

    @Override
    public void execute() {

        ConverterTcConfigHolder configHolder = new PropertiesConverterTcConfigHolder();
        ConverterTcContext context = new ConverterTcContext();
        Converter converter = new Converter_GOST6651_2009();
        SwingConverterTcManager manager = new SwingConverterTcManager(context, converter);
        context.registerManager(manager);
        SwingConverterTcDialog dialog = new SwingConverterTcDialog(applicationScreen, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info("Service is running");
    }
}
