package service.application;

import service.repository.RepositoryServiceInitializer;
import service.root.ServiceInitializer;

import java.util.HashSet;
import java.util.Set;

public class ApplicationInitializer implements ServiceInitializer {
    private static final Set<ServiceInitializer> initializerSet = new HashSet<>();
    private static ApplicationConfigHolder config;

    public ApplicationInitializer(ApplicationConfigHolder configHolder) {
        config = configHolder;
    }

    private static void INIT() {
        ApplicationScreen.init(config);
        initializerSet.add(new RepositoryServiceInitializer());
    }

    @Override
    public void init() {
        INIT();
        initializerSet.forEach(ServiceInitializer::init);
    }
}
