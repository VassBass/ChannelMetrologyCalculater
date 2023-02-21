package service.repository.repos.measurement_factor;

import model.dto.MeasurementTransformFactor;

import java.util.Collection;
import java.util.Map;

public class MeasurementFactorIdGenerator {
    private MeasurementFactorIdGenerator(){}

    public static int generateForMap(Map<Integer, MeasurementTransformFactor> map) {
        int id = 0;
        while (map.containsKey(id)) id++;
        return id;
    }

    public static int generateForCollection(Collection<MeasurementTransformFactor> collection) {
        int id = 0;
        for (MeasurementTransformFactor mtf : collection) {
            if (mtf.getId() == id) {
                id++;
            } else break;
        }
        return id;
    }

    public static int generateForRepository(MeasurementFactorRepository repository) {
        int id = 0;
        while (repository.getById(id) != null) id++;
        return id;
    }
}
