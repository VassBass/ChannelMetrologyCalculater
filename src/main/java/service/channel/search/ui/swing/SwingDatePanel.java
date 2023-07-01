package service.channel.search.ui.swing;

import model.ui.IntegerTextField;
import model.ui.TitledComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ui.DatePanel;

import javax.swing.*;
import java.util.Arrays;

import static model.ui.builder.CellBuilder.NONE;

public class SwingDatePanel extends TitledPanel implements DatePanel {
    private static final String TITLE = "Дата останнього КМХ";
    private static final String YEAR_TOOLTIP_TEXT = "Щоб проігнорувати в пошуку рік введіть в поле вводу \"0\"";
    private static final String MONTH_TITLE = "Місяць";
    private static final String YEAR_TITLE = "Рік";

    private final TitledComboBox month;
    private final IntegerTextField year;

    public SwingDatePanel() {
        super(TITLE);

        month = new TitledComboBox(false, MONTH_TITLE);
        month.setList(Arrays.asList(
                " - ",
                "Січень",
                "Лютий",
                "Березень",
                "Квітень",
                "Травень",
                "Червень",
                "Липень",
                "Серпень",
                "Вересень",
                "Жовтень",
                "Листопад",
                "Грудень"
        ));

        year = new IntegerTextField(4, YEAR_TOOLTIP_TEXT);
        year.setText("0");
        year.setBorder(BorderFactory.createTitledBorder(YEAR_TITLE));

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
