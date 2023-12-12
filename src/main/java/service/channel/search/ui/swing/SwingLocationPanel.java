package service.channel.search.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultComboBox;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ui.LocationPanel;

import java.util.Arrays;
import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingLocationPanel extends TitledPanel implements LocationPanel {
    private static final String CHANNEL_LOCATION = "channelLocation";

    private final DefaultComboBox zone;
    private final DefaultTextField value;

    public SwingLocationPanel() {
        super(Labels.getLabels(SwingLocationPanel.class).get(CHANNEL_LOCATION));
        Map<String, String> rootLabels = Labels.getRootLabels();

        zone = new DefaultComboBox(false);
        zone.setList(Arrays.asList(
                Labels.SPACE_DASH_SPACE,
                rootLabels.get(RootLabelName.ALL),
                rootLabels.get(RootLabelName.DEPARTMENT),
                rootLabels.get(RootLabelName.AREA),
                rootLabels.get(RootLabelName.PROCESS),
                rootLabels.get(RootLabelName.INSTALLATION)
        ));

        value = new DefaultTextField(20);
        value.setEnabled(false);

        zone.addItemListener(e -> value.setEnabled(!zone.getSelectedString().equals(Labels.SPACE_DASH_SPACE)));

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
