package service.sensor_error.info.ui.swing;

import model.ui.TitledTextField;
import service.error_calculater.MxParserErrorCalculater;
import service.sensor_error.info.ui.SensorErrorInfoErrorPanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public class SwingSensorErrorInfoErrorPanel extends TitledTextField implements SensorErrorInfoErrorPanel {
    private static final String TITLE_TEXT = "Формула похибки ПВП";
    private static final String TOOLTIP_TEXT = "<html>"
            + "Щоб написати формулу користуйтеся:"
            + "<br>0...9, 0.1, 0,1 - Натуральні та дробні числа"
            + "<br>() - Дужки, для розстановки послідовності дій"
            + "<br>+, -, *, / - сума, різниця, множення, ділення"
            + "<br>R - Діапазон вимірювання вимірювального каналу"
            + "<br>convR - Діапазон вимірювання ПВП переконвертований під вимірювальну величину вимірювального каналу"
            + "<br>r - Діапазон вимірювання ПВП"
            + "<br>conv(...) - Число переконвертоване з вимірювальної величини ПВП до вимірювальної величини каналу"
            + "<br>-----------------------------------------------------------------------------------"
            + "<br>Приклад: ((0.005 * R) / r) + convR - conv(10)"
            + "<br>Дія №1 - 0.005 помножено на діапазон вимірювання вимірювального каналу(R)"
            + "<br>Дія №2 - Результат першої дії поділено на діапазон вимірювання ПВП(r)"
            + "<br>Дія №3 - До результату другої дії додати діапазон вимірювання ПВП переконвертований під вимірювальну"
            + "<br>величину вимірювального каналу(convR)"
            + "<br>Дія №4 - Від результату третьої дії відняти число 10 переконвертоване з вимірювальної величини ПВП"
            + "<br>до вимірювальної величини вимірювального каналу (conv(10))"
            + "</html>";

    public SwingSensorErrorInfoErrorPanel() {
        super(20, TITLE_TEXT, Color.BLACK);
        this.setToolTipText(TOOLTIP_TEXT);
    }

    @Override
    public void setErrorFormula(@Nonnull String errorFormula) {
        this.setText(errorFormula);
    }

    @Nullable
    @Override
    public String getErrorFormula() {
        String formula = this.getText();
        if (MxParserErrorCalculater.isFormulaValid(formula)) {
            this.setTitleColor(Color.BLACK);
            return formula;
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }
}
