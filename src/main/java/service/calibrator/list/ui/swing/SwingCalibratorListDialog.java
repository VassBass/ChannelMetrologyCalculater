package service.calibrator.list.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.RootLabelName;
import model.dto.Calibrator;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calibrator.list.CalibratorListConfigHolder;
import service.calibrator.list.ui.CalibratorListContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static model.ui.builder.CellBuilder.BOTH;
import static model.ui.builder.CellBuilder.VERTICAL;

public class SwingCalibratorListDialog extends DefaultDialog {
    private final RepositoryFactory repositoryFactory;
    private final SwingCalibratorListMeasurementPanel measurementPanel;
    private final SwingCalibratorListTable table;

    public SwingCalibratorListDialog(@Nonnull ApplicationScreen applicationScreen,
                                     @Nonnull RepositoryFactory repositoryFactory,
                                     @Nonnull CalibratorListConfigHolder configHolder,
                                     @Nonnull CalibratorListContext context) {
        super(applicationScreen, Labels.getRootLabels().get(RootLabelName.CALIBRATORS_LIST));
        this.repositoryFactory = repositoryFactory;

        measurementPanel = context.getElement(SwingCalibratorListMeasurementPanel.class);
        table = context.getElement(SwingCalibratorListTable.class);
        SwingCalibratorListButtonsPanel buttonsPanel = context.getElement(SwingCalibratorListButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(measurementPanel, new CellBuilder().fill(BOTH).weightY(0.05).y(0).build());
        panel.add(table, new CellBuilder().fill(BOTH).weightY(0.9).y(1).build());
        panel.add(buttonsPanel, new CellBuilder().fill(VERTICAL).weightY(0.05).y(2).build());

        int width = configHolder.getDialogWidth();
        int height = configHolder.getDialogHeight();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(width, height);
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
        String measurementName = measurementPanel.getSelectedMeasurement();
        List<Calibrator> calibratorList = calibratorRepository.getAll().stream()
                .filter(c -> c.getMeasurementName().equals(measurementName))
                .collect(Collectors.toList());
        table.setCalibratorList(calibratorList);
        EventQueue.invokeLater(() -> {
            if (this.isVisible()) this.setVisible(false);
            this.setVisible(true);
        });
    }
}
