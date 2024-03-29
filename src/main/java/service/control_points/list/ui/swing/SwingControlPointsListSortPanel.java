package service.control_points.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor.SensorRepository;
import service.control_points.list.ControlPointsListManager;
import service.control_points.list.ui.ControlPointsListSortPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.*;

public class SwingControlPointsListSortPanel extends TitledPanel implements ControlPointsListSortPanel {
    private static final Map<String, String> labels = Labels.getRootLabels();

    private final DefaultComboBox measurementNameList;
    private final DefaultComboBox sensorTypeList;

    public SwingControlPointsListSortPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull ControlPointsListManager manager) {
        super(labels.get(RootLabelName.SORT_PARAMS));
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        measurementNameList = new DefaultComboBox(false);
        List<String> measurementNames = new ArrayList<>();
        measurementNames.add(labels.get(RootLabelName.ALL_ALT));
        measurementNames.addAll(Arrays.asList(measurementRepository.getAllNames()));
        measurementNameList.setList(measurementNames);

        sensorTypeList = new DefaultComboBox(false);
        sensorTypeList.setEnabled(false);

        measurementNameList.addItemListener(e -> {
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
            String selected = measurementNameList.getSelectedString();
            if (selected.equals(labels.get(RootLabelName.ALL_ALT))) {
                setSensorTypeList(Collections.emptyList());
                manager.showAllControlPointsInTable();
            } else {
                setSensorTypeList(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selected)));
                manager.showSortedControlPointsInTable();
            }
        });

        sensorTypeList.addItemListener(e -> {
            if (StringHelper.nonEmpty(sensorTypeList.getSelectedString()) || !measurementNameList.getSelectedString().equals(labels.get(RootLabelName.ALL_ALT))) {
                manager.showSortedControlPointsInTable();
            }
        });

        this.add(measurementNameList, new CellBuilder().x(0).build());
        this.add(sensorTypeList, new CellBuilder().x(1).build());
    }

    @Override
    public void setSensorTypeList(List<String> types) {
        sensorTypeList.setList(types);
        sensorTypeList.setEnabled(!types.isEmpty());
    }

    @Override
    public String getSelectedSensorType() {
        return sensorTypeList.getSelectedString();
    }

    @Override
    public String getSelectedMeasurementName() {
        return measurementNameList.getSelectedString();
    }
}
