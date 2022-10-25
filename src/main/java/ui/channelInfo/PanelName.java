package ui.channelInfo;

import application.Application;

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

    PanelName(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBackground(Color.WHITE);
        this.setBorder(border = BorderFactory.createTitledBorder(NAME));

        this.add(name = new NameTextField());
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
                parent.specialCharactersPanel.setFieldForInsert(NameTextField.this);
            }
        };
    }
}
