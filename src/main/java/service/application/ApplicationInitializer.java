package service.application;

import service.repository.RepositoryServiceInitializer;
import service.root.ServiceInitializer;

import java.util.HashSet;
import java.util.Set;

public class ApplicationInitializer implements ServiceInitializer {
    private static final Set<ServiceInitializer> initializerSet = new HashSet<>();

    private static void INIT() {
        initializerSet.add(new RepositoryServiceInitializer());
    }

    @Override
    public void init() {
        INIT();
        initializerSet.forEach(ServiceInitializer::init);
    }
}
