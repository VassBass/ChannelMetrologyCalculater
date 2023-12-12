package service.sensor_error.list.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.RootLabelName;
import model.dto.SensorError;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_error.list.SensorErrorListConfigHolder;
import service.sensor_error.list.ui.SensorErrorListContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static model.ui.builder.CellBuilder.BOTH;
import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorErrorListDialog extends DefaultDialog {
    public static final String ERROR_FORMULAS = "errorFormulas";

    private final RepositoryFactory repositoryFactory;

    private final SwingSensorErrorListMeasurementPanel measurementPanel;
    private final SwingSensorErrorListTable table;

    public SwingSensorErrorListDialog(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull SensorErrorListConfigHolder configHolder,
                                      @Nonnull SensorErrorListContext context) {
        super(applicationScreen, Labels.getLabels(SwingSensorErrorListDialog.class).get(ERROR_FORMULAS));
        this.repositoryFactory = repositoryFactory;

        measurementPanel = context.getElement(SwingSensorErrorListMeasurementPanel.class);
        table = context.getElement(SwingSensorErrorListTable.class);
        SwingSensorErrorListButtonsPanel buttonsPanel = context.getElement(SwingSensorErrorListButtonsPanel.class);

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
        SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        String measurementName = measurementPanel.getMeasurementName();
        List<SensorError> list = new ArrayList<>();
        if (measurementName.equalsIgnoreCase(Labels.getRootLabels().get(RootLabelName.ALL_ALT))) {
            list.addAll(sensorErrorRepository.getAll());
        } else {
            List<String> measurementValues = Arrays.asList(measurementRepository.getValues(measurementName));
            list.addAll(sensorErrorRepository.getAll().stream()
                    .filter(se -> measurementValues.contains(se.getMeasurementValue()))
                    .collect(Collectors.toList()));
        }
        table.setSensorErrorsList(list);

        EventQueue.invokeLater(() -> {
            if (this.isVisible()) this.setVisible(false);
            this.setVisible(true);
        });
    }
}
