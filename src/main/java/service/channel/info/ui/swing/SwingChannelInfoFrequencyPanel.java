package service.channel.info.ui.swing;

import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoFrequencyPanel;
import util.StringHelper;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static javax.swing.SwingConstants.RIGHT;

public class SwingChannelInfoFrequencyPanel extends TitledPanel implements ChannelInfoFrequencyPanel {
    private static final String TITLE_TEXT = "Міжконтрольний інтервал";
    private static final String VALUE_TEXT = "р.";
    private static final String TOOLTIP_TEXT = "Пів року = 0.51; місяць = 0.083 (приблизно)";

    private final ChannelInfoManager manager;
    private final DefaultTextField frequency;

    public SwingChannelInfoFrequencyPanel(ChannelInfoManager manager) {
        super(TITLE_TEXT, Color.BLACK);
        this.manager = manager;
        this.setToolTipText(TOOLTIP_TEXT);

        FocusListener frequencyFocusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isFrequencyValid()) manager.changeDateOrFrequency();
            }
        };

        frequency = new DefaultTextField(3, null, RIGHT).setFocusListener(frequencyFocusListener);

        DefaultLabel frequencyValue = new DefaultLabel(VALUE_TEXT);

        this.add(frequency, new CellBuilder().x(0).build());
        this.add(frequencyValue, new CellBuilder().x(1).build());
    }

    @Override
    public void setFrequency(String frequency) {
        if (StringHelper.isDouble(frequency)) {
            this.frequency.setText(frequency);
            manager.changeDateOrFrequency();
        }
    }

    @Override
    public String getFrequency() {
        return frequency.getText();
    }

    @Override
    public boolean isFrequencyValid() {
        String frequency = this.frequency.getText();
        if (frequency.length() > 0) {
            if (StringHelper.isDouble(frequency)) {
                this.setTitleColor(Color.BLACK);
                return true;
            }
        }

        this.setTitleColor(Color.RED);
        return false;
    }
}
