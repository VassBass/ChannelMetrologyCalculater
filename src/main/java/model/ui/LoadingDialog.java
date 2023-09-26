package model.ui;

import util.ScreenPoint;

import javax.swing.*;

import java.awt.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class LoadingDialog extends DefaultDialog {
    public static final String MESSAGE_TEXT = "Завантаження...будь ласка зачекайте...";

    private static final int width = 300;
    private static final int height = 30;

    private final Window owner;

    public LoadingDialog(DefaultDialog owner){
        super(owner, EMPTY);
        this.owner = owner;
        init();
    }

    public LoadingDialog(MainScreen owner) {
        super(owner, EMPTY);
        this.owner = owner;
        init();
    }

    private void init() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(MESSAGE_TEXT);

        this.setSize(width, height);
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
        this.setContentPane(progressBar);
        this.setLocation(ScreenPoint.center(owner, this));
    }
}
