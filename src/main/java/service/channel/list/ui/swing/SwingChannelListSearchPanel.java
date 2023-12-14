package service.channel.list.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.TitledTextField;
import model.ui.builder.CellBuilder;
import model.ui.builder.TitledTextFieldBuilder;
import service.channel.list.ChannelListManager;
import service.channel.list.ui.ChannelListSearchPanel;

import javax.swing.*;
import java.util.Map;

public class SwingChannelListSearchPanel extends DefaultPanel implements ChannelListSearchPanel {
    private static final String CHANNEL_CODE_FOR_SEARCH = "channelCodeForSearch";

    private final JTextField codeField;

    public SwingChannelListSearchPanel(ChannelListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();
        Map<String, String> messages = Messages.getMessages(SwingChannelListSearchPanel.class);

        codeField = new TitledTextFieldBuilder()
                .setColumns(15)
                .setTitle(labels.get(RootLabelName.CODE))
                .setTitleLocation(TitledTextField.TITLE_TOP_CENTER)
                .setTooltipText(messages.get(CHANNEL_CODE_FOR_SEARCH))
                .setTextLocation(TitledTextField.TEXT_CENTER)
                .build();

        JButton buttonSearch = new DefaultButton(labels.get(RootLabelName.START_SEARCH));
        JButton buttonAdvancedSearch = new DefaultButton(labels.get(RootLabelName.ADVANCED_SEARCH));

        buttonSearch.addActionListener(e -> manager.search());

        buttonAdvancedSearch.addActionListener(e -> manager.advancedSearch());

        this.add(codeField, new CellBuilder().y(0).build());
        this.add(buttonSearch, new CellBuilder().y(1).build());
        this.add(buttonAdvancedSearch, new CellBuilder().y(2).build());
    }

    @Override
    public String getChannelCode() {
        return codeField.getText();
    }
}
