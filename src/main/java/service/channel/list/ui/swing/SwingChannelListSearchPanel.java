package service.channel.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.TitledTextField;
import model.ui.builder.CellBuilder;
import model.ui.builder.TitledTextFieldBuilder;
import service.channel.list.ChannelListManager;
import service.channel.list.ui.ChannelListSearchPanel;

import javax.swing.*;

public class SwingChannelListSearchPanel extends DefaultPanel implements ChannelListSearchPanel {
    private static final String CODE = "Код";
    private static final String SEARCH = "Шукати";
    private static final String ADVANCED_SEARCH = "Розширений пошук";

    private static final String CODE_TOOLTIP_TEXT = "Код каналу для пошуку";

    private final JTextField codeField;

    public SwingChannelListSearchPanel(ChannelListManager manager) {
        super();

        codeField = new TitledTextFieldBuilder()
                .setColumns(15)
                .setTitle(CODE)
                .setTitleLocation(TitledTextField.TITLE_TOP_CENTER)
                .setTooltipText(CODE_TOOLTIP_TEXT)
                .setTextLocation(TitledTextField.TEXT_CENTER)
                .build();

        JButton buttonSearch = new DefaultButton(SEARCH);
        JButton buttonAdvancedSearch = new DefaultButton(ADVANCED_SEARCH);

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
