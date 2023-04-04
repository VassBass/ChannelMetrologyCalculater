package repository.repos.calculation_method;

import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class BufferedCalculationMethodRepositorySQLite extends CalculationMethodRepositorySQLite {
    private final Map<String, String> buffer;

    public BufferedCalculationMethodRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = super.getAll();
    }

    @Override
    public Map<String, String> getAll() {
        return buffer;
    }

    @Override
    public String getMethodNameByMeasurementName(@Nonnull String measurementName) {
        return buffer.get(measurementName);
    }

    @Override
    public boolean set(@Nonnull String measurementName, @Nonnull String methodName) {
        if (buffer.containsKey(measurementName)) {
            buffer.put(measurementName, methodName);
            return super.set(measurementName, methodName);
        } else return false;
    }

    @Override
    public boolean add(@Nonnull String measurementName, @Nonnull String methodName) {
        if (buffer.containsKey(measurementName)) return false;

        buffer.put(measurementName, methodName);
        return super.add(measurementName, methodName);
    }

    @Override
    public boolean removeByMeasurementName(@Nonnull String measurementName) {
        if (Objects.nonNull(buffer.remove(measurementName))) {
            return super.removeByMeasurementName(measurementName);
        } else return false;
    }
}
