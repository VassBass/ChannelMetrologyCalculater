package service.method_name.ui.swing;

import model.ui.DefaultTextField;
import service.method_name.ui.MethodNamePanel;

import javax.annotation.Nonnull;

public class SwingMethodNamePanel extends DefaultTextField implements MethodNamePanel {

    public SwingMethodNamePanel() {
        super(20);
    }

    @Override
    public void setMethodName(@Nonnull String name) {
        this.setText(name);
    }

    @Override
    public String getMethodName() {
        return this.getText();
    }
}
