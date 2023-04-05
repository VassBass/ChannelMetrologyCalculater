package model.ui;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog implements UI {
    public static final String MESSAGE_TEXT = "Завантаження...будь ласка зачекайте...";

    private static volatile LoadingDialog instance;
    public static LoadingDialog getInstance() {
        if (instance == null) {
            synchronized (LoadingDialog.class) {
                if (instance == null) instance = new LoadingDialog();
            }
        }
        return instance;
    }

    private LoadingDialog(){
        super();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(MESSAGE_TEXT);

        this.setSize(300, 30);
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
        this.setContentPane(progressBar);
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
