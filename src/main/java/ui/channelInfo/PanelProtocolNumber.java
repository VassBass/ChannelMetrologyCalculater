package ui.channelInfo;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PanelProtocolNumber extends JPanel {
    private final DialogChannel parent;

    PanelProtocolNumber(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.add(new ProtocolNumberTextField());
    }

    /**
     * TextField for protocol number
     */
    private class ProtocolNumberTextField extends JTextField {
        private static final String PROTOCOL_NUMBER = "Номер протоколу";

        private ProtocolNumberTextField(){
            super(10);

            this.setBorder(BorderFactory.createTitledBorder(PROTOCOL_NUMBER));

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
