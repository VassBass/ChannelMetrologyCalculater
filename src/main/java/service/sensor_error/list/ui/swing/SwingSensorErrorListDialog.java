package service.sensor_error.list.ui.swing;

import application.ApplicationScreen;
import model.dto.SensorError;
import model.ui.DefaultPanel;
import model.ui.UI;
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

import static model.ui.builder.CellBuilder.*;

public class SwingSensorErrorListDialog extends JDialog implements UI {
    public static final String TITLE_TEXT = "Формули похибок ПВП";

    private final RepositoryFactory repositoryFactory;

    private final SwingSensorErrorListMeasurementPanel measurementPanel;
    private final SwingSensorErrorListTable table;

    public SwingSensorErrorListDialog(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull SensorErrorListConfigHolder configHolder,
                                      @Nonnull SensorErrorListContext context) {
        super(applicationScreen, TITLE_TEXT, true);
        this.repositoryFactory = repositoryFactory;

        measurementPanel = context.getElement(SwingSensorErrorListMeasurementPanel.class);
        table = context.getElement(SwingSensorErrorListTable.class);
        SwingSensorErrorListButtonsPanel buttonsPanel = context.getElement(SwingSensorErrorListButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(measurementPanel, new CellBuilder().fill(NONE).y(0).build());
        panel.add(table, new CellBuilder().fill(BOTH).y(1).build());
        panel.add(buttonsPanel, new CellBuilder().fill(BOTH).y(2).build());

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
        if (measurementName.equalsIgnoreCase("Всі")) {
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

    @Override
    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    @Override
    public void shutdown() {
        this.dispose();
    }

    @Override
    public Object getSource() {
        return this;
    }
}
