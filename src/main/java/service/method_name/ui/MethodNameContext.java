package service.method_name.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.method_name.MethodNameManager;
import service.method_name.ui.swing.SwingButtonsPanel;
import service.method_name.ui.swing.SwingMeasurementNamePanel;
import service.method_name.ui.swing.SwingMethodNamePanel;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodNameContext {
    private static final Logger logger = LoggerFactory.getLogger(MethodNameContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private MethodNameManager manager;

    public MethodNameContext(@Nonnull RepositoryFactory repositoryFactory) {
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
            if (clazz.isAssignableFrom(MethodNamePanel.class) || clazz.isAssignableFrom(SwingMethodNamePanel.class)) {
                element = (T) new SwingMethodNamePanel();
                buffer.put(MethodNamePanel.class, element);
                buffer.put(SwingMethodNamePanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingButtonsPanel.class)) {
                element = (T) new SwingButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(MethodNameManager manager) {
        this.manager = manager;
    }
}
