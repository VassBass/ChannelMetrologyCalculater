package repository;

import application.Application;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Repository<O> {
    protected final String dbUrl;
    protected final ArrayList<O>mainList;

    public Repository(){
        this.dbUrl = Application.pathToDB;
        this.mainList = new ArrayList<>();
        init();
    }

    public Repository(String dbUrl){
        this.dbUrl = dbUrl;
        this.mainList = new ArrayList<>();
        init();
    }

    protected Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        return DriverManager.getConnection(this.dbUrl);
    }

    protected abstract void init();
}
