package ui.model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class DefaultButton extends JButton {

    private final Color buttonColor = new Color(51,51,51);

    public DefaultButton(String text){
        super(text);
        this.setBackground(this.buttonColor);
        this.setForeground(Color.WHITE);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(true);

        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JButton button = (JButton) e.getSource();

                if (button.getModel().isPressed()) {
                    button.setBackground(buttonColor.darker());
                } else {
                    button.setBackground(buttonColor);
                }

            }
        });
    }
}