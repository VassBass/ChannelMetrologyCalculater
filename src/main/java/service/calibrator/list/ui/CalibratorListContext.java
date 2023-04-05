package service.calibrator.list.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.calibrator.list.CalibratorListManager;
import service.calibrator.list.ui.swing.SwingCalibratorListButtonsPanel;
import service.calibrator.list.ui.swing.SwingCalibratorListMeasurementPanel;
import service.calibrator.list.ui.swing.SwingCalibratorListTable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CalibratorListContext {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorListContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;

    public CalibratorListContext(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    private CalibratorListManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(CalibratorListMeasurementPanel.class) || clazz.isAssignableFrom(SwingCalibratorListMeasurementPanel.class)) {
                element = (T) new SwingCalibratorListMeasurementPanel(repositoryFactory, manager);
                buffer.put(CalibratorListMeasurementPanel.class, element);
                buffer.put(SwingCalibratorListMeasurementPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalibratorListTable.class) || clazz.isAssignableFrom(SwingCalibratorListTable.class)) {
                element = (T) new SwingCalibratorListTable(repositoryFactory);
                buffer.put(CalibratorListTable.class, element);
                buffer.put(SwingCalibratorListTable.class, element);
            }
            if (clazz.isAssignableFrom(SwingCalibratorListButtonsPanel.class)) {
                element = (T) new SwingCalibratorListButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(CalibratorListManager manager) {
        this.manager = manager;
    }
}
