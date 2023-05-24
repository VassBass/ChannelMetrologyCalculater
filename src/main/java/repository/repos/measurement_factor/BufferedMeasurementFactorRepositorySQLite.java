package repository.repos.measurement_factor;

import model.dto.MeasurementTransformFactor;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedMeasurementFactorRepositorySQLite extends MeasurementFactorRepositorySQLite {
    private final Map<Integer, MeasurementTransformFactor> buffer;

    public BufferedMeasurementFactorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(MeasurementTransformFactor::getId, Function.identity()));
    }

    @Override
    public Collection<MeasurementTransformFactor> getAll() {
        return buffer.values();
    }

    @Override
    public MeasurementTransformFactor getById(int id) {
        return buffer.get(id);
    }

    @Override
    public Collection<MeasurementTransformFactor> getBySource(@Nonnull String source) {
        return buffer.values().stream()
                .filter(mtf -> mtf.getTransformFrom().equals(source))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<MeasurementTransformFactor> getByResult(@Nonnull String result) {
        return buffer.values().stream()
                .filter(mtf -> mtf.getTransformTo().equals(result))
                .collect(Collectors.toSet());
    }

    @Override
    public int add(@Nonnull String source, @Nonnull String result, double factor) {
        int id = super.add(source, result, factor);
        if (id >= 0 && !buffer.containsKey(id)) {
            buffer.put(id, new MeasurementTransformFactor(id, source, result, factor));
            return id;
        }
        return -1;
    }

    @Override
    public boolean set(@Nonnull MeasurementTransformFactor mtf) {
        int id = mtf.getId();
        if (buffer.containsKey(id)) {
            buffer.put(id, mtf);
            return super.set(mtf);
        } else return false;
    }

    @Override
    public boolean rewrite(@Nonnull Collection<MeasurementTransformFactor> mtf) {
        List<MeasurementTransformFactor> list = new ArrayList<>(mtf);
        buffer.clear();
        buffer.putAll(list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(MeasurementTransformFactor::getId, Function.identity())));
        return super.rewrite(list);
    }

    @Override
    public boolean changeAllSources(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return true;

        buffer.values().forEach(mtf -> {
            if (mtf.getTransformFrom().equals(oldValue)) mtf.setTransformFrom(newValue);
        });
        return super.changeAllSources(oldValue, newValue);
    }

    @Override
    public boolean changeAllResults(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return true;

        buffer.values().forEach(mtf -> {
            if (mtf.getTransformTo().equals(oldValue)) mtf.setTransformTo(newValue);
        });
        return super.changeAllResults(oldValue, newValue);
    }

    @Override
    public boolean changeFactor(int id, double factor) {
        if (buffer.containsKey(id)) {
            buffer.get(id).setTransformFactor(factor);
            return super.changeFactor(id, factor);
        } else return false;
    }

    @Override
    public boolean removeById(int id) {
        if (buffer.containsKey(id)) {
            buffer.remove(id);
            return super.removeById(id);
        } else return false;
    }

    @Override
    public boolean removeBySource(@Nonnull String source) {
        buffer.values().removeIf(mtf -> mtf.getTransformFrom().equals(source));
        return super.removeBySource(source);
    }

    @Override
    public boolean removeByResult(@Nonnull String result) {
        buffer.values().removeIf(mtf -> mtf.getTransformTo().equals(result));
        return super.removeByResult(result);
    }
}
