package service.measurement.list.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.measurement.list.MeasurementListManager;
import service.measurement.list.ui.swing.SwingMeasurementListButtonsPanel;
import service.measurement.list.ui.swing.SwingMeasurementListFactorTable;
import service.measurement.list.ui.swing.SwingMeasurementListNamePanel;
import service.measurement.list.ui.swing.SwingMeasurementListValueTable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MeasurementListContext {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementListContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private MeasurementListManager manager;

    public MeasurementListContext(@Nonnull RepositoryFactory repositoryFactory) {
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
            if (clazz.isAssignableFrom(MeasurementListNamePanel.class) || clazz.isAssignableFrom(SwingMeasurementListNamePanel.class)) {
                element = (T) new SwingMeasurementListNamePanel(repositoryFactory, manager);
                buffer.put(MeasurementListNamePanel.class, element);
                buffer.put(SwingMeasurementListNamePanel.class, element);
            }
            if (clazz.isAssignableFrom(MeasurementListValueTable.class) || clazz.isAssignableFrom(SwingMeasurementListValueTable.class)) {
                element = (T) new SwingMeasurementListValueTable(manager);
                buffer.put(MeasurementListValueTable.class, element);
                buffer.put(SwingMeasurementListValueTable.class, element);
            }
            if (clazz.isAssignableFrom(MeasurementListFactorTable.class) || clazz.isAssignableFrom(SwingMeasurementListFactorTable.class)) {
                element = (T) new SwingMeasurementListFactorTable();
                buffer.put(MeasurementListFactorTable.class, element);
                buffer.put(SwingMeasurementListFactorTable.class, element);
            }
            if (clazz.isAssignableFrom(SwingMeasurementListButtonsPanel.class)) {
                element = (T) new SwingMeasurementListButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(MeasurementListManager manager) {
        this.manager = manager;
    }
}
