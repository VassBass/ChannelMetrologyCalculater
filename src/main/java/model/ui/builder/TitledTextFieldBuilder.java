package model.ui.builder;

import model.ui.TitledTextField;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class TitledTextFieldBuilder {
    private static final String DEFAULT_TITLE_TEXT = EMPTY;

    private int columns = 10;
    private TitledBorder border;
    private String tooltipText;
    int borderLocation = -1;
    int textLocation = -1;

    public TitledTextFieldBuilder setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    public TitledTextFieldBuilder setTitle(String title) {
        border = BorderFactory.createTitledBorder(title);
        return this;
    }

    public TitledTextFieldBuilder setTooltipText(String text) {
        tooltipText = text;
        return this;
    }

    public TitledTextFieldBuilder setBorderLocation(int location) {
        borderLocation = location;
        return this;
    }

    public TitledTextFieldBuilder setTextLocation(int location) {
        textLocation = location;
        return this;
    }

    public TitledTextField build() {
        if (border == null) border = BorderFactory.createTitledBorder(DEFAULT_TITLE_TEXT);

        TitledTextField textField = new TitledTextField(columns);
        if (borderLocation >= 0) border.setTitleJustification(borderLocation);
        textField.setBorder(border);
        if (tooltipText != null) textField.setToolTipText(tooltipText);
        if (textLocation >= 0) textField.setHorizontalAlignment(textLocation);

        return textField;
    }
}
