package ui.model;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

public class Table<M> extends JTable {

    public Table(TableModel dm){
        super(dm);
    }

    public void setList(ArrayList<M>list){}
}
