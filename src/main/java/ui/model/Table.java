package ui.model;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;

public class Table<M> extends JTable {

    public Table(TableModel dm){
        super(dm);
    }

    public void setList(List<M> list){}
}
