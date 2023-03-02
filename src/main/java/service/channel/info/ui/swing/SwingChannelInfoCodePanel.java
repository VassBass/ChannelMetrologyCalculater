package service.channel.info.ui.swing;

import model.dto.Channel;
import model.ui.TitledTextField;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoCodePanel;
import repository.repos.channel.ChannelRepository;

import javax.swing.*;

public class SwingChannelInfoCodePanel extends TitledTextField implements ChannelInfoCodePanel {
    private static final String TITLE_TEXT = "Код ВК";
    private static final String TOOLTIP_TEXT = "Натисніть праву кнопку миші щоб почати пошук ВК по коду.";
    private static final String SEARCH_TEXT = "Пошук";

    private static final String CHANNEL_NOT_FOUND_MESSAGE = "Канал з данним кодом не знайдений.";
    private static final String CHANNEL_FOUND_MESSAGE = "Канал знайдено. Відкрити інформацію про канал у данному вікні?";

    public SwingChannelInfoCodePanel(SwingChannelInfoDialog owner,
                                     ChannelInfoManager manager,
                                     ChannelRepository channelRepository) {
        super(TITLE_TEXT, 15);
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
