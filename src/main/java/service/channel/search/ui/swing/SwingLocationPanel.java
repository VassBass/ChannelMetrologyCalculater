package service.channel.search.ui.swing;

import model.ui.DefaultComboBox;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ui.LocationPanel;

import java.util.Arrays;

import static model.ui.builder.CellBuilder.NONE;

public class SwingLocationPanel extends TitledPanel implements LocationPanel {
    private static final String TITLE = "Місце знаходження каналу";
    private static final String ZONE_TOOLTIP = "При значенні \"-\" пошук ведеться по повній адресі каналу";
    private static final String LIST_ITEM_IGNORE = " - ";
    private static final String LIST_ITEM_ALL = "Все";
    private static final String LIST_ITEM_DEPARTMENT = "Цех";
    private static final String LIST_ITEM_AREA = "Дільниця";
    private static final String LIST_ITEM_PROCESS = "Секція, технологічна лінія, тощо";
    private static final String LIST_ITEM_INSTALLATION = "Установка";

    private final DefaultComboBox zone;
    private final DefaultTextField value;

    public SwingLocationPanel() {
        super(TITLE);

        zone = new DefaultComboBox(false);
        zone.setList(Arrays.asList(
                LIST_ITEM_IGNORE,
                LIST_ITEM_ALL,
                LIST_ITEM_DEPARTMENT,
                LIST_ITEM_AREA,
                LIST_ITEM_PROCESS,
                LIST_ITEM_INSTALLATION
        ));
        zone.setToolTipText(ZONE_TOOLTIP);

        value = new DefaultTextField(20);
        value.setEnabled(false);

        zone.addItemListener(e -> value.setEnabled(!zone.getSelectedString().equals(LIST_ITEM_IGNORE)));

        this.add(zone, new CellBuilder().fill(NONE).x(0).build());
        this.add(value, new CellBuilder().fill(NONE).x(1).build());
    }

    @Override
    public int getSearchingZone() {
        return zone.getSelectedIndex();
    }

    @Override
    public String getSearchingValue() {
        return value.getText();
    }
}
