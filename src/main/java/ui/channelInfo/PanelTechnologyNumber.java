package ui.channelInfo;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

public class PanelTechnologyNumber extends JPanel {
    private static final String TECHNOLOGY_NUMBER = "*Технологічний номер";

    private final DialogChannel parent;

    private final JTextField technologyNumber;
    private final TitledBorder border;

    PanelTechnologyNumber(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBackground(Color.WHITE);
        this.setBorder(border = BorderFactory.createTitledBorder(TECHNOLOGY_NUMBER));

        this.add(technologyNumber = new TextTechnologyNumber());
    }

    public boolean isTechnologyNumberAvailable(){
        if (technologyNumber.getText().length() == 0){
            border.setTitleColor(Color.RED);
            return false;
        }else {
            border.setTitleColor(Color.BLACK);
            return true;
        }
    }

    public String getTechnologyNumber() {
        return technologyNumber.getText();
    }

    public void setTechnologyNumber(String technologyNumber) {
        this.technologyNumber.setText(technologyNumber);
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        technologyNumber.addKeyListener(l);
    }

    /**
     * TextField for channel technology number
     */
    private class TextTechnologyNumber extends JTextField {

        private TextTechnologyNumber(){
            super(10);

            this.addFocusListener(focusListener);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(TextTechnologyNumber.this);
            }
        };
    }
}
