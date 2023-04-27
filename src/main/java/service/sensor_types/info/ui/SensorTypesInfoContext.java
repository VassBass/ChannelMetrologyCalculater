package service.sensor_types.info.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.sensor_types.info.SensorTypesInfoManager;
import service.sensor_types.info.ui.swing.SwingSensorTypesInfoButtonsPanel;
import service.sensor_types.info.ui.swing.SwingSensorTypesInfoTypePanel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SensorTypesInfoContext {
    private static final Logger logger = LoggerFactory.getLogger(SensorTypesInfoContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final String oldType;
    private SensorTypesInfoManager manager;

    public SensorTypesInfoContext(String oldType) {
        this.oldType = oldType;
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
            if (clazz.isAssignableFrom(SensorTypesInfoTypePanel.class) || clazz.isAssignableFrom(SwingSensorTypesInfoTypePanel.class)) {
                element = (T) new SwingSensorTypesInfoTypePanel(oldType);
                buffer.put(SensorTypesInfoTypePanel.class, element);
                buffer.put(SwingSensorTypesInfoTypePanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingSensorTypesInfoButtonsPanel.class)) {
                element = (T) new SwingSensorTypesInfoButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(SensorTypesInfoManager manager) {
        this.manager = manager;
    }
}
