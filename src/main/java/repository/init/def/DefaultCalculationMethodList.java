package repository.init.def;

import model.dto.Measurement;
import repository.repos.calculation_method.CalculationMethodRepository;

public class DefaultCalculationMethodList implements DefaultList {

    private final CalculationMethodRepository repository;

    public DefaultCalculationMethodList(CalculationMethodRepository repository){
        this.repository = repository;
    }

    @Override
    public void insertDefaultList() {
        repository.add(Measurement.TEMPERATURE, "МКМХ №5300.01:18");
        repository.add(Measurement.PRESSURE, "МКМХ №5300.02:18");
        repository.add(Measurement.CONSUMPTION, "МКМХ №5300.07:20");
    }
}
