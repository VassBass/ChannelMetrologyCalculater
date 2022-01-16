package ui.model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class DefaultButton extends JButton {

    public static final Color BACKGROUND_COLOR = new Color(51,51,51);

    public DefaultButton(String text){
        super(text);
        this.setBackground(BACKGROUND_COLOR);
        this.setForeground(Color.WHITE);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(true);

        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JButton button = (JButton) e.getSource();

                if (button.getModel().isPressed()) {
                    button.setBackground(BACKGROUND_COLOR.darker());
                } else {
                    button.setBackground(BACKGROUND_COLOR);
                }

            }
        });
    }
}