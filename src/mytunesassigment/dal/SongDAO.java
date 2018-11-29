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
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */
public class SongDAO {

    SQLServerDataSource ds;

    /*
    Initialises the constructor. Also gets the database information from DatabaseConnectionDAO and sets up the connectio for the whole class.
     */
    public SongDAO() throws IOException {
        this.ds = new SQLServerDataSource();
        DatabaseConnectionDAO connectionInfo = new DatabaseConnectionDAO();
        List<String> infoList = connectionInfo.getDatabaseInfo();
        ds.setDatabaseName(infoList.get(0));
        ds.setUser(infoList.get(1));
        ds.setPassword(infoList.get(2));
        ds.setPortNumber(Integer.parseInt(infoList.get(3)));
        ds.setServerName(infoList.get(4));
    }

    /*
    Gets all songs in the database
     */
    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Song";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) { // Creates and adds song objects into an array list
                Song son = new Song(rs.getString("name"), rs.getString("artist"), rs.getString("category"), rs.getInt("time"), rs.getString("url"), rs.getInt("id"));
                allSongs.add(son);
            }
            return allSongs; //Returns the full list
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /*
    Creates a song and inserts it into the database
     */
    public Song createSong(String title, String artist, String category, int playtime, String location) {
        String sql = "INSERT INTO Song(name,artist,category,time,url) VALUES (?,?,?,?,?)";
        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, artist);
            ps.setString(3, category);
            ps.setInt(4, playtime);
            ps.setString(5, location);
            ps.addBatch();
            ps.executeBatch();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        Song son = new Song(title, artist, category, playtime, location, getNewestSongID()); // Creates a song object
        return son; //Returns the song object
    }

    /*
    Gets the top songs ID from the database so it is possible to create the song object
     */
    private int getNewestSongID() {
        int newestID = -1; // Default ID not found
        try (Connection con = ds.getConnection()) {
            String query = "SELECT TOP(1) * FROM Song ORDER by id desc"; //Selects the biggest song ID in the database
            PreparedStatement preparedStmt = con.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                newestID = rs.getInt("id");
            }
            return newestID;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return newestID;
        } catch (SQLException ex) {
            System.out.println(ex);
            return newestID;
        }
    }

    /*
    Updates song in the database with the new specifics given by the user
     */
    public Song updateSong(Song song, String title, String artist, String category, int playtime, String location) {
        try (Connection con = ds.getConnection()) {
            String query = "UPDATE Song set name = ?,artist = ?,category = ?,time = ?,url = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, title);
            preparedStmt.setString(2, artist);
            preparedStmt.setString(3, category);
            preparedStmt.setInt(4, playtime);
            preparedStmt.setString(5, location);
            preparedStmt.setInt(6, song.getID());
            preparedStmt.executeUpdate();
            Song son = new Song(title, artist, category, playtime, location, song.getID()); //creates a new song object.
            return son;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /*
    Deletes specified song from the database.
     */
    public void deleteSong(Song songToDelete) {
        try (Connection con = ds.getConnection()) {
            String query = "DELETE from Song WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, songToDelete.getID());
            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
