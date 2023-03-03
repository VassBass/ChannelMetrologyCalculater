package service.channel.info.ui.swing;

import model.ui.DefaultLabel;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoAllowableErrorPanel;
import util.StringHelper;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoAllowableErrorPanel extends TitledPanel implements ChannelInfoAllowableErrorPanel {
    private static final String TITLE_TEXT = "Допустима похибка ВК";
    private static final String TOOLTIP_TEXT = "Допустима похибка вимірювального каналу";

    private final DefaultTextField errorPercent;
    private final DefaultTextField errorValue;
    private final DefaultLabel measurementValue;

    public SwingChannelInfoAllowableErrorPanel(final ChannelInfoManager manager) {
        super(TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);

        errorPercent = new DefaultTextField(3);
        errorPercent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isAllowableErrorPercentValid()) manager.changedAllowableErrorPercent();
            }
        });

        errorValue = new DefaultTextField(4);
        errorValue.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isAllowableErrorValueValid()) manager.changedAllowableErrorValue();
            }
        });

        measurementValue = new DefaultLabel();

        this.add(errorPercent, new CellBuilder().x(0).build());
        this.add(new DefaultLabel("% або "), new CellBuilder().x(1).build());
        this.add(errorValue, new CellBuilder().x(2).build());
        this.add(measurementValue, new CellBuilder().x(3).build());
    }

    @Override
    public void setAllowableErrorPercent(String allowableErrorPercent) {
        errorPercent.setText(allowableErrorPercent);
    }

    @Override
    public void setAllowableErrorValue(String allowableErrorValue) {
        errorValue.setText(allowableErrorValue);
    }

    @Override
    public void setMeasurementValue(String value) {
        measurementValue.setText(value);
    }

    @Override
    public String getAllowableErrorPercent() {
        return isAllowableErrorPercentValid() ?
                errorPercent.getText() :
                EMPTY;
    }

    @Override
    public String getAllowableErrorValue() {
        return isAllowableErrorValueValid() ?
                errorValue.getText() :
                EMPTY;
    }

    @Override
    public boolean isAllowableErrorPercentValid() {
        return StringHelper.isDouble(errorPercent.getText());
    }

    @Override
    public boolean isAllowableErrorValueValid() {
        return StringHelper.isDouble(errorValue.getText());
    }
}
