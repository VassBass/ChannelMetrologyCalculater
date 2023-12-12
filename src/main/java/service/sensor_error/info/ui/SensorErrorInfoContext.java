package service.sensor_error.info.ui;

import localization.Messages;
import model.dto.SensorError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.sensor_error.info.SensorErrorInfoManager;
import service.sensor_error.info.ui.swing.SwingSensorErrorInfoButtonsPanel;
import service.sensor_error.info.ui.swing.SwingSensorErrorInfoErrorPanel;
import service.sensor_error.info.ui.swing.SwingSensorErrorInfoSensorPanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SensorErrorInfoContext {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorInfoContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private final SensorError oldError;
    private SensorErrorInfoManager manager;

    public SensorErrorInfoContext(@Nonnull RepositoryFactory repositoryFactory, @Nullable SensorError oldError) {
        this.repositoryFactory = repositoryFactory;
        this.oldError = oldError;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            logger.warn(Messages.Log.MISSING_UI_MANAGER_ERROR);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(SensorErrorInfoSensorPanel.class) || clazz.isAssignableFrom(SwingSensorErrorInfoSensorPanel.class)) {
                element = (T) new SwingSensorErrorInfoSensorPanel(repositoryFactory);
                buffer.put(SensorErrorInfoSensorPanel.class, element);
                buffer.put(SwingSensorErrorInfoSensorPanel.class, element);
            }
            if (clazz.isAssignableFrom(SensorErrorInfoErrorPanel.class) || clazz.isAssignableFrom(SwingSensorErrorInfoErrorPanel.class)) {
                element = (T) new SwingSensorErrorInfoErrorPanel();
                buffer.put(SensorErrorInfoErrorPanel.class, element);
                buffer.put(SwingSensorErrorInfoErrorPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingSensorErrorInfoButtonsPanel.class)) {
                element = (T) new SwingSensorErrorInfoButtonsPanel(manager, oldError);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(SensorErrorInfoManager manager) {
        this.manager = manager;
    }
}
