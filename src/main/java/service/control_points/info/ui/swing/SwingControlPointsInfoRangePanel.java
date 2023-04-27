package service.control_points.info.ui.swing;

import model.dto.ControlPoints;
import model.ui.ButtonCell;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.control_points.info.ui.ControlPointsInfoRangePanel;
import util.ObjectHelper;
import util.StringHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

import static javax.swing.SwingConstants.RIGHT;
import static model.ui.ButtonCell.SIMPLE;
import static model.ui.builder.CellBuilder.VERTICAL;
import static util.StringHelper.FOR_LAST_ZERO;

public class SwingControlPointsInfoRangePanel extends TitledPanel implements ControlPointsInfoRangePanel {
    private static final String TITLE_TEXT = "Діапазон вимірюваннь";
    private static final String TOOLTIP_TEXT = "<html>" +
            "Контрольні точки будуть використовуватись якщо діапазон вимірюваннь" +
            "<br>буде співпадати з діапазоном перевіряємого каналу" +
            "</html>";

    private final DefaultTextField rangeMin;
    private final DefaultTextField rangeMax;

    public SwingControlPointsInfoRangePanel(@Nullable ControlPoints cp) {
        super(TITLE_TEXT, Color.BLACK);
        this.setToolTipText(TOOLTIP_TEXT);

        rangeMin = new DefaultTextField(5, RIGHT);
        rangeMax = new DefaultTextField(5);
        String[] range = executeRangeFromCPName(cp);
        rangeMin.setText(range[0]);
        rangeMax.setText(range[1]);

        this.add(rangeMin, new CellBuilder().fill(VERTICAL).x(0).build());
        this.add(new ButtonCell(SIMPLE, "..."), new CellBuilder().fill(VERTICAL).x(1).build());
        this.add(rangeMax, new CellBuilder().fill(VERTICAL).x(2).build());
    }


    @Override
    public void setRange(double r1, double r2) {
        rangeMin.setText(StringHelper.roundingDouble(Math.min(r1, r2), FOR_LAST_ZERO));
        rangeMax.setText(StringHelper.roundingDouble(Math.max(r1, r2), FOR_LAST_ZERO));
    }

    @Override
    public double getRangeMin() {
        String min = rangeMin.getText();
        if (StringHelper.isDouble(min)) {
            this.setTitleColor(Color.BLACK);
            return Double.parseDouble(min);
        } else {
            this.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    @Override
    public double getRangeMax() {
        String max = rangeMax.getText();
        if (StringHelper.isDouble(max)) {
            this.setTitleColor(Color.BLACK);
            return Double.parseDouble(max);
        } else {
            this.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    private String[] executeRangeFromCPName(@Nullable ControlPoints cp) {
        String[] defValues = new String[] { "0.00", "100.00" };
        if (Objects.isNull(cp)) return defValues;

        String name = cp.getName();
        int start = name.indexOf("[") + 1;
        int end = name.indexOf("]");
        String[] range = name.substring(start, end).split("\\.\\.\\.");
        if (range.length == 2) {
            return range;
        } else return defValues;
    }
}
