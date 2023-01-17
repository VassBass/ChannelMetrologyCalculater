package service.root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HashMapFactoryImplementationHolder implements FactoryImplementationHolder {
    private static final Logger logger = LoggerFactory.getLogger(HashMapFactoryImplementationHolder.class);

    private final Map<String, ImplementationFactory> factoryMap;

    public HashMapFactoryImplementationHolder() {
        this.factoryMap = new HashMap<>();
    }

    @Override
    public void factoryRegistration(ImplementationFactory factory) {
        factoryMap.put(factory.getClass().getName(), factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getImplementation(Class<T> clazz) {
        ImplementationFactory factory = factoryMap.get(clazz.getName());

        if (factory == null) {
            logger.warn(String.format("Implementation for %s not registered!", clazz.getName()));
            return null;
        } else return (T) factory;
    }
}
