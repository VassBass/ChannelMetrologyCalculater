package service.channel.info.ui.swing;

import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoDatePanel;
import util.DateHelper;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static model.ui.DefaultLabel.CENTER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoDatePanel extends TitledPanel implements ChannelInfoDatePanel {
    private static final String TITLE_TEXT = "Дата останньої перевірки";
    private static final String DAY_TOOLTIP_TEXT = "День";
    private static final String MONTH_TOOLTIP_TEXT = "Місяць";
    private static final String YEAR_TOOLTIP_TEXT = "Рік";

    private final DefaultTextField dayField;
    private final DefaultTextField monthField;
    private final DefaultTextField yearField;

    public SwingChannelInfoDatePanel(ChannelInfoManager manager) {
        super(TITLE_TEXT, Color.BLACK);

        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isDateValid()) manager.changeDateOrFrequency();
            }
        };

        dayField = new DefaultTextField(2, DAY_TOOLTIP_TEXT, CENTER).setFocusListener(focusListener);
        monthField = new DefaultTextField(2, MONTH_TOOLTIP_TEXT, CENTER).setFocusListener(focusListener);
        yearField = new DefaultTextField(4, YEAR_TOOLTIP_TEXT, CENTER).setFocusListener(focusListener);
        DefaultLabel dot1 = new DefaultLabel(".", CENTER);
        DefaultLabel dot2 = new DefaultLabel(".", CENTER);

        this.add(dayField, new CellBuilder().x(0).build());
        this.add(dot1, new CellBuilder().x(1).build());
        this.add(monthField, new CellBuilder().x(2).build());
        this.add(dot2, new CellBuilder().x(3).build());
        this.add(yearField, new CellBuilder().x(4).build());
    }

    @Override
    public void setDate(String date) {
        String[] splttedDate = DateHelper.getSplittedDate(date);
        dayField.setText(splttedDate[0]);
        monthField.setText(splttedDate[1]);
        yearField.setText(splttedDate[2]);
    }

    @Override
    public String getDate() {
        String date = String.format("%s.%s.%s",
                dayField.getText(),
                monthField.getText(),
                yearField.getText());
        return DateHelper.isDateValid(date) ? date : EMPTY;
    }

    @Override
    public boolean isDateValid() {
        String date = String.format("%s.%s.%s",
                dayField.getText(),
                monthField.getText(),
                yearField.getText());
        if (DateHelper.isDateValid(date)) {
            this.setTitleColor(Color.BLACK);
            return true;
        }

        this.setTitleColor(Color.RED);
        return false;
    }

}
