package ui.channelInfo;

import application.Application;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.*;

public class PanelName extends JPanel {
    private static final String NAME = "*Назва";

    private final DialogChannel parent;

    PanelName(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBorder(BorderFactory.createTitledBorder(NAME));

        this.add(new NameTextField());
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
