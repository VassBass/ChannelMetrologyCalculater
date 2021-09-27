package ui;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class ButtonCell extends JButton {
    public ButtonCell (boolean isHeader) {

        if (isHeader) {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.white;
                }
            });
            this.setBackground(Color.black);
        }else {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.black;
                }
            });
            this.setBackground(Color.white);
        }

        this.setEnabled(false);

    }

    public ButtonCell (boolean isHeader, String text) {
        this.setText(text);

        if (isHeader) {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.white;
                }
            });
            this.setBackground(Color.black);
        }else {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.black;
                }
            });
            this.setBackground(Color.white);
        }

        this.setEnabled(false);
    }

    public ButtonCell (Color background, final Color foreground, String text) {
        super(text);

        this.setUI(new MetalButtonUI(){
            protected Color getDisabledTextColor() {
                return foreground;
            }
        });
        this.setBackground(background);

        this.setEnabled(false);
    }
}
