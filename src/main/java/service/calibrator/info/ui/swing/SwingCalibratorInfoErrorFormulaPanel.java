package service.calibrator.info.ui.swing;

import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoErrorFormulaPanel;
import service.error_calculater.MxParserErrorCalculater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public class SwingCalibratorInfoErrorFormulaPanel extends TitledTextField implements CalibratorInfoErrorFormulaPanel {
    private static final String TITLE_TEXT = "Формула розрахунку похибки калібратора";
    private static final String TOOLTIP_TEXT = "<html>"
            + "Щоб написати формулу користуйтеся:"
            + "<br>0...9, 0.1, 0,1 - Натуральні та дробні числа"
            + "<br>() - Дужки, для розстановки послідовності дій"
            + "<br>+, -, *, / - сума, різниця, множення, ділення"
            + "<br>R - Діапазон вимірювання вимірювального каналу"
            + "<br>convR - Діапазон вимірювання калібратора переконвертований під вимірювальну величину вимірювального каналу"
            + "<br>r - Діапазон вимірювання калібратора"
            + "<br>conv(...) - Число переконвертоване з вимірювальної величини калібратора до вимірювальної величини каналу"
            + "<br>-----------------------------------------------------------------------------------"
            + "<br>Приклад: ((0.005 * R) / r) + convR - conv(10)"
            + "<br>Дія №1 - 0.005 помножено на діапазон вимірювання вимірювального каналу(R)"
            + "<br>Дія №2 - Результат першої дії поділено на діапазон вимірювання калібратора(r)"
            + "<br>Дія №3 - До результату другої дії додати діапазон вимірювання калібратора переконвертований під вимірювальну"
            + "<br>величину вимірювального каналу(convR)"
            + "<br>Дія №4 - Від результату третьої дії відняти число 10 переконвертоване з вимірювальної величини калібратора"
            + "<br>до вимірювальної величини вимірювального каналу (conv(10))"
            + "</html>";

    public SwingCalibratorInfoErrorFormulaPanel() {
        super(20, TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);
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

    @Override
    public void setErrorFormula(@Nonnull String value) {
        this.setText(value);
    }
}
