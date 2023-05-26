package service.calculation.persons.ui.swing;

import application.ApplicationScreen;
import model.OS;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.persons.CalculationPersonValuesBuffer;
import service.calculation.protocol.Protocol;
import service.calculation.persons.ui.SwingCalculationPersonsContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.List;

public class SwingCalculationPersonsDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE = "Персонал задієний в КМХ";

    private final SwingCalculationPersonsMakersPanel makersPanel;
    private final SwingCalculationPersonsFormerPanel formerPanel;
    private final SwingCalculationPersonsHeadsPanel headsPanel;
    private final SwingCalculationPersonsOSChooserPanel osChooserPanel;

    public SwingCalculationPersonsDialog(@Nonnull ApplicationScreen applicationScreen,
                                         @Nonnull CalculationConfigHolder configHolder,
                                         @Nonnull CalculationManager manager,
                                         @Nonnull SwingCalculationPersonsContext context,
                                         @Nonnull Protocol protocol) {
        super(applicationScreen, TITLE, true);

        makersPanel = context.getElement(SwingCalculationPersonsMakersPanel.class);
        formerPanel = context.getElement(SwingCalculationPersonsFormerPanel.class);
        headsPanel = context.getElement(SwingCalculationPersonsHeadsPanel.class);
        osChooserPanel = context.getElement(SwingCalculationPersonsOSChooserPanel.class);
        SwingCalculationPersonsButtonPanel buttonPanel = context.getElement(SwingCalculationPersonsButtonPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(makersPanel, new CellBuilder().y(0).build());
        panel.add(formerPanel, new CellBuilder().y(1).build());
        panel.add(headsPanel, new CellBuilder().y(2).build());
        panel.add(osChooserPanel, new CellBuilder().y(3).build());
        panel.add(buttonPanel, new CellBuilder().y(4).build());

        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.showInputDialog();
            }
        });

        int width = configHolder.getPersonsDialogWidth();
        int height = configHolder.getPersonsDialogHeight();
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
    public Object getSource() {
        return this;
    }

    @Override
    public boolean fillProtocol(Protocol protocol) {
        List<Map.Entry<String, String>> makers = makersPanel.getMakers();
        Map.Entry<String, String> former = formerPanel.getFormer();
        String headOfMetrologyDepartment = headsPanel.getHeadOfMetrologyDepartment();
        Map.Entry<String, String> headOfCheckedChannelDepartment = headsPanel.getHeadOfCheckedChannelDepartment();
        String headOfASPCDepartment = headsPanel.getHeadOfASPCDepartment();
        OS os = osChooserPanel.getOS();

        protocol.setMakers(makers);
        protocol.setFormer(former);
        protocol.setHeadOfMetrologyDepartment(headOfMetrologyDepartment);
        protocol.setHeadOfCheckedChannelDepartment(headOfCheckedChannelDepartment);
        protocol.setHeadOfASPCDepartment(headOfASPCDepartment);
        protocol.setOs(os);

        CalculationPersonValuesBuffer buffer = CalculationPersonValuesBuffer.getInstance();
        int index = 0;
        if (index < makers.size()) {
            Map.Entry<String, String> firstMaker = makers.get(index++);
            buffer.setFirstMakerName(firstMaker.getKey());
            buffer.setFirstMakerPosition(firstMaker.getValue());
        }
        if (index < makers.size()) {
            Map.Entry<String, String> secondMaker = makers.get(index);
            buffer.setSecondMakerName(secondMaker.getKey());
            buffer.setSecondMakerPosition(secondMaker.getValue());
        }
        buffer.setFormerName(former.getKey());
        buffer.setFormerPosition(former.getValue());
        buffer.setHeadOfMetrologyDepartment(headOfMetrologyDepartment);
        buffer.setHeadOfCheckedChannelDepartmentName(headOfCheckedChannelDepartment.getKey());
        buffer.setHeadOfCheckedChannelDepartmentPosition(headOfCheckedChannelDepartment.getValue());
        buffer.setHeadOfASPCDepartment(headOfASPCDepartment);
        buffer.setOs(os);
        return true;
    }
}
