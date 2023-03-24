package service.calculation.collect.input.ui;

import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.calculation.CalculationManager;
import service.calculation.collect.input.ui.swing.SwingCalculationInputAlarmPanel;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SwingCalculationInputContext {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationInputContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final Channel channel;

    private CalculationManager manager;

    public SwingCalculationInputContext(@Nonnull Channel channel) {
        this.channel =channel;
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

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(CalculationManager manager) {
        this.manager = manager;
    }
}
