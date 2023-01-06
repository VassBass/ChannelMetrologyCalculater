package factory;

public abstract class AbstractFactory {
    public abstract <T> T create(Class<T> clazz);
}
