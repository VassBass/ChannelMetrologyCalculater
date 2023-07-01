package service.converter_tc.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.converter_tc.ConverterTcConfigHolder;
import service.converter_tc.ui.ConverterTcContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class SwingConverterTcDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Перетворювач величин ТО";

    public SwingConverterTcDialog(@Nonnull ApplicationScreen applicationScreen,
                                  @Nonnull ConverterTcConfigHolder configHolder,
                                  @Nonnull ConverterTcContext context) {
        super(applicationScreen, TITLE_TEXT, true);

        SwingTypePanel typePanel = context.getElement(SwingTypePanel.class);
        SwingValuePanel valuePanel = context.getElement(SwingValuePanel.class);
        SwingResultPanel resultPanel = context.getElement(SwingResultPanel.class);
        SwingButtonsPanel buttonsPanel = context.getElement(SwingButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(typePanel, new CellBuilder().x(0).y(0).weightY(0.05).width(1).build());
        panel.add(valuePanel, new CellBuilder().x(1).y(0).weightY(0.05).width(1).build());
        panel.add(new JScrollPane(resultPanel), new CellBuilder().x(0).y(1).weightY(0.9).width(2).build());
        panel.add(buttonsPanel, new CellBuilder().x(0).y(2).weightY(0.05).width(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
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
}