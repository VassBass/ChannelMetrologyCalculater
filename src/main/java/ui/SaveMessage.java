package ui;

import controller.FileBrowser;
import converters.ConverterUI;

import javax.swing.*;
import java.awt.*;

public class SaveMessage extends JWindow {
    private final Window window;
    private JLabel anim, message;

    public SaveMessage(Window window){
        super();
        this.window = window;
        this.setAlwaysOnTop(true);

        this.createElements();
        this.build();
    }

    public void createElements() {
        Icon animation = new ImageIcon(FileBrowser.FILE_IMAGE_ANIM_LOAD.getAbsolutePath());
        this.anim = new JLabel(animation);

        this.message = new JLabel("Запис даних");
    }

    public void build() {
        this.setSize(125,25);
        this.setLocation(ConverterUI.POINT_TOP_LEFT(this.window));
        this.setContentPane(new MainPanel());
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(anim, new Cell(0));
            this.add(message, new Cell(1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell (int x){
                this.fill = BOTH;

                this.gridx = x;
                this.gridy = 0;
            }
        }
    }
}
