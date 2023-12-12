package service.converter_tc;

import application.ApplicationMenu;
import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Map;

public class ConverterTcInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConverterTcInitializer.class);

    private final ApplicationScreen applicationScreen;

    public ConverterTcInitializer(@Nonnull ApplicationScreen applicationScreen) {
        this.applicationScreen = applicationScreen;
    }

    @Override
    public void init() {
        Map<String, String> labels = Labels.getRootLabels();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.get(RootLabelName.TOOLS));

        JMenuItem btnConverterTc = new JMenuItem(Labels.getRootLabels().get(RootLabelName.CONVERTER_TC));

        btnConverterTc.addActionListener(e ->
                new ConverterTcExecutor(applicationScreen).execute());
        applicationMenu.addMenuItem(labels.get(RootLabelName.TOOLS), btnConverterTc);

        logger.info((Messages.Log.INIT_SUCCESS));
    }
}
