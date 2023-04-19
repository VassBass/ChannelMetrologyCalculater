package model.ui;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class ButtonCell extends JButton {
    public static final int HEADER = 0;
    public static final int SIMPLE = 1;

    public ButtonCell (int type) {
        if (type == HEADER) {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.WHITE;
                }
            });
            this.setBackground(Color.BLACK);
        }else {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.BLACK;
                }
            });
            this.setBackground(Color.WHITE);
        }

        this.setEnabled(false);
    }

    public ButtonCell (int type, String text) {
        this.setText(text);

        if (type == HEADER) {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.WHITE;
                }
            });
            this.setBackground(Color.BLACK);
        }else {
            this.setUI(new MetalButtonUI(){
                protected Color getDisabledTextColor() {
                    return Color.BLACK;
                }
            });
            this.setBackground(Color.WHITE);
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