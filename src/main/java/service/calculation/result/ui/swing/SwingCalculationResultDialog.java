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

    private final SwingCalculationResultPanel resultPanel;
    private final SwingCalculationResultConclusionPanel conclusionPanel;

    public SwingCalculationResultDialog(@Nonnull ApplicationScreen applicationScreen,
                                        @Nonnull CalculationConfigHolder configHolder,
                                        @Nonnull CalculationManager manager,
                                        @Nonnull SwingCalculationResultContext context) {
        super(applicationScreen, TITLE_TEXT, true);

        resultPanel = context.getElement(SwingCalculationResultPanel.class);
        conclusionPanel = context.getElement(SwingCalculationResultConclusionPanel.class);
        SwingCalculationResultButtonsPanel buttonsPanel = context.getElement(SwingCalculationResultButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(resultPanel, new CellBuilder().y(0).build());
        panel.add(conclusionPanel, new CellBuilder().y(1).build());
        panel.add(buttonsPanel, new CellBuilder().y(2).build());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.disposeCalculation();
            }
        });

        int width = configHolder.getResultDialogWidth();
        int height = configHolder.getResultDialogHeight();
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
            protocol.setConclusion(conclusionPanel.getConclusion());
            return true;
        } else return false;
    }
}
