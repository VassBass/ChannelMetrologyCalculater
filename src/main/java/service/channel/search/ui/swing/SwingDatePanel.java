package service.channel.search.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.IntegerTextField;
import model.ui.TitledComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ui.DatePanel;

import javax.swing.*;
import java.util.Arrays;
import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingDatePanel extends TitledPanel implements DatePanel {
    private static final String YEAR_IGNORING = "yearIgnoring";

    private final TitledComboBox month;
    private final IntegerTextField year;

    private static final Map<String, String> labels = Labels.getRootLabels();

    public SwingDatePanel() {
        super(labels.get(RootLabelName.LAST_CHECK_DATE));
        Map<String, String> messages = Messages.getMessages(SwingDatePanel.class);

        month = new TitledComboBox(false, labels.get(RootLabelName.MONTH));
        month.setList(Arrays.asList(
                Labels.SPACE_DASH_SPACE,
                labels.get(RootLabelName.JANUARY),
                labels.get(RootLabelName.FEBRUARY),
                labels.get(RootLabelName.MARCH),
                labels.get(RootLabelName.APRIL),
                labels.get(RootLabelName.MAY),
                labels.get(RootLabelName.JUNE),
                labels.get(RootLabelName.JULY),
                labels.get(RootLabelName.AUGUST),
                labels.get(RootLabelName.SEPTEMBER),
                labels.get(RootLabelName.OCTOBER),
                labels.get(RootLabelName.NOVEMBER),
                labels.get(RootLabelName.DECEMBER)
        ));

        year = new IntegerTextField(4, messages.get(YEAR_IGNORING));
        year.setText(Labels.ZERRO);
        year.setBorder(BorderFactory.createTitledBorder(labels.get(RootLabelName.YEAR)));

        this.add(month, new CellBuilder().fill(NONE).x(0).build());
        this.add(year, new CellBuilder().fill(NONE).x(1).build());
    }

    @Override
    public int getMonth() {
        return month.getSelectedIndex();
    }

    @Override
    public int getYear() {
        return year.getInput();
    }
}
