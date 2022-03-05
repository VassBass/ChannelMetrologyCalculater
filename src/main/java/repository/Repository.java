package repository;

import application.Application;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository {
    protected final String dbUrl;

    public Repository(){
        this.dbUrl = Application.pathToDB;
        this.init();
    }

    public Repository(String dbUrl){
        this.dbUrl = dbUrl;
        this.init();
    }

    protected Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        return DriverManager.getConnection(this.dbUrl);
    }

    protected abstract void init();
}
