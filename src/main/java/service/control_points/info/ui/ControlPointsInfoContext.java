package service.control_points.info.ui;

import model.dto.ControlPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.control_points.info.ControlPointsInfoManager;
import service.control_points.info.ui.swing.SwingControlPointsInfoButtonsPanel;
import service.control_points.info.ui.swing.SwingControlPointsInfoRangePanel;
import service.control_points.info.ui.swing.SwingControlPointsInfoSensorTypePanel;
import service.control_points.info.ui.swing.SwingControlPointsInfoValuesPanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControlPointsInfoContext {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsInfoContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private final ControlPoints oldCP;

    public ControlPointsInfoContext(@Nonnull RepositoryFactory repositoryFactory,
                                    @Nullable ControlPoints oldCP) {
        this.repositoryFactory = repositoryFactory;
        this.oldCP= oldCP;
    }

    private ControlPointsInfoManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(ControlPointsInfoSensorTypePanel.class) || clazz.isAssignableFrom(SwingControlPointsInfoSensorTypePanel.class)) {
                element = (T) new SwingControlPointsInfoSensorTypePanel(repositoryFactory, oldCP);
                buffer.put(ControlPointsInfoSensorTypePanel.class, element);
                buffer.put(SwingControlPointsInfoSensorTypePanel.class, element);
            }
            if (clazz.isAssignableFrom(ControlPointsInfoRangePanel.class) || clazz.isAssignableFrom(SwingControlPointsInfoRangePanel.class)) {
                element = (T) new SwingControlPointsInfoRangePanel();
                buffer.put(ControlPointsInfoRangePanel.class, element);
                buffer.put(SwingControlPointsInfoRangePanel.class, element);
            }
            if (clazz.isAssignableFrom(ControlPointsInfoValuesPanel.class) || clazz.isAssignableFrom(SwingControlPointsInfoValuesPanel.class)) {
                element = (T) new SwingControlPointsInfoValuesPanel(manager);
                buffer.put(ControlPointsInfoValuesPanel.class, element);
                buffer.put(SwingControlPointsInfoValuesPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingControlPointsInfoButtonsPanel.class)) {
                element = (T) new SwingControlPointsInfoButtonsPanel(manager, oldCP);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(ControlPointsInfoManager manager) {
        this.manager = manager;
    }
}