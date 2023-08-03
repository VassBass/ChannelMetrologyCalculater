package service.measurement.converter.service;

import model.dto.MeasurementTransformFactor;
import repository.RepositoryFactory;
import repository.repos.measurement_factor.MeasurementFactorRepository;

import javax.annotation.Nonnull;

public class DefaultConverter implements Converter {

    private final RepositoryFactory repositoryFactory;

    public DefaultConverter(@Nonnull RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public double convert(String fromMeasurementValue, String toMeasurementValue, double value) {
        if (fromMeasurementValue.equals(toMeasurementValue)) return value;

        MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

        double factor = factorRepository.getBySource(fromMeasurementValue).stream()
                .filter(e -> e.getTransformTo().equals(toMeasurementValue))
                .map(MeasurementTransformFactor::getTransformFactor)
                .findAny()
                .orElse(Double.NaN);

        if (!Double.isNaN(factor)) {
            return value * factor;
        }

        return 0D;
    }
}
