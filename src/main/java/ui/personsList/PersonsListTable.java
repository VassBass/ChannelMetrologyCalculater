package ui.personsList;

import model.Person;
import service.impl.PersonServiceImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class PersonsListTable extends JTable {
    private static final String FIO = "ПІБ";
    private static final String POSITION = "Посада";

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

        String[]columnsHeader = new String[]{FIO, POSITION};
        model.setColumnIdentifiers(columnsHeader);

        ArrayList<Person>workers = new ArrayList<>(PersonServiceImpl.getInstance().getAll());
        String[][] list = new String[workers.size()][2];
        for (int x = 0; x < list.length; x++){
            Person worker = workers.get(x);
            String fullName = worker.getSurname()
                    + " "
                    + worker.getName()
                    + " "
                    + worker.getPatronymic();
            list[x][0] = fullName;
            list[x][1] = worker.getPosition();

            model.addRow(list[x]);
        }
        return model;
    }
}