package service.impl;

import model.Sensor;
import repository.SensorRepository;
import repository.impl.SensorRepositorySQLite;
import service.SensorService;

import java.util.List;
import java.util.stream.Collectors;

public class SensorServiceImpl implements SensorService {
    private static SensorServiceImpl service;

    private final SensorRepository repository;

    private SensorServiceImpl(){
        repository = SensorRepositorySQLite.getInstance();
    }

    public SensorServiceImpl(SensorRepository repository){
        this.repository = repository;
    }

    public static SensorServiceImpl getInstance() {
        if (service == null) service = new SensorServiceImpl();

        return service;
    }

    @Override
    public List<String> getAllTypesWithoutROSEMOUNT() {
        return repository.getAllTypes().stream()
                .filter(t -> !t.contains(Sensor.ROSEMOUNT))
                .collect(Collectors.toList());
    }
}