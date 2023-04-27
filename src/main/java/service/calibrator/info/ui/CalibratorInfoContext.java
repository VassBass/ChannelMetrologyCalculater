package service.calibrator.info.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.calibrator.info.CalibratorInfoManager;
import service.calibrator.info.ui.swing.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CalibratorInfoContext {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorInfoContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private final RepositoryFactory repositoryFactory;
    private CalibratorInfoManager manager;

    public CalibratorInfoContext(@Nonnull RepositoryFactory repositoryFactory) {
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
            if (clazz.isAssignableFrom(CalibratorInfoMeasurementPanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoMeasurementPanel.class)) {
                element = (T) new SwingCalibratorInfoMeasurementPanel(repositoryFactory, manager);
                buffer.put(CalibratorInfoMeasurementPanel.class, element);
                buffer.put(SwingCalibratorInfoMeasurementPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingCalibratorInfoButtonsPanel.class)) {
                element = (T) new SwingCalibratorInfoButtonsPanel(manager);
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(CalibratorInfoCertificatePanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoCertificatePanel.class)) {
                element = (T) new SwingCalibratorInfoCertificatePanel(repositoryFactory);
                buffer.put(CalibratorInfoCertificatePanel.class, element);
                buffer.put(SwingCalibratorInfoCertificatePanel.class, element);
            }
            if (clazz.isAssignableFrom(CalibratorInfoErrorFormulaPanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoErrorFormulaPanel.class)) {
                element = (T) new SwingCalibratorInfoErrorFormulaPanel();
                buffer.put(CalibratorInfoErrorFormulaPanel.class, element);
                buffer.put(SwingCalibratorInfoErrorFormulaPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalibratorInfoNamePanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoNamePanel.class)) {
                element = (T) new SwingCalibratorInfoNamePanel(manager);
                buffer.put(CalibratorInfoNamePanel.class, element);
                buffer.put(SwingCalibratorInfoNamePanel.class, element);
            }
            if (clazz.isAssignableFrom(CalibratorInfoNumberPanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoNumberPanel.class)) {
                element = (T) new SwingCalibratorInfoNumberPanel();
                buffer.put(CalibratorInfoNumberPanel.class, element);
                buffer.put(SwingCalibratorInfoNumberPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalibratorInfoRangePanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoRangePanel.class)) {
                element = (T) new SwingCalibratorInfoRangePanel();
                buffer.put(CalibratorInfoRangePanel.class, element);
                buffer.put(SwingCalibratorInfoRangePanel.class, element);
            }
            if (clazz.isAssignableFrom(CalibratorInfoTypePanel.class) || clazz.isAssignableFrom(SwingCalibratorInfoTypePanel.class)) {
                element = (T) new SwingCalibratorInfoTypePanel();
                buffer.put(CalibratorInfoTypePanel.class, element);
                buffer.put(SwingCalibratorInfoTypePanel.class, element);
            }

            if (Objects.isNull(element)) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(CalibratorInfoManager manager) {
        this.manager = manager;
    }
}
