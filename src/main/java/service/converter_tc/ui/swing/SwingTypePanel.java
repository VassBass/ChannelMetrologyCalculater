package service.converter_tc.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.DefaultComboBox;
import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.converter_tc.model.Type;
import service.converter_tc.ui.TypePanel;
import util.StringHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

import static java.util.Objects.nonNull;

public class SwingTypePanel extends TitledPanel implements TypePanel {
    private static final String TYPE_TC = "typeTC";
    private static final String TOOLTIP = "tooltip";
    private static final String DEFAULT_VALUE0 = "100";

    private final DefaultComboBox type;
    private final DefaultTextField value0;

    public SwingTypePanel() {
        super(Labels.getLabels(SwingTypePanel.class).get(TYPE_TC), Color.BLACK);

        type = new DefaultComboBox(false);
        type.setList(Arrays.asList(
                Type.Cu.name,
                Type.Pt.name,
                Type.Pl.name
        ));
        value0 = new DefaultTextField(5, DEFAULT_VALUE0, null);
        DefaultLabel tooltipText = new DefaultLabel(Messages.getMessages(SwingTypePanel.class).get(TOOLTIP));

        this.add(type, new CellBuilder().x(0).fill(CellBuilder.NONE).build());
        this.add(tooltipText, new CellBuilder().x(1).fill(CellBuilder.NONE).build());
        this.add(value0, new CellBuilder().x(2).fill(CellBuilder.NONE).build());
    }

    @Override
    @Nullable
    public Map.Entry<Type, Double> getType() {
        String val0 = value0.getText().replaceAll(Labels.COMMA, Labels.DOT);
        Type type = Type.getType(this.type.getSelectedString());

        if (StringHelper.isDouble(val0) && nonNull(type)) {
            this.setTitleColor(Color.BLACK);
            return new AbstractMap.SimpleEntry<>(type, Double.parseDouble(val0));
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }
}
