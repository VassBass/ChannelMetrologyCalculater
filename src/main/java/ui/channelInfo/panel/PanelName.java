package ui.channelInfo.panel;

import application.Application;
import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class PanelName extends JPanel {
    private static final String NAME = "*Назва";

    private final DialogChannel parent;

    private final JTextField name;
    private final TitledBorder border;

    public PanelName(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        this.setBackground(Color.WHITE);
        this.setBorder(border = BorderFactory.createTitledBorder(NAME));
        border.setTitleJustification(TitledBorder.CENTER);

        this.add(name = new NameTextField(), new Cell(0,0));
    }

    public boolean isNameAvailable(){
        if (name.getText().length() == 0){
            border.setTitleColor(Color.RED);
            return false;
        }else {
            border.setTitleColor(Color.BLACK);
            return true;
        }
    }

    public String getChannelName(){
        return name.getText();
    }

    public void setChannelName(@Nonnull String name) {
        this.name.setText(name);
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        name.addKeyListener(l);
    }

    /**
     * TextField for channel name
     */
    private class NameTextField extends JTextField {
        private static final String INSERT = "Вставка";

        private NameTextField(){
            super(10);
            this.setHorizontalAlignment(JTextField.CENTER);

            JPopupMenu namePopupMenu = new JPopupMenu(INSERT);
            this.setComponentPopupMenu(namePopupMenu);
            String[] hints = Application.getHints();
            for (String hint : hints) {
                if (hint == null) continue;

                JMenuItem type = new JMenuItem(hint);
                type.addActionListener(e -> this.setText(type.getText()));
                namePopupMenu.add(type);
            }

            this.addFocusListener(focusListener);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.panelSpecialCharacters.setFieldForInsert(NameTextField.this);
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
