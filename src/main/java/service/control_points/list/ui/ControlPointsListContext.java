package service.control_points.list.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.control_points.list.ControlPointsListManager;
import service.control_points.list.ui.swing.SwingControlPointsListButtonsPanel;
import service.control_points.list.ui.swing.SwingControlPointsListSortPanel;
import service.control_points.list.ui.swing.SwingControlPointsListTable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControlPointsListContext {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsListContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private ControlPointsListManager manager;

    public ControlPointsListContext(@Nonnull RepositoryFactory repositoryFactory) {
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
            if (clazz.isAssignableFrom(ControlPointsListSortPanel.class) || clazz.isAssignableFrom(SwingControlPointsListSortPanel.class)) {
                element = (T) new SwingControlPointsListSortPanel(repositoryFactory, manager);
                buffer.put(ControlPointsListSortPanel.class, element);
                buffer.put(SwingControlPointsListSortPanel.class, element);
            }
            if (clazz.isAssignableFrom(ControlPointsListTable.class) || clazz.isAssignableFrom(SwingControlPointsListTable.class)) {
                element = (T) new SwingControlPointsListTable(repositoryFactory);
                buffer.put(ControlPointsListTable.class, element);
                buffer.put(SwingControlPointsListTable.class, element);
            }
            if (clazz.isAssignableFrom(SwingControlPointsListButtonsPanel.class)) {
                element = (T) new SwingControlPointsListButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(ControlPointsListManager manager) {
        this.manager = manager;
    }
}
