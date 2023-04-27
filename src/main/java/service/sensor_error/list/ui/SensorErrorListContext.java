package service.sensor_error.list.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.sensor_error.list.SensorErrorListManager;
import service.sensor_error.list.ui.swing.SwingSensorErrorListButtonsPanel;
import service.sensor_error.list.ui.swing.SwingSensorErrorListMeasurementPanel;
import service.sensor_error.list.ui.swing.SwingSensorErrorListTable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SensorErrorListContext {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorListContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private SensorErrorListManager manager;

    public SensorErrorListContext(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
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
            if (clazz.isAssignableFrom(SensorErrorListMeasurementPanel.class) || clazz.isAssignableFrom(SwingSensorErrorListMeasurementPanel.class)) {
                element = (T) new SwingSensorErrorListMeasurementPanel(repositoryFactory, manager);
                buffer.put(SensorErrorListMeasurementPanel.class, element);
                buffer.put(SwingSensorErrorListMeasurementPanel.class, element);
            }
            if (clazz.isAssignableFrom(SensorErrorListTable.class) || clazz.isAssignableFrom(SwingSensorErrorListTable.class)) {
                element = (T) new SwingSensorErrorListTable(repositoryFactory);
                buffer.put(SensorErrorListTable.class, element);
                buffer.put(SwingSensorErrorListTable.class, element);
            }
            if (clazz.isAssignableFrom(SwingSensorErrorListButtonsPanel.class)) {
                element = (T) new SwingSensorErrorListButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(SensorErrorListManager manager) {
        this.manager = manager;
    }
}
