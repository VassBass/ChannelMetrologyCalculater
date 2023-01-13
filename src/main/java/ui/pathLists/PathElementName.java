package ui.pathLists;

import model.Model;
import repository.PathElementRepository;
import service.repository.repos.area.AreaRepositorySQLite;
import service.repository.repos.department.DepartmentRepositorySQLite;
import service.repository.repos.installation.InstallationRepositorySQLite;
import service.repository.repos.process.ProcessRepositorySQLite;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.UI_Constants.POINT_CENTER;

public class PathElementName extends JDialog {
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";
    public static final String DEPARTMENT_STRING = "Цех";
    public static final String AREA_STRING = "Ділянка";
    public static final String PROCESS_STRING = "Лінія, секція і т.п.";
    public static final String INSTALLATION_STRING = "Установка";

    private final PathListsDialog parent;

    private JButton buttonSave, buttonCancel;
    private JTextField elementName;

    private final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();

    private final Model model;
    private static String elementType(Model model){
        switch (model){
            case DEPARTMENT:
                return DEPARTMENT_STRING;
            case AREA:
                return AREA_STRING;
            case PROCESS:
                return PROCESS_STRING;
            case INSTALLATION:
                return INSTALLATION_STRING;
            default:
                return null;
        }
    }

    private final String oldNameOfElement;

    public PathElementName(PathListsDialog parent, Model model, String elementName){
        super(parent, elementType(model), true);
        this.parent = parent;
        this.model = model;
        this.oldNameOfElement = elementName;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.elementName = new JTextField(10);
        this.elementName.setHorizontalAlignment(SwingConstants.CENTER);
        if (this.oldNameOfElement != null){
            this.elementName.setText(this.oldNameOfElement);
        }
        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonSave = new DefaultButton(SAVE);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonSave.addActionListener(this.clickSave);
    }

    private void build() {
        this.setSize(230,100);
        this.setLocation(POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = e -> dispose();

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (elementName.getText().length() > 0) {
                dispose();
                switch (model) {
                    case DEPARTMENT:
                        if (oldNameOfElement == null){
                            departmentRepository.add(elementName.getText());
                        }else {
                            departmentRepository.set(oldNameOfElement, elementName.getText());
                        }
                        break;
                    case AREA:
                        if (oldNameOfElement == null){
                            areaRepository.add(elementName.getText());
                        }else {
                            areaRepository.set(oldNameOfElement, elementName.getText());
                        }
                        break;
                    case PROCESS:
                        if (oldNameOfElement == null){
                            processRepository.add(elementName.getText());
                        }else {
                            processRepository.set(oldNameOfElement, elementName.getText());
                        }
                        break;
                    case INSTALLATION:
                        if (oldNameOfElement == null){
                            installationRepository.add(elementName.getText());
                        }else {
                            installationRepository.set(oldNameOfElement, elementName.getText());
                        }
                        break;
                }
                parent.update(model);
            }
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(elementName, new Cell(0,0,2,0.9));
            this.add(buttonCancel, new Cell(0,1,1,0.1));
            this.add(buttonSave, new Cell(1,1,1,0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width, double height){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.weighty = height;
                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}