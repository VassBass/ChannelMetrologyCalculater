package service.measurement.converter.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.measurement.converter.ConverterManager;
import service.measurement.converter.ui.swing.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ConverterContext {
    private static final Logger logger = LoggerFactory.getLogger(ConverterContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final RepositoryFactory repositoryFactory;
    private ConverterManager manager;

    public ConverterContext(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            logger.warn(Messages.Log.MISSING_UI_MANAGER_ERROR);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(MeasurementNamePanel.class) || clazz.isAssignableFrom(SwingMeasurementNamePanel.class)) {
                element = (T) new SwingMeasurementNamePanel(repositoryFactory, manager);
                buffer.put(MeasurementNamePanel.class, element);
                buffer.put(SwingMeasurementNamePanel.class, element);
            }
            if (clazz.isAssignableFrom(ResultPanel.class) || clazz.isAssignableFrom(SwingResultPanel.class)) {
                element = (T) new SwingResultPanel();
                buffer.put(ResultPanel.class, element);
                buffer.put(SwingResultPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingSourceMeasurementValuePanel.class)) {
                element = (T) new SwingSourceMeasurementValuePanel();
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(SwingResultMeasurementValuePanel.class)) {
                element = (T) new SwingResultMeasurementValuePanel();
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(SwingButtonsPanel.class)) {
                element = (T) new SwingButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(ConverterManager manager) {
        this.manager = manager;
    }
}
