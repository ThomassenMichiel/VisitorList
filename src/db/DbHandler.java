package db;

import elements.Visitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michiel Thomassen on 9/12/2016 at 01:28.
 * Project name: VisitorList
 * Package name: db
 * File name: DbHandler
 */
public class DbHandler {
    private Connection c = null;
    private Statement statement = null;
    private String dbSelection = "jdbc:sqlite:";

    public DbHandler(String dbName) {
        dbSelection += dbName;
        connect();
    }

    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(dbSelection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            this.connect();
            statement = c.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS Visitorlist" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Date STRING NOT NULL," +
                    "Name STRING NOT NULL," +
                    "Municipality STRING NOT NULL," +
                    "Copies BOOLEAN," +
                    "Mail BOOLEAN," +
                    "Taxes BOOLEAN," +
                    "Others BOOLEAN)";
            statement.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS Municipalities" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Municipality STRING NOT NULL)";
            statement.executeUpdate(sql);

            statement.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMunicipality(String municipality) {
        try {
            this.connect();
            statement = c.createStatement();
            c.setAutoCommit(false);

            String sql = "INSERT INTO Municipalities (Municipality) VALUES (\"" + municipality + "\" );";
            statement.executeUpdate(sql);
            statement.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addVisitor(String date,
                           String name,
                           String municipality,
                           boolean copies,
                           boolean mail,
                           boolean taxes,
                           boolean others) {
        try {
            this.connect();
            statement = c.createStatement();
            c.setAutoCommit(false);

            String sql = "INSERT INTO Visitorlist (Date, Name, Municipality, Copies, Mail, Taxes, Others) " +
                    "VALUES (\"" + date + "\", \"" + name + "\", \"" + municipality + "\", \"" + copies + "\", \"" + mail + "\", \"" + taxes  + "\", \"" + others + "\");";
            statement.executeUpdate(sql);
            statement.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectVisitor() {
        try {
            this.connect();
            statement = c.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM Visitorlist;");
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectMunicipalities() {
        try {
            this.connect();
            statement = c.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM Municipalities ORDER BY `Municipality` COLLATE NOCASE;");
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Integer> countItems(List<String> items, String column, String table) {
        try {
            this.connect();
            statement = c.createStatement();

            Map<String, Integer> counts = new HashMap<>();
            //List<Integer> counts = new ArrayList<>();
            ResultSet rs = statement.executeQuery("select municipality, count(*) as 'count' from visitorlist group by municipality;");

            while (rs.next()) {
                counts.put(rs.getString(1), rs.getInt(2));
                //counts.add(rs.getInt("count"));
            }


            return counts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateVisitor(Visitor oldVisitor, Visitor newVisitor) {
        try{
            this.connect();;
            statement = c.createStatement();
            c.setAutoCommit(false);

            String sql = "update visitorlist set name = \"" + newVisitor.getName()
                    + "\", municipality = \"" + newVisitor.getMunicipality()
                    + "\", copies = \"" + newVisitor.getCopies()
                    + "\", mail = \"" + newVisitor.getMail()
                    + "\", taxes = \"" + newVisitor.getTaxes()
                    + "\", others = \"" + newVisitor.getOthers()
                    + "\" where name = \"" + oldVisitor.getName()
                    + "\" and municipality = \"" + oldVisitor.getMunicipality()
                    + "\" and copies = \"" + oldVisitor.getCopies()
                    + "\" and mail = \"" + oldVisitor.getMail()
                    + "\" and taxes = \""  + oldVisitor.getTaxes()
                    + "\" and others = \""  + oldVisitor.getOthers() + "\"";

            statement.executeUpdate(sql);
            statement.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
