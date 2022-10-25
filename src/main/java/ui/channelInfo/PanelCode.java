package ui.channelInfo;

import backgroundTasks.CheckChannel;
import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class PanelCode extends JPanel {
    private static final String CODE = "*Код";

    private final DialogChannel parent;

    private final TitledBorder border;

    private final JTextField code;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    PanelCode(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBackground(Color.WHITE);
        this.setBorder(border = BorderFactory.createTitledBorder(CODE));

        this.add(code = new CodeTextField());
    }

    public boolean isCodeAvailable(@Nullable Channel oldChannel){
        Optional<Channel> c = channelRepository.get(code.getText());

        if (this.code.getText().length() == 0) {
            border.setTitleColor(Color.RED);
            return false;
        }else if (oldChannel == null) {
            if (c.isPresent()){
                border.setTitleColor(Color.RED);
                EventQueue.invokeLater(() -> new ChannelExistsDialog(parent, c.get()));
                return false;
            } else {
                border.setTitleColor(Color.BLACK);
                return true;
            }
        }else {
            if (channelRepository.isExist(oldChannel.getCode(), code.getText())) {
                c.ifPresent(channel -> {
                    border.setTitleColor(Color.RED);
                    EventQueue.invokeLater(() -> new ChannelExistsDialog(parent, channel));
                });
                return false;
            }else {
                border.setTitleColor(Color.BLACK);
                return true;
            }
        }
    }

    public String getCode(){
        return code.getText();
    }

    public void setCode(@Nonnull String code) {
        this.code.setText(code);
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        code.addKeyListener(l);
    }

    private class CodeTextField extends JTextField {
        private static final String SEARCH = "Пошук";

        /**
         * TextField for channel code
         */
        private CodeTextField(){
            super(20);

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
                    parent.buttonRemove.setEnabled(parent.oldChannel.getCode().equals(CodeTextField.this.getText()));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (parent.oldChannel != null){
                    parent.buttonRemove.setEnabled(parent.oldChannel.getCode().equals(CodeTextField.this.getText()));
                }
            }

            @Override public void changedUpdate(DocumentEvent e) {}
        };
    }
}
