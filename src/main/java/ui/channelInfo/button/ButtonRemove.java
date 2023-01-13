package ui.channelInfo.button;

import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.channel.ChannelRepositorySQLite;
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
import java.util.concurrent.ExecutionException;

public class ButtonRemove extends DefaultButton {
    private static final String REMOVE = "Видалити";

    private final DialogChannel parent;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public ButtonRemove(@Nonnull DialogChannel parent) {
        super(REMOVE);
        this.parent = parent;

        this.addActionListener(new Click());
    }

    private class Click extends SwingWorker<Boolean, Void> implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (parent.oldChannel != null){
                EventQueue.invokeLater(() -> {
                    int result = JOptionPane.showConfirmDialog(
                            parent,
                            "Видалити канал \"" + parent.oldChannel.getName() + "\"?",
                            parent.oldChannel.getCode(),
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    if (result == 0) {
                        EventQueue.invokeLater(() -> parent.dialogLoading.setVisible(true));
                        this.execute();
                    }
                });
            }
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            if (channelRepository.remove(parent.oldChannel)) {
                if (ChannelSorter.getInstance().isOn()){
                    MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getCurrent());
                }else {
                    MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
                }
                return true;
            } else return false;
        }

        @Override
        protected void done() {
            parent.dialogLoading.dispose();
            parent.dispose();
            try {
                if (!get()) showErrorMessage();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                showErrorMessage();
            }
        }

        private void showErrorMessage() {
            EventQueue.invokeLater(() -> {
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
