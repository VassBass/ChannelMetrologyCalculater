package service.application;

import service.channel.SwingChannelInitializer;
import service.repository.RepositoryServiceInitializer;
import service.root.ServiceInitializer;

import java.util.LinkedHashSet;
import java.util.Set;

public class ApplicationInitializer implements ServiceInitializer {
    private static final Set<ServiceInitializer> initializerSet = new LinkedHashSet<>();
    private static ApplicationConfigHolder config;

    public ApplicationInitializer(ApplicationConfigHolder configHolder) {
        config = configHolder;
    }

    private static void INIT() {
        ApplicationScreen.init(config);
        initializerSet.add(new RepositoryServiceInitializer());
        initializerSet.add(new SwingChannelInitializer());
    }

    @Override
    public void init() {
        INIT();
        initializerSet.forEach(ServiceInitializer::init);
    }
}
