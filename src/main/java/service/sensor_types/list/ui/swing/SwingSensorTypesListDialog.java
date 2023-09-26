package service.sensor_types.list.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.sensor.SensorRepository;
import service.sensor_types.list.SensorTypesListConfigHolder;
import service.sensor_types.list.ui.SensorTypesListContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static model.ui.builder.CellBuilder.BOTH;
import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorTypesListDialog extends DefaultDialog {
    private static final String TITLE_TEXT = "Типи ПВП";

    private final SensorTypesListContext context;
    private final RepositoryFactory repositoryFactory;

    public SwingSensorTypesListDialog(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull SensorTypesListConfigHolder configHolder,
                                      @Nonnull SensorTypesListContext context) {
        super(applicationScreen, TITLE_TEXT);
        this.repositoryFactory = repositoryFactory;
        this.context = context;
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        SwingSensorTypesListMeasurementPanel measurementPanel = context.getElement(SwingSensorTypesListMeasurementPanel.class);
        SwingSensorTypesListTable table = context.getElement(SwingSensorTypesListTable.class);
        SwingSensorTypesListButtonsPanel buttonsPanel = context.getElement(SwingSensorTypesListButtonsPanel.class);

        table.setTypeList(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(measurementPanel.getSelectedMeasurementName())));

        DefaultPanel panel = new DefaultPanel();
        panel.add(measurementPanel, new CellBuilder().fill(NONE).y(0).weightY(0.05).build());
        panel.add(new JScrollPane(table), new CellBuilder().fill(BOTH).y(1).weightY(0.9).build());
        panel.add(buttonsPanel, new CellBuilder().fill(BOTH).y(2).weightY(0.05).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        SwingSensorTypesListMeasurementPanel measurementPanel = context.getElement(SwingSensorTypesListMeasurementPanel.class);
        SwingSensorTypesListTable table = context.getElement(SwingSensorTypesListTable.class);

        table.setTypeList(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(measurementPanel.getSelectedMeasurementName())));

        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }
}
