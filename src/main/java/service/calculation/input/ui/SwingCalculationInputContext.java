package service.calculation.input.ui;

import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.calculation.CalculationManager;
import service.calculation.input.CalculationInputManager;
import service.calculation.input.ui.swing.SwingCalculationInputAlarmPanel;
import service.calculation.input.ui.swing.SwingCalculationInputButtonsPanel;
import service.calculation.input.ui.swing.SwingCalculationInputMeasurementPanel;
import service.calculation.input.ui.swing.SwingCalculationInputNumberFormatPanel;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SwingCalculationInputContext {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationInputContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final RepositoryFactory repositoryFactory;
    private final Channel channel;

    private CalculationManager manager;
    private CalculationInputManager localManager;

    public SwingCalculationInputContext(@Nonnull RepositoryFactory repositoryFactory,
                                        @Nonnull Channel channel) {
        this.repositoryFactory = repositoryFactory;
        this.channel = channel;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(CalculationInputAlarmPanel.class) || clazz.isAssignableFrom(SwingCalculationInputAlarmPanel.class)) {
                element = (T) new SwingCalculationInputAlarmPanel(channel.getMeasurementValue());
                buffer.put(CalculationInputAlarmPanel.class, element);
                buffer.put(SwingCalculationInputAlarmPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalculationInputMeasurementPanel.class) || clazz.isAssignableFrom(SwingCalculationInputMeasurementPanel.class)) {
                element = (T) new SwingCalculationInputMeasurementPanel(repositoryFactory, channel);
                buffer.put(CalculationInputMeasurementPanel.class, element);
                buffer.put(SwingCalculationInputMeasurementPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingCalculationInputButtonsPanel.class)) {
                element = (T) new SwingCalculationInputButtonsPanel(manager);
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(CalculationInputNumberFormatPanel.class) || clazz.isAssignableFrom(SwingCalculationInputNumberFormatPanel.class)) {
                element = (T) new SwingCalculationInputNumberFormatPanel(localManager);
                buffer.put(CalculationInputNumberFormatPanel.class, element);
                buffer.put(SwingCalculationInputNumberFormatPanel.class, element);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(CalculationManager manager) {
        this.manager = manager;
    }
    public void registerManager(CalculationInputManager manager) {
        this.localManager = manager;
    }
}
