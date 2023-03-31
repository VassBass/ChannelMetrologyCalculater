package service.calculation.persons.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.dto.Protocol;
import service.calculation.persons.ui.SwingCalculationPersonsContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SwingCalculationPersonsDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE = "Персонал задієний в КМХ";

    private final SwingCalculationPersonsMakersPanel makersPanel;
    private final SwingCalculationPersonsFormerPanel formerPanel;
    private final SwingCalculationPersonsHeadsPanel headsPanel;

    public SwingCalculationPersonsDialog(@Nonnull ApplicationScreen applicationScreen,
                                         @Nonnull CalculationConfigHolder configHolder,
                                         @Nonnull CalculationManager manager,
                                         @Nonnull SwingCalculationPersonsContext context,
                                         @Nonnull Protocol protocol) {
        super(applicationScreen, TITLE, true);

        makersPanel = context.getElement(SwingCalculationPersonsMakersPanel.class);
        formerPanel = context.getElement(SwingCalculationPersonsFormerPanel.class);
        headsPanel = context.getElement(SwingCalculationPersonsHeadsPanel.class);
        SwingCalculationPersonsButtonPanel buttonPanel = context.getElement(SwingCalculationPersonsButtonPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(makersPanel, new CellBuilder().y(0).build());
        panel.add(formerPanel, new CellBuilder().y(1).build());
        panel.add(headsPanel, new CellBuilder().y(2).build());
        panel.add(buttonPanel, new CellBuilder().y(3).build());

        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.showInputDialog();
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
        protocol.setMakers(makersPanel.getMakers());
        protocol.setFormer(formerPanel.getFormer());
        protocol.setHeadOfMetrologyDepartment(headsPanel.getHeadOfMetrologyDepartment());
        protocol.setHeadOfCheckedChannelDepartment(headsPanel.getHeadOfCheckedChannelDepartment());
        protocol.setHeadOfASPCDepartment(headsPanel.getHeadOfASPCDepartment());
        return true;
    }
}
