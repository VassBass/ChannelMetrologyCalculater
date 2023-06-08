package service.converter_tc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.converter_tc.ConverterTcManager;
import service.converter_tc.ui.swing.SwingButtonsPanel;
import service.converter_tc.ui.swing.SwingResultPanel;
import service.converter_tc.ui.swing.SwingTypePanel;
import service.converter_tc.ui.swing.SwingValuePanel;

import java.util.HashMap;
import java.util.Map;

public class ConverterTcContext {
    private static final Logger logger = LoggerFactory.getLogger(ConverterTcContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private ConverterTcManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(TypePanel.class) || clazz.isAssignableFrom(SwingTypePanel.class)) {
                element = (T) new SwingTypePanel();
                buffer.put(TypePanel.class, element);
                buffer.put(SwingTypePanel.class, element);
            }
            if (clazz.isAssignableFrom(ValuePanel.class) || clazz.isAssignableFrom(SwingValuePanel.class)) {
                element = (T) new SwingValuePanel(manager);
                buffer.put(ValuePanel.class, element);
                buffer.put(SwingValuePanel.class, element);
            }
            if (clazz.isAssignableFrom(ResultPanel.class) || clazz.isAssignableFrom(SwingResultPanel.class)) {
                element = (T) new SwingResultPanel();
                buffer.put(ResultPanel.class, element);
                buffer.put(SwingResultPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingButtonsPanel.class)) {
                element = (T) new SwingButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(ConverterTcManager manager) {
        this.manager = manager;
    }
}
