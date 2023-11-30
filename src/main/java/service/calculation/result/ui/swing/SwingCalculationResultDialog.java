package service.calculation.result.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.protocol.Protocol;
import service.calculation.result.ui.SwingCalculationResultContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class SwingCalculationResultDialog extends DefaultDialog implements CalculationCollectDialog {
    private static final String CALCULATION_RESULT = "calculationResult";
    private static final String EMPTY_REFERENCE_NUMBER = "emptyReferenceNumber";

    private final SwingCalculationResultConclusionPanel conclusionPanel;
    private final SwingCalculationResultReferencePanel referencePanel;

    public SwingCalculationResultDialog(@Nonnull ApplicationScreen applicationScreen,
                                        @Nonnull CalculationConfigHolder configHolder,
                                        @Nonnull CalculationManager manager,
                                        @Nonnull SwingCalculationResultContext context,
                                        @Nonnull Protocol protocol) {
        super(applicationScreen, Labels.getLabels(SwingCalculationResultDialog.class).get(CALCULATION_RESULT));

        SwingCalculationResultPanel resultPanel = context.getElement(SwingCalculationResultPanel.class);
        conclusionPanel = context.getElement(SwingCalculationResultConclusionPanel.class);
        SwingCalculationResultButtonsPanel buttonsPanel = context.getElement(SwingCalculationResultButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        int y = 0;
        panel.add(resultPanel, new CellBuilder().y(y++).build());
        panel.add(conclusionPanel, new CellBuilder().y(y++).build());

        boolean suitable = protocol.getRelativeError() <= protocol.getChannel().getAllowableErrorPercent();
        if (!suitable) {
            referencePanel = new SwingCalculationResultReferencePanel();
            panel.add(referencePanel, new CellBuilder().y(y++).build());
        } else referencePanel = null;

        panel.add(buttonsPanel, new CellBuilder().y(y).build());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.disposeCalculation();
            }
        });

        int width = configHolder.getResultDialogWidth();
        int height = configHolder.getResultDialogHeight();
        if (!suitable) height += 35;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(width, height);
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public boolean fillProtocol(Protocol protocol) {
        if (Objects.nonNull(referencePanel)) {
            String referenceNumber = referencePanel.getReferenceNumber();
            if (referenceNumber.isEmpty()) {
                String title = Labels.getRootLabels().get(RootLabelName.INVALID_DATA);
                String message = Messages.getMessages(SwingCalculationResultDialog.class).get(EMPTY_REFERENCE_NUMBER);
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                protocol.setReferenceNumber(referenceNumber);
            }
        }
        protocol.setConclusion(conclusionPanel.getConclusion());
        return true;
    }
}
