package service.calculation.result.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.calculation.CalculationManager;
import service.calculation.protocol.Protocol;
import service.calculation.result.ui.swing.SwingCalculationResultButtonsPanel;
import service.calculation.result.ui.swing.SwingCalculationResultConclusionPanel;
import service.calculation.result.ui.swing.SwingCalculationResultPanel;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SwingCalculationResultContext {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationResultContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final Protocol protocol;

    private CalculationManager manager;

    public SwingCalculationResultContext(@Nonnull Protocol protocol) {
        this.protocol = protocol;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            logger.warn(Messages.Log.MISSING_UI_MANAGER_ERROR);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(CalculationResultConclusionPanel.class) || clazz.isAssignableFrom(SwingCalculationResultConclusionPanel.class)) {
                element = (T) new SwingCalculationResultConclusionPanel(protocol);
                buffer.put(CalculationResultConclusionPanel.class, element);
                buffer.put(SwingCalculationResultConclusionPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingCalculationResultPanel.class)) {
                element = (T) new SwingCalculationResultPanel(protocol);
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(SwingCalculationResultButtonsPanel.class)) {
                element = (T) new SwingCalculationResultButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(CalculationManager manager) {
        this.manager = manager;
    }
}
