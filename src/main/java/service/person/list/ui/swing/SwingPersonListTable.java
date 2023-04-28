package service.person.list.ui.swing;

import model.dto.Person;
import repository.RepositoryFactory;
import repository.repos.person.PersonRepository;
import service.person.list.ui.PersonListTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class SwingPersonListTable extends JTable implements PersonListTable {
    private static final String COLUMN_NAME = "ПІБ";
    private static final String COLUMN_POSITION = "Посада";

    private final PersonRepository personRepository;
    private final List<Person> personList;

    public SwingPersonListTable(@Nonnull RepositoryFactory repositoryFactory){
        super();
        personRepository = repositoryFactory.getImplementation(PersonRepository.class);
        personList = new ArrayList<>(personRepository.getAll());

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setModel(tableModel(personList));
    }

    private DefaultTableModel tableModel(List<Person> personList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {
                COLUMN_NAME,
                COLUMN_POSITION
        };
        model.setColumnIdentifiers(columnsHeader);

        for (Person person : personList) {
            String[] data = new String[2];
            data[0] = String.format("%s %s %s", person.getSurname(), person.getName(), person.getPatronymic());
            data[1] = person.getPosition();
            model.addRow(data);
        }

        return model;
    }

    @Nullable
    @Override
    public Person getSelectedId() {
        int row = this.getSelectedRow();
        return row < 0 ? null : personList.get(row);
    }

    @Override
    public void updateContent() {
        personList.clear();
        personList.addAll(personRepository.getAll());
        this.setModel(tableModel(personList));
    }
}
