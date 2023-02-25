package service.channel.ui.swing;

import model.ui.DefaultButton;
import model.ui.builder.CellBuilder;
import service.channel.ChannelManager;
import service.channel.ui.SearchPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

public class SwingSearchPanel extends JPanel implements SearchPanel {
    private static final String CODE = "Код";
    private static final String SEARCH = "Шукати";
    private static final String CANCEL = "Відмінити пошук";
    private static final String ADVANCED_SEARCH = "Розширений пошук";

    private static final String CODE_TOOLTIP_TEXT = "Код каналу для пошуку";
    private static final String SEARCH_PARAMS = "Параметри пошуку";

    private final JTextField codeField;
    private final JButton buttonSearch;

    public SwingSearchPanel(ChannelManager manager) {
        super(new GridBagLayout());

        codeField = new JTextField(15);
        TitledBorder codeBorder = BorderFactory.createTitledBorder(CODE);
        codeBorder.setTitleJustification(TitledBorder.CENTER);
        codeField.setBorder(codeBorder);
        codeField.setToolTipText(CODE_TOOLTIP_TEXT);
        codeField.setHorizontalAlignment(SwingConstants.CENTER);

        buttonSearch = new DefaultButton(SEARCH);
        JButton buttonAdvancedSearch = new DefaultButton(ADVANCED_SEARCH);

        buttonSearch.addActionListener(e -> {
            if (buttonSearch.getText().equals(SEARCH)) manager.search();
            if (buttonSearch.getText().equals(CANCEL)) manager.shutdownSearch();
        });

        buttonAdvancedSearch.addActionListener(e -> manager.advancedSearch());

        this.add(codeField, new CellBuilder().y(0).build());
        this.add(buttonSearch, new CellBuilder().y(1).build());
        this.add(buttonAdvancedSearch, new CellBuilder().y(2).build());
    }

    @Override
    public String getChannelCode() {
        return codeField.getText();
    }

    @Override
    public void setSearchInfo(Map<String, String> info) {
        codeField.setText(SEARCH_PARAMS);
        codeField.setEnabled(false);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            builder.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append('\n');
        }
        codeField.setToolTipText(builder.toString());

        buttonSearch.setText(CANCEL);
    }

    @Override
    public void resetPanel() {
        codeField.setEnabled(true);
        codeField.setText("");
        codeField.setToolTipText(CODE_TOOLTIP_TEXT);
        buttonSearch.setText(SEARCH);
    }
}
