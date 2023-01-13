package service.application;

public interface ImplementationFactory {
    <T> T getImplementation(Class<T> clazz);
}
