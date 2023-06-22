package service.channel.search.ui.swing;

import model.ui.DefaultComboBox;
import model.ui.IntegerTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ui.DatePanel;

import java.util.Arrays;

public class SwingDatePanel extends TitledPanel implements DatePanel {
    private static final String TITLE = "Дата останнього КМХ";
    private static final String YEAR_TOOLTIP_TEXT = "Щоб проігнорувати в пошуку рік введіть в поле вводу \"0\"";

    private final DefaultComboBox month;
    private final IntegerTextField year;

    public SwingDatePanel() {
        super(TITLE);

        month = new DefaultComboBox(false);
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

        this.add(month, new CellBuilder().x(0).build());
        this.add(year, new CellBuilder().x(1).build());
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
