package repository.init.def;

import java.util.Map;
import localization.Labels;
import model.dto.Measurement;
import repository.repos.calculation_method.CalculationMethodRepository;

public class DefaultCalculationMethodList implements DefaultList {

    private final CalculationMethodRepository repository;

    public DefaultCalculationMethodList(CalculationMethodRepository repository){
        this.repository = repository;
    }

    @Override
    public void insertDefaultList() {
        Map<String, String> labels = Labels.getLabels(DefaultCalculationMethodList.class);
        repository.add(Measurement.TEMPERATURE, labels.get("temperatureMethodName"));
        repository.add(Measurement.PRESSURE, labels.get("pressureMethodName"));
        repository.add(Measurement.CONSUMPTION, labels.get("consumptionMethodName"));
    }
}
