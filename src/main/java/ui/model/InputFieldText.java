package ui.model;

import ui.specialCharacters.PanelSpecialCharacters;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class InputFieldText extends InputField {

    private PanelSpecialCharacters panelSpecialCharacters;

    public InputFieldText() {
        super();

        this.addFocusListener(focusListener);
    }

    public InputFieldText(int textHorizontalAlignment) {
        super();
        setHorizontalAlignment(textHorizontalAlignment);

        this.addFocusListener(focusListener);
    }

    public InputFieldText(PanelSpecialCharacters panelSpecialCharacters) {
        super();
        this.panelSpecialCharacters = panelSpecialCharacters;

        this.addFocusListener(focusListener);
    }

    public InputFieldText(int textHorizontalAlignment, PanelSpecialCharacters panelSpecialCharacters) {
        super();
        setHorizontalAlignment(textHorizontalAlignment);
        this.panelSpecialCharacters = panelSpecialCharacters;

        this.addFocusListener(focusListener);
    }

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            if (panelSpecialCharacters != null) {
                panelSpecialCharacters.setFieldForInsert(InputFieldText.this);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (panelSpecialCharacters != null) {
                panelSpecialCharacters.setFieldForInsert(null);
            }
        }
    };
}
