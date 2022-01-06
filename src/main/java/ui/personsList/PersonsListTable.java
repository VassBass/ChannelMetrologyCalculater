package ui.personsList;

import constants.Strings;
import model.Worker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;

public class PersonsListTable extends JTable {

    public PersonsListTable(final PersonsListDialog parent){
        super(tableModel());

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                parent.setEnabledButtons(true);
            }
        });
    }

    private static DefaultTableModel tableModel(){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[]{Strings.FIO, Strings.POSITION};
        model.setColumnIdentifiers(columnsHeader);

        /*String[][] list = new String[Objects.requireNonNull(Lists.persons()).size()][2];
        for (int x = 0; x < list.length; x++){
            Worker worker = Objects.requireNonNull(Lists.persons()).get(x);
            String fullName = worker.getSurname()
                    + " "
                    + worker.getName()
                    + " "
                    + worker.getPatronymic();
            list[x][0] = fullName;
            list[x][1] = worker.getPosition();

            model.addRow(list[x]);
        }*/

        return model;
    }
}
