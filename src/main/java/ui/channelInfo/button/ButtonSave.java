package ui.channelInfo.button;

import application.Application;
import model.Channel;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.channelInfo.DialogChannel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ButtonSave extends DefaultButton {
    private static final String SAVE = "Зберегти (Alt + Enter)";

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    private final DialogChannel parent;

    public ButtonSave(@Nonnull DialogChannel parent){
        super(SAVE);
        this.parent = parent;

        this.addActionListener(new Click());
    }

    private final class Click extends SwingWorker<Boolean, Void> implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.panelSpecialCharacters.setFieldForInsert(null);
            if (parent.isFieldsAreFilled()) {
                EventQueue.invokeLater(() -> parent.dialogLoading.setVisible(true));
                this.execute();
            }
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            Optional<Channel> c = parent.getChannel();
            if (c.isPresent()) {
                Channel channel = c.get();

                if (parent.oldChannel == null) {
                    channelRepository.add(channel);
                } else {
                    channelRepository.set(parent.oldChannel, channel);
                }

                if (ChannelSorter.getInstance().isOn()) {
                    MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getCurrent());
                } else {
                    MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
                }
                Application.putHint(parent.panelName.getChannelName());
                return true;
            } else return false;
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    parent.dialogLoading.dispose();
                    parent.dispose();
                } else {
                    showErrorMessage();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                showErrorMessage();
            }
        }

        private void showErrorMessage() {
            EventQueue.invokeLater(() -> {
                parent.dialogLoading.setVisible(false);
                JOptionPane.showMessageDialog(
                        parent,
                        "Виникла помилка. Будь ласка спробуйте ще раз",
                        "Помилка",
                        JOptionPane.ERROR_MESSAGE
                );
            });
        }
    }
}
