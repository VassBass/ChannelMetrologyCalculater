package service.channel.info.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoRangePanel;
import util.StringHelper;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.RIGHT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoRangePanel extends TitledPanel implements ChannelInfoRangePanel {
    private static final String CHANNEL_RANGE_TOOLTIP = "channelRangeTooltip";

    private static final Map<String, String> labels = Labels.getRootLabels();

    private final DefaultTextField rangeMin, rangeMax;
    private final DefaultLabel measurementValue;

    public SwingChannelInfoRangePanel(final ChannelInfoManager manager) {
        super(labels.get(RootLabelName.RANGE + Labels.SPACE + labels.get(RootLabelName.CHANNEL_SHORT)), Color.BLACK);
        this.setToolTipText(Messages.getMessages(SwingChannelInfoRangePanel.class).get(CHANNEL_RANGE_TOOLTIP));

        FocusListener textFocusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isRangeValid()) manager.changedChannelRange();
            }
        };

        rangeMin = new DefaultTextField(4, null, RIGHT).setFocusListener(textFocusListener);
        rangeMax = new DefaultTextField(4).setFocusListener(textFocusListener);

        measurementValue = new DefaultLabel();
        DefaultLabel separator = new DefaultLabel("...", CENTER);

        this.add(rangeMin, new CellBuilder().x(0).build());
        this.add(separator, new CellBuilder().x(1).build());
        this.add(rangeMax, new CellBuilder().x(2).build());
        this.add(measurementValue, new CellBuilder().x(3).build());
    }

    @Override
    public void setMeasurementValue(String value) {
        measurementValue.setText(value);
    }

    @Override
    public void setRangeMin(String min) {
        rangeMin.setText(min);
    }

    @Override
    public void setRangeMax(String max) {
        rangeMax.setText(max);
    }

    @Override
    public String getRangeMin() {
        String min = rangeMin.getText();
        return StringHelper.isDouble(min) ?
                min :
                EMPTY;
    }

    @Override
    public String getRangeMax() {
        String max = rangeMax.getText();
        return StringHelper.isDouble(max) ?
                max :
                EMPTY;
    }

    @Override
    public String getMeasurementValue() {
        return measurementValue.getText();
    }

    @Override
    public boolean isRangeValid() {
        String min = rangeMin.getText();
        String max = rangeMax.getText();
        if (min.isEmpty() || max.isEmpty()) {
            this.setTitleColor(Color.RED);
            return false;
        }

        if (Double.parseDouble(min) > Double.parseDouble(max)) {
            this.setTitleColor(Color.RED);
            return false;
        } else {
            this.setTitleColor(Color.BLACK);
            return true;
        }
    }
}
