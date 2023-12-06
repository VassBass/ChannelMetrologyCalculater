package service.channel.info.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.TitledTextField;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoCodePanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public class SwingChannelInfoCodePanel extends TitledTextField implements ChannelInfoCodePanel {
    private static final String CHANNEL_CODE = "channelCode";
    private static final String SEARCH_TOOLTIP = "searchTooltip";

    private static final Map<String, String> labels = Labels.getLabels(SwingChannelInfoCodePanel.class);

    private final RepositoryFactory repositoryFactory;

    public SwingChannelInfoCodePanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull ChannelInfoManager manager) {
        super(15, labels.get(CHANNEL_CODE), Color.BLACK);
        this.repositoryFactory = repositoryFactory;
        this.setToolTipText(Messages.getMessages(SwingChannelInfoCodePanel.class).get(SEARCH_TOOLTIP));
        this.setComponentPopupMenu(popupMenu(manager));
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
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
            if (Objects.nonNull(channelRepository)) {
                if (Objects.isNull(oldChannelCode)) {
                    if (!channelRepository.isExist(code)) {
                        this.setTitleColor(Color.BLACK);
                        return true;
                    }
                } else {
                    if (!channelRepository.isExist(oldChannelCode, code)) {
                        this.setTitleColor(Color.BLACK);
                        return true;
                    }
                }
            }
        }
        this.setTitleColor(Color.RED);
        return false;
    }

    private JPopupMenu popupMenu(final ChannelInfoManager manager) {
        Map<String, String> rootLabels = Labels.getRootLabels();

        JPopupMenu popupMenu = new JPopupMenu(rootLabels.get(RootLabelName.SEARCH));
        JMenuItem check = new JMenuItem(rootLabels.get(RootLabelName.SEARCH));
        check.addActionListener(e -> manager.searchChannelByCode());
        popupMenu.add(check);

        return popupMenu;
    }
}
