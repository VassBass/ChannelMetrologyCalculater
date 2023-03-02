package service.channel.info.ui.swing;

import model.dto.Channel;
import model.ui.TitledTextField;
import repository.repos.channel.ChannelRepository;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoCodePanel;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class SwingChannelInfoCodePanel extends TitledTextField implements ChannelInfoCodePanel {
    private static final String TITLE_TEXT = "Код ВК";
    private static final String TOOLTIP_TEXT = "Натисніть праву кнопку миші щоб почати пошук ВК по коду.";
    private static final String SEARCH_TEXT = "Пошук";

    private static final String CHANNEL_NOT_FOUND_MESSAGE = "Канал з данним кодом не знайдений.";
    private static final String CHANNEL_FOUND_MESSAGE = "Канал знайдено. Відкрити інформацію про канал у данному вікні?";

    private final ChannelRepository channelRepository;

    public SwingChannelInfoCodePanel(SwingChannelInfoDialog owner,
                                     ChannelInfoManager manager,
                                     ChannelRepository channelRepository) {
        super(TITLE_TEXT, 15);
        this.channelRepository = channelRepository;
        this.setToolTipText(TOOLTIP_TEXT);
        this.setComponentPopupMenu(popupMenu(channelRepository, manager, owner));
    }

    @Override
    public String getCode() {
        return this.getText();
    }

    @Override
    public void setCode(String code) {
        this.setText(code);
    }

    /**
     * Checks is code in text field valid
     * @param oldChannelCode If the value is {@code null}, it means that the service is started to create a new channel.
     *                      Otherwise - to change the existing one.
     * @return true if the code field is not empty or if the channel code is valid for the implemented repository
     */
    @Override
    public boolean isCodeValid(@Nullable String oldChannelCode) {
        String code = getCode();
        if (code.length() > 0) {
            if (oldChannelCode == null) {
                if (channelRepository.isExist(code)) {
                    this.setTitleColor(Color.BLACK);
                    return true;
                }
            } else {
                if (channelRepository.isExist(oldChannelCode, code)) {
                    this.setTitleColor(Color.BLACK);
                    return true;
                }
            }
        }

        this.setTitleColor(Color.RED);
        return false;
    }

    private JPopupMenu popupMenu(final ChannelRepository repository,
                                 final ChannelInfoManager manager,
                                 final SwingChannelInfoDialog owner) {
        JPopupMenu popupMenu = new JPopupMenu(SEARCH_TEXT);
        JMenuItem check = new JMenuItem(SEARCH_TEXT);
        check.addActionListener(e -> {
            Channel channel = repository.get(this.getCode());
            if (channel == null) {
                JOptionPane.showMessageDialog(owner, CHANNEL_NOT_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.INFORMATION_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(owner, CHANNEL_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) manager.setChannelInfo(channel);
            }
        });
        popupMenu.add(check);

        return popupMenu;
    }


}
