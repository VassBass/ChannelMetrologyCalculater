package service.calculation.persons.ui;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.calculation.CalculationManager;
import service.calculation.persons.ui.swing.*;
import service.calculation.protocol.Protocol;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SwingCalculationPersonsContext {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationPersonsContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final RepositoryFactory repositoryFactory;
    private final Protocol protocol;

    private CalculationManager manager;

    public SwingCalculationPersonsContext(@Nonnull RepositoryFactory repositoryFactory,
                                          @Nonnull Protocol protocol) {
        this.repositoryFactory = repositoryFactory;
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
            if (clazz.isAssignableFrom(CalculationPersonsMakersPanel.class) || clazz.isAssignableFrom(SwingCalculationPersonsMakersPanel.class)) {
                element = (T) new SwingCalculationPersonsMakersPanel(repositoryFactory);
                buffer.put(CalculationPersonsMakersPanel.class, element);
                buffer.put(SwingCalculationPersonsMakersPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalculationPersonsFormerPanel.class) || clazz.isAssignableFrom(SwingCalculationPersonsFormerPanel.class)) {
                element = (T) new SwingCalculationPersonsFormerPanel(repositoryFactory);
                buffer.put(CalculationPersonsFormerPanel.class, element);
                buffer.put(SwingCalculationPersonsFormerPanel.class, element);
            }
            if (clazz.isAssignableFrom(CalculationPersonsHeadsPanel.class) || clazz.isAssignableFrom(SwingCalculationPersonsHeadsPanel.class)) {
                element = (T) new SwingCalculationPersonsHeadsPanel(repositoryFactory, protocol);
                buffer.put(CalculationPersonsHeadsPanel.class, element);
                buffer.put(SwingCalculationPersonsHeadsPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingCalculationPersonsButtonPanel.class)) {
                element = (T) new SwingCalculationPersonsButtonPanel(manager);
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(CalculationPersonsOSChooserPanel.class) || clazz.isAssignableFrom(SwingCalculationPersonsOSChooserPanel.class)) {
                element = (T) new SwingCalculationPersonsOSChooserPanel();
                buffer.put(CalculationPersonsOSChooserPanel.class, element);
                buffer.put(SwingCalculationPersonsOSChooserPanel.class, element);
            }

            if (element == null) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(CalculationManager manager) {
        this.manager = manager;
    }
}
