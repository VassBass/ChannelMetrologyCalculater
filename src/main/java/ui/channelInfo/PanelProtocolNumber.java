package ui.channelInfo;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

public class PanelProtocolNumber extends JPanel {
    private static final String PROTOCOL_NUMBER = "Номер протоколу";

    private final DialogChannel parent;

    private final JTextField protocolNumber;

    PanelProtocolNumber(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createTitledBorder(PROTOCOL_NUMBER));

        this.add(protocolNumber = new ProtocolNumberTextField());
    }

    public String getProtocolNumber() {
        return protocolNumber.getText();
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber.setText(protocolNumber);
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        protocolNumber.addKeyListener(l);
    }

    /**
     * TextField for protocol number
     */
    private class ProtocolNumberTextField extends JTextField {

        private ProtocolNumberTextField(){
            super(10);

            this.addFocusListener(focusListener);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(ProtocolNumberTextField.this);
            }
        };
    }
}
