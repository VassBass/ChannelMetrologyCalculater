package application;

public interface AbstractApplicationContext {
    <T> T get (Class<T> clazz);
}
