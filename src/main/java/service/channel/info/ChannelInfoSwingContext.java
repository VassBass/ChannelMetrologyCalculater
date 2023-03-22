package service.channel.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.channel.info.ui.*;
import service.channel.info.ui.swing.*;

import java.util.HashMap;
import java.util.Map;

public class ChannelInfoSwingContext {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInfoSwingContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final RepositoryFactory repositoryFactory;

    public ChannelInfoSwingContext(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    private ChannelInfoManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(ChannelInfoCodePanel.class) || clazz.isAssignableFrom(SwingChannelInfoCodePanel.class))
                element = (T) new SwingChannelInfoCodePanel(repositoryFactory, manager);
            if (clazz.isAssignableFrom(ChannelInfoNamePanel.class) || clazz.isAssignableFrom(SwingChannelInfoNamePanel.class))
                element = (T) new SwingChannelInfoNamePanel();
            if (clazz.isAssignableFrom(ChannelInfoMeasurementPanel.class) || clazz.isAssignableFrom(SwingChannelInfoMeasurementPanel.class))
                element = (T) new SwingChannelInfoMeasurementPanel(manager);
            if (clazz.isAssignableFrom(ChannelInfoTechnologyNumberPanel.class) || clazz.isAssignableFrom(SwingChannelInfoTechnologyNumberPanel.class))
                element = (T) new SwingChannelInfoTechnologyNumberPanel();
            if (clazz.isAssignableFrom(ChannelInfoDatePanel.class) || clazz.isAssignableFrom(SwingChannelInfoDatePanel.class))
                element = (T) new SwingChannelInfoDatePanel(manager);
            if (clazz.isAssignableFrom(ChannelInfoProtocolNumberPanel.class) || clazz.isAssignableFrom(SwingChannelInfoProtocolNumberPanel.class))
                element = (T) new SwingChannelInfoProtocolNumberPanel();
            if (clazz.isAssignableFrom(ChannelInfoFrequencyPanel.class) || clazz.isAssignableFrom(SwingChannelInfoFrequencyPanel.class))
                element = (T) new SwingChannelInfoFrequencyPanel(manager);
            if (clazz.isAssignableFrom(ChannelInfoNextDatePanel.class) || clazz.isAssignableFrom(SwingChannelInfoNextDatePanel.class))
                element = (T) new SwingChannelInfoNextDatePanel();
            if (clazz.isAssignableFrom(ChannelInfoPathPanel.class) || clazz.isAssignableFrom(SwingChannelInfoPathPanel.class))
                element = (T) new SwingChannelInfoPathPanel();
            if (clazz.isAssignableFrom(ChannelInfoSensorPanel.class) || clazz.isAssignableFrom(SwingChannelInfoSensorPanel.class))
                element = (T) new SwingChannelInfoSensorPanel(manager);
            if (clazz.isAssignableFrom(ChannelInfoRangePanel.class) || clazz.isAssignableFrom(SwingChannelInfoRangePanel.class))
                element = (T) new SwingChannelInfoRangePanel(manager);
            if (clazz.isAssignableFrom(ChannelInfoAllowableErrorPanel.class) || clazz.isAssignableFrom(SwingChannelInfoAllowableErrorPanel.class))
                element = (T) new SwingChannelInfoAllowableErrorPanel(manager);
            if (clazz.isAssignableFrom(SwingChannelInfoButtonsPanel.class)) {
                element = (T) new SwingChannelInfoButtonsPanel(manager);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
            else buffer.put(clazz, element);
        }

        return element;
    }

    public void registerManager(ChannelInfoManager manager) {
        this.manager = manager;
    }
}
