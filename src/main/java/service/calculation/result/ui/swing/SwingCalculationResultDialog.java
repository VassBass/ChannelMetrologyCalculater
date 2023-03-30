package service.calculation.result.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.dto.Protocol;
import service.calculation.result.ui.SwingCalculationResultContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class SwingCalculationResultDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE_TEXT = "Результати розрахунку";

    private final SwingCalculationResultConclusionPanel conclusionPanel;
    private final SwingCalculationResultReferencePanel referencePanel;

    public SwingCalculationResultDialog(@Nonnull ApplicationScreen applicationScreen,
                                        @Nonnull CalculationConfigHolder configHolder,
                                        @Nonnull CalculationManager manager,
                                        @Nonnull SwingCalculationResultContext context,
                                        @Nonnull Protocol protocol) {
        super(applicationScreen, TITLE_TEXT, true);

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
    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
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
    public boolean fillProtocol(Protocol protocol) {
        if (Objects.nonNull(conclusionPanel)) {
            if (Objects.nonNull(referencePanel)) {
                String referenceNumber = referencePanel.getReferenceNumber();
                if (referenceNumber.isEmpty()) {
                    String message = "Поле вводу номера довідки не має бути пустим";
                    JOptionPane.showMessageDialog(this, message, "Некоректні дані", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    protocol.setReferenceNumber(referenceNumber);
                }
            }
            protocol.setConclusion(conclusionPanel.getConclusion());
            return true;
        } else return false;
    }
}
