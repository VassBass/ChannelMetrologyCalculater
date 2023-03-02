package service.channel.info.ui.swing;

import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ui.ChannelInfoDatePanel;
import util.DateHelper;

import java.awt.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoDatePanel extends TitledPanel implements ChannelInfoDatePanel {
    private static final String TITLE_TEXT = "Дата останньої перевірки";
    private static final String DAY_TOOLTIP_TEXT = "День";
    private static final String MONTH_TOOLTIP_TEXT = "Місяць";
    private static final String YEAR_TOOLTIP_TEXT = "Рік";


    private final DefaultTextField dayField;
    private final DefaultTextField monthField;
    private final DefaultTextField yearField;

    public SwingChannelInfoDatePanel() {
        super(TITLE_TEXT);

        dayField = new DefaultTextField(2, DAY_TOOLTIP_TEXT);
        monthField = new DefaultTextField(2,MONTH_TOOLTIP_TEXT);
        yearField = new DefaultTextField(4, YEAR_TOOLTIP_TEXT);

        this.add(dayField, new CellBuilder().x(0).build());
        this.add(new DefaultLabel("."), new CellBuilder().x(1).build());
        this.add(monthField, new CellBuilder().x(2).build());
        this.add(new DefaultLabel("."), new CellBuilder().x(3).build());
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
