package service.channel.server;

import application.ApplicationScreen;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.TitledTextField;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.channel.exchange.heroku.api.ResponseWorker;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class TestServerDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Тестування серверу";

    public TestServerDialog(@Nonnull ApplicationScreen applicationScreen, @Nonnull ResponseWorker responseWorker) {
        super(applicationScreen, TITLE_TEXT, true);

        TitledTextField idField = new TitledTextField(4, "Content id");

        TitledTextField responseField = new TitledTextField(50, "Server response");
        responseField.setEnabled(false);

        DefaultButton closeBtn = new DefaultButton("Close");
        DefaultButton sendBtn = new DefaultButton("Send request");
        DefaultPanel buttonPanel = new DefaultPanel();
        buttonPanel.add(closeBtn, new CellBuilder().x(0).fill(CellBuilder.NONE).build());
        buttonPanel.add(sendBtn, new CellBuilder().x(1).fill(CellBuilder.NONE).build());

        closeBtn.addActionListener(e -> this.shutdown());
        sendBtn.addActionListener(e -> responseField.setText(responseWorker.getContent(Integer.parseInt(idField.getText()))));

        DefaultPanel panel = new DefaultPanel();
        panel.add(idField, new CellBuilder().y(0).weightY(0.05).build());
        panel.add(responseField, new CellBuilder().y(1).weightY(0.9).build());
        panel.add(buttonPanel, new CellBuilder().y(2).weightY(0.05).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
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
