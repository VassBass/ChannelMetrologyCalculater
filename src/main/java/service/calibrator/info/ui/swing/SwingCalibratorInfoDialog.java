package service.calibrator.info.ui.swing;

import localization.Labels;
import model.dto.Calibrator;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.info.CalibratorInfoConfigHolder;
import service.calibrator.info.ui.CalibratorInfoContext;
import service.calibrator.list.ui.swing.SwingCalibratorListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SwingCalibratorInfoDialog extends DefaultDialog {
    private static final String DEFAULT_TITLE = Labels.getRootLabels().get("calibrator");

    public SwingCalibratorInfoDialog(@Nonnull SwingCalibratorListDialog calibratorListDialog,
                                     @Nonnull CalibratorInfoConfigHolder configHolder,
                                     @Nonnull CalibratorInfoContext context,
                                     @Nullable Calibrator calibrator) {
        super(calibratorListDialog, Objects.isNull(calibrator) ? DEFAULT_TITLE : calibrator.getName());

        SwingCalibratorInfoMeasurementPanel measurementPanel = context.getElement(SwingCalibratorInfoMeasurementPanel.class);
        SwingCalibratorInfoTypePanel typePanel = context.getElement(SwingCalibratorInfoTypePanel.class);
        SwingCalibratorInfoNamePanel namePanel = context.getElement(SwingCalibratorInfoNamePanel.class);
        SwingCalibratorInfoNumberPanel numberPanel = context.getElement(SwingCalibratorInfoNumberPanel.class);
        SwingCalibratorInfoRangePanel rangePanel = context.getElement(SwingCalibratorInfoRangePanel.class);
        SwingCalibratorInfoErrorFormulaPanel errorFormulaPanel = context.getElement(SwingCalibratorInfoErrorFormulaPanel.class);
        SwingCalibratorInfoCertificatePanel certificatePanel = context.getElement(SwingCalibratorInfoCertificatePanel.class);
        SwingCalibratorInfoButtonsPanel buttonsPanel = context.getElement(SwingCalibratorInfoButtonsPanel.class);

        if (Objects.nonNull(calibrator)) {
            measurementPanel.setMeasurementName(calibrator.getMeasurementName());
            measurementPanel.setMeasurementValue(calibrator.getMeasurementValue());
            typePanel.setType(calibrator.getType());
            namePanel.setCalibratorName(calibrator.getName());
            numberPanel.setNumber(calibrator.getNumber());
            rangePanel.setRangeMin(calibrator.getRangeMin());
            rangePanel.setRangeMax(calibrator.getRangeMax());
            rangePanel.setMeasurementValue(calibrator.getMeasurementValue());
            errorFormulaPanel.setErrorFormula(calibrator.getErrorFormula());
            certificatePanel.setCertificateInfo(calibrator.getCertificate());
        } else {
            rangePanel.setMeasurementValue(measurementPanel.getMeasurementValue());
        }

        DefaultPanel panel = new DefaultPanel();
        panel.add(measurementPanel, new CellBuilder().x(0).y(0).width(1).height(1).build());
        panel.add(typePanel, new CellBuilder().x(0).y(1).width(1).height(1).build());
        panel.add(namePanel, new CellBuilder().x(0).y(2).width(1).height(1).build());
        panel.add(numberPanel, new CellBuilder().x(0).y(3).width(1).height(1).build());
        panel.add(rangePanel, new CellBuilder().x(0).y(4).width(1).height(1).build());
        panel.add(errorFormulaPanel, new CellBuilder().x(0).y(5).width(1).height(1).build());
        panel.add(certificatePanel, new CellBuilder().x(1).y(0).width(1).height(3).build());
        panel.add(buttonsPanel, new CellBuilder().x(0).y(6).width(2).height(1).build());

        int width = configHolder.getDialogWidth();
        int height = configHolder.getDialogHeight();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(width, height);
        this.setLocation(ScreenPoint.center(calibratorListDialog, this));
        this.setContentPane(panel);
    }
}
