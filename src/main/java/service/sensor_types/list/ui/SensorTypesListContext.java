package service.sensor_types.list.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.sensor_error.list.ui.SensorErrorListContext;
import service.sensor_types.list.SensorTypesListManager;
import service.sensor_types.list.ui.swing.SwingSensorTypesListButtonsPanel;
import service.sensor_types.list.ui.swing.SwingSensorTypesListMeasurementPanel;
import service.sensor_types.list.ui.swing.SwingSensorTypesListTable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SensorTypesListContext {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorListContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private SensorTypesListManager manager;

    public SensorTypesListContext(@Nonnull RepositoryFactory repositoryFactory) {
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
            if (clazz.isAssignableFrom(SensorTypesListMeasurementPanel.class) || clazz.isAssignableFrom(SwingSensorTypesListMeasurementPanel.class)) {
                element = (T) new SwingSensorTypesListMeasurementPanel(repositoryFactory, manager);
                buffer.put(SensorTypesListMeasurementPanel.class, element);
                buffer.put(SwingSensorTypesListMeasurementPanel.class, element);
            }
            if (clazz.isAssignableFrom(SensorTypesListTable.class) || clazz.isAssignableFrom(SwingSensorTypesListTable.class)) {
                element = (T) new SwingSensorTypesListTable();
                buffer.put(SensorTypesListTable.class, element);
                buffer.put(SwingSensorTypesListTable.class, element);
            }
            if (clazz.isAssignableFrom(SwingSensorTypesListButtonsPanel.class)) {
                element = (T) new SwingSensorTypesListButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(SensorTypesListManager manager) {
        this.manager = manager;
    }
}
