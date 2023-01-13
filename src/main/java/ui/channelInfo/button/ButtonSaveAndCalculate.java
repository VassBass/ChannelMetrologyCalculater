package ui.channelInfo.button;

import application.Application;
import model.Channel;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.channel.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.calculate.start.DialogCalculateStart;
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

public class ButtonSaveAndCalculate extends DefaultButton {
    private static final String SAVE_AND_CALCULATE = "Зберегти та розрахувати (Ctrl + Enter)";

    private final DialogChannel parent;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public ButtonSaveAndCalculate(@Nonnull DialogChannel parent){
        super(SAVE_AND_CALCULATE);
        this.parent = parent;

        this.addActionListener(new Click());
    }

    private final class Click extends SwingWorker<Boolean, Void> implements ActionListener {
        private Channel channel;

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
                channel = c.get();

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
                    EventQueue.invokeLater(() -> new DialogCalculateStart(MainScreen.getInstance(), channel, null).setVisible(true));
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
