package ui.channelInfo.panel;

import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

public class PanelProtocolNumber extends JPanel {
    private static final String PROTOCOL_NUMBER = "Номер протоколу";

    private final DialogChannel parent;

    private final JTextField protocolNumber;

    public PanelProtocolNumber(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        this.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(PROTOCOL_NUMBER);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);

        this.add(protocolNumber = new ProtocolNumberTextField(), new Cell(0,0));
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
            this.setHorizontalAlignment(JTextField.CENTER);

            this.addFocusListener(focusListener);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.panelSpecialCharacters.setFieldForInsert(ProtocolNumberTextField.this);
            }
        };
    }

    private static class Cell extends GridBagConstraints {
        private Cell(int x, int y){
            super();
            this.fill = BOTH;
            this.weightx = 1D;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
