package ui.channelInfo;

import backgroundTasks.CheckChannel;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

public class PanelCode extends JPanel {
    private static final String CODE = "*Код";

    private final DialogChannel parent;

    PanelCode(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBorder(BorderFactory.createTitledBorder(CODE));

        this.add(new CodeTextField());
    }

    private class CodeTextField extends JTextField {
        private static final String SEARCH = "Пошук";

        /**
         * TextField for channel code
         */
        private CodeTextField(){
            super(10);

            JPopupMenu codePopupMenu = new JPopupMenu(SEARCH);
            JMenuItem check = new JMenuItem(SEARCH);
            check.addActionListener(e -> new CheckChannel(parent, this.getText()).start());
            codePopupMenu.add(check);

            this.setComponentPopupMenu(codePopupMenu);

            this.addFocusListener(focusListener);
            this.getDocument().addDocumentListener(codeUpdate);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(CodeTextField.this);
            }
        };

        @SuppressWarnings("FieldCanBeLocal")
        private final DocumentListener codeUpdate = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (parent.oldChannel != null){
                    parent.removeButton.setEnabled(parent.oldChannel.getCode().equals(CodeTextField.this.getText()));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (parent.oldChannel != null){
                    parent.removeButton.setEnabled(parent.oldChannel.getCode().equals(CodeTextField.this.getText()));
                }
            }

            @Override public void changedUpdate(DocumentEvent e) {}
        };
    }
}
