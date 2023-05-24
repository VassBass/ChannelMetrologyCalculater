package service.measurement.info.ui;

import model.dto.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.measurement.info.MeasurementInfoManager;
import service.measurement.info.ui.swing.SwingMeasurementInfoButtonsPanel;
import service.measurement.info.ui.swing.SwingMeasurementInfoFactorsPanel;
import service.measurement.info.ui.swing.SwingMeasurementInfoNamePanel;
import service.measurement.info.ui.swing.SwingMeasurementInfoValuePanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MeasurementInfoContext {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementInfoContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private MeasurementInfoManager manager;
    private final Measurement oldMeasurement;

    public MeasurementInfoContext(@Nonnull RepositoryFactory repositoryFactory, @Nullable Measurement oldMeasurement) {
        this.repositoryFactory = repositoryFactory;
        this.oldMeasurement = oldMeasurement;
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
            if (clazz.isAssignableFrom(MeasurementInfoNamePanel.class) || clazz.isAssignableFrom(SwingMeasurementInfoNamePanel.class)) {
                element = (T) new SwingMeasurementInfoNamePanel(repositoryFactory, manager, oldMeasurement);
                buffer.put(MeasurementInfoNamePanel.class, element);
                buffer.put(SwingMeasurementInfoNamePanel.class, element);
            }
            if (clazz.isAssignableFrom(MeasurementInfoValuePanel.class) || clazz.isAssignableFrom(SwingMeasurementInfoValuePanel.class)) {
                element = (T) new SwingMeasurementInfoValuePanel(manager, oldMeasurement);
                buffer.put(MeasurementInfoValuePanel.class, element);
                buffer.put(SwingMeasurementInfoValuePanel.class, element);
            }
            if (clazz.isAssignableFrom(MeasurementInfoFactorsPanel.class) || clazz.isAssignableFrom(SwingMeasurementInfoFactorsPanel.class)) {
                element = (T) new SwingMeasurementInfoFactorsPanel(repositoryFactory, oldMeasurement);
                buffer.put(MeasurementInfoFactorsPanel.class, element);
                buffer.put(SwingMeasurementInfoFactorsPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingMeasurementInfoButtonsPanel.class)) {
                element = (T) new SwingMeasurementInfoButtonsPanel(manager, oldMeasurement);
                buffer.put(clazz, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(MeasurementInfoManager manager) {
        this.manager = manager;
    }
}
