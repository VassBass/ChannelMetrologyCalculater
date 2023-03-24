package service.calculation.collect.condition.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.calculation.CalculationManager;
import service.calculation.collect.condition.ui.swing.*;

import java.util.HashMap;
import java.util.Map;

public class SwingCalculationControlConditionContext {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationControlConditionContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();

    private CalculationManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(CalculationControlConditionDatePanel.class) ||
                    clazz.isAssignableFrom(SwingCalculationControlConditionDatePanel.class)) {
                element = (T) new SwingCalculationControlConditionDatePanel();
                buffer.put(CalculationControlConditionDatePanel.class, element);
                buffer.put(SwingCalculationControlConditionDatePanel.class, element);
            }
            if (clazz.isAssignableFrom(CalculationControlConditionProtocolNumberPanel.class) ||
                    clazz.isAssignableFrom(SwingCalculationControlConditionProtocolNumberPanel.class)) {
                element = (T) new SwingCalculationControlConditionProtocolNumberPanel();
                buffer.put(CalculationControlConditionProtocolNumberPanel.class, element);
                buffer.put(SwingCalculationControlConditionProtocolNumberPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalculationControlConditionCalibratorPanel.class) ||
                    clazz.isAssignableFrom(SwingCalculationControlConditionCalibratorPanel.class)) {
                element = (T) new SwingCalculationControlConditionCalibratorPanel();
                buffer.put(CalculationControlConditionCalibratorPanel.class, element);
                buffer.put(SwingCalculationControlConditionCalibratorPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalculationControlConditionEnvironmentPanel.class) ||
                    clazz.isAssignableFrom(SwingCalculationControlConditionEnvironmentPanel.class)) {
                element = (T) new SwingCalculationControlConditionEnvironmentPanel();
                buffer.put(CalculationControlConditionEnvironmentPanel.class, element);
                buffer.put(SwingCalculationControlConditionEnvironmentPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingCalculationControlConditionButtonsPanel.class)) {
                element = (T) new SwingCalculationControlConditionButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(CalculationManager manager) {
        this.manager = manager;
    }
}
