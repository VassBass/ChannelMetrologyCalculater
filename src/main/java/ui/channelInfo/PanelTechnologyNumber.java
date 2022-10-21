package ui.channelInfo;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PanelTechnologyNumber extends JPanel {
    private static final String TECHNOLOGY_NUMBER = "*Технологічний номер";

    private final DialogChannel parent;

    PanelTechnologyNumber(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        this.setBorder(BorderFactory.createTitledBorder(TECHNOLOGY_NUMBER));

        this.add(new TextTechnologyNumber());
    }

    /**
     * TextField for channel technology number
     */
    private class TextTechnologyNumber extends JTextField {

        private TextTechnologyNumber(){
            super(10);

            this.addFocusListener(focusListener);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(TextTechnologyNumber.this);
            }
        };
    }
}
