/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunesassigment.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunesassigment.be.Playlist;

/**
 *
 * @author nedas
 */
public class CategoriesDAO { // Initialises the CategoriesDAO class

    SQLServerDataSource ds;

    /*
    Initialises the constructor. Gets the array from the DatabaseConnectionDAO and sets up the database so the class can use it.
     */
    public CategoriesDAO() throws IOException {
        this.ds = new SQLServerDataSource();
        DatabaseConnectionDAO connectionInfo = new DatabaseConnectionDAO();
        List<String> infoList = connectionInfo.getDatabaseInfo();
        ds.setDatabaseName(infoList.get(0));
        ds.setUser(infoList.get(1));
        ds.setPassword(infoList.get(2));
        ds.setPortNumber(Integer.parseInt(infoList.get(3)));
        ds.setServerName(infoList.get(4));
    }

    public List<String> getAllCategories() {
        List<String> allCategories = new ArrayList<>(); // Creates a String array to store all categories

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Catagory";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                allCategories.add(rs.getString("name")); // Adds the category to the String array
            }
            return allCategories; // Returns the String array
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /*
    Inserts a new category into Category table
     */
    public void createCategory(String name) {
        String sql = "INSERT INTO Catagory VALUES (?)";
        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.addBatch();
            ps.executeBatch();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /*
    Deletes category from Category table
     */
    public void deleteCategory(String name) {
        try (Connection con = ds.getConnection()) {
            String query = "DELETE from Catagory WHERE name = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

}
