package service.measurement.list.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.list.MeasurementListConfigHolder;
import service.measurement.list.ui.MeasurementListContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SwingMeasurementListDialog extends DefaultDialog {
    private static final String TITLE_TEXT = "Вид вимірюваннь";

    private final RepositoryFactory repositoryFactory;
    private final SwingMeasurementListNamePanel namePanel;
    private final SwingMeasurementListValueTable valueTable;
    private final SwingMeasurementListFactorTable factorTable;

    public SwingMeasurementListDialog(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull MeasurementListConfigHolder configHolder,
                                      @Nonnull MeasurementListContext context) {
        super(applicationScreen, TITLE_TEXT);
        this.repositoryFactory = repositoryFactory;
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        namePanel = context.getElement(SwingMeasurementListNamePanel.class);
        valueTable = context.getElement(SwingMeasurementListValueTable.class);
        factorTable = context.getElement(SwingMeasurementListFactorTable.class);
        SwingMeasurementListButtonsPanel buttonsPanel = context.getElement(SwingMeasurementListButtonsPanel.class);

        String measurementName = namePanel.getSelectedName();
        valueTable.setValueList(Arrays.asList(measurementRepository.getValues(measurementName)));

        DefaultPanel panel = new DefaultPanel();
        panel.add(namePanel, new CellBuilder().weightY(0.05).height(1).width(1).x(0).y(0).build());
        panel.add(new JScrollPane(valueTable), new CellBuilder().weightY(0.90).height(1).width(1).x(0).y(1).build());
        panel.add(new JScrollPane(factorTable), new CellBuilder().height(2).width(1).x(1).y(0).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.05).height(1).width(2).x(0).y(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        String measurementName = namePanel.getSelectedName();
        valueTable.setValueList(Arrays.asList(measurementRepository.getValues(measurementName)));
        factorTable.setFactorList(null, null);

        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }
}
