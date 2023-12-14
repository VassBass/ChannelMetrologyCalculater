package service.channel.info.ui;

import localization.Messages;
import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.measurement.MeasurementRepository;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.swing.*;

import java.util.*;

public class ChannelInfoSwingContext {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInfoSwingContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final RepositoryFactory repositoryFactory;
    private ChannelInfoManager manager;

    public ChannelInfoSwingContext(RepositoryFactory repositoryFactory) {
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
            if (clazz.isAssignableFrom(ChannelInfoCodePanel.class) || clazz.isAssignableFrom(SwingChannelInfoCodePanel.class)) {
                element = (T) new SwingChannelInfoCodePanel(repositoryFactory, manager);
                buffer.put(ChannelInfoCodePanel.class, element);
                buffer.put(SwingChannelInfoCodePanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoNamePanel.class) || clazz.isAssignableFrom(SwingChannelInfoNamePanel.class)) {
                element = (T) new SwingChannelInfoNamePanel();
                buffer.put(ChannelInfoNamePanel.class, element);
                buffer.put(SwingChannelInfoNamePanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoMeasurementPanel.class) || clazz.isAssignableFrom(SwingChannelInfoMeasurementPanel.class)) {
                SwingChannelInfoMeasurementPanel measurementPanel = new SwingChannelInfoMeasurementPanel(manager);
                MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
                measurementPanel.setMeasurementNames(Arrays.asList(measurementRepository.getAllNames()));

                element = (T) measurementPanel;
                buffer.put(ChannelInfoMeasurementPanel.class, element);
                buffer.put(SwingChannelInfoMeasurementPanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoTechnologyNumberPanel.class) || clazz.isAssignableFrom(SwingChannelInfoTechnologyNumberPanel.class)) {
                element = (T) new SwingChannelInfoTechnologyNumberPanel();
                buffer.put(ChannelInfoTechnologyNumberPanel.class, element);
                buffer.put(SwingChannelInfoTechnologyNumberPanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoDatePanel.class) || clazz.isAssignableFrom(SwingChannelInfoDatePanel.class)) {
                element = (T) new SwingChannelInfoDatePanel(manager);
                buffer.put(ChannelInfoDatePanel.class, element);
                buffer.put(SwingChannelInfoDatePanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoProtocolNumberPanel.class) || clazz.isAssignableFrom(SwingChannelInfoProtocolNumberPanel.class)) {
                element = (T) new SwingChannelInfoProtocolNumberPanel();
                buffer.put(ChannelInfoProtocolNumberPanel.class, element);
                buffer.put(SwingChannelInfoProtocolNumberPanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoFrequencyPanel.class) || clazz.isAssignableFrom(SwingChannelInfoFrequencyPanel.class)) {
                element = (T) new SwingChannelInfoFrequencyPanel(manager);
                buffer.put(ChannelInfoFrequencyPanel.class, element);
                buffer.put(SwingChannelInfoFrequencyPanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoNextDatePanel.class) || clazz.isAssignableFrom(SwingChannelInfoNextDatePanel.class)) {
                element = (T) new SwingChannelInfoNextDatePanel();
                buffer.put(ChannelInfoNextDatePanel.class, element);
                buffer.put(SwingChannelInfoNextDatePanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoPathPanel.class) || clazz.isAssignableFrom(SwingChannelInfoPathPanel.class)) {
                SwingChannelInfoPathPanel pathPanel = new SwingChannelInfoPathPanel();
                ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
                Collection<Channel> allChannels = channelRepository.getAll();

                Set<String> departmentSet = new HashSet<>();
                Set<String> areaSet = new HashSet<>();
                Set<String> processSet = new HashSet<>();
                Set<String> installationSet = new HashSet<>();
                for (Channel c : allChannels) {
                    departmentSet.add(c.getDepartment());
                    areaSet.add(c.getArea());
                    processSet.add(c.getProcess());
                    installationSet.add(c.getInstallation());
                }

                pathPanel.setDepartments(departmentSet);
                pathPanel.setAreas(areaSet);
                pathPanel.setProcesses(processSet);
                pathPanel.setInstallations(installationSet);

                element = (T) pathPanel;
                buffer.put(ChannelInfoPathPanel.class, element);
                buffer.put(SwingChannelInfoPathPanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoSensorPanel.class) || clazz.isAssignableFrom(SwingChannelInfoSensorPanel.class)) {
                element = (T) new SwingChannelInfoSensorPanel(manager);
                buffer.put(ChannelInfoSensorPanel.class, element);
                buffer.put(SwingChannelInfoSensorPanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoRangePanel.class) || clazz.isAssignableFrom(SwingChannelInfoRangePanel.class)) {
                element = (T) new SwingChannelInfoRangePanel(manager);
                buffer.put(ChannelInfoRangePanel.class, element);
                buffer.put(SwingChannelInfoRangePanel.class, element);
            }

            if (clazz.isAssignableFrom(ChannelInfoAllowableErrorPanel.class) || clazz.isAssignableFrom(SwingChannelInfoAllowableErrorPanel.class)) {
                element = (T) new SwingChannelInfoAllowableErrorPanel(manager);
                buffer.put(ChannelInfoAllowableErrorPanel.class, element);
                buffer.put(SwingChannelInfoAllowableErrorPanel.class, element);
            }

            if (clazz.isAssignableFrom(SwingChannelInfoButtonsPanel.class)) {
                element = (T) new SwingChannelInfoButtonsPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(Messages.Log.missingImplementation(clazz));
        }

        return element;
    }

    public void registerManager(ChannelInfoManager manager) {
        this.manager = manager;
    }
}
