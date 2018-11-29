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
import mytunesassigment.be.Song;

/**
 *
 * @author nedas
 */
public class PlaylistDAO {

    PlaylistSongDAO PlaylistSongInfo = new PlaylistSongDAO(); // Initialises the PlaylistDAO class
    SQLServerDataSource ds;

    /*
    Initialises the constructor. Gets the array from the DatabaseConnectionDAO and sets up the database so the class can use it.
     */
    public PlaylistDAO() throws IOException {
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
    Gets all existing playlists from database
     */
    public List<Playlist> getAllPlaylists() {
        List<Playlist> allPlaylists = new ArrayList<>(); // Creates a playlist array to store all playlists

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Playlist";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");
                List<Song> allSongs = PlaylistSongInfo.getPlaylistSongs(id); //Puts all songs into the playlist
                Playlist pl = new Playlist(allSongs.size(), countTotalTime(allSongs), name, id); //Creates a new playlist object
                pl.setSongList(allSongs); // Sets up the song list
                allPlaylists.add(pl); // Adds the playlist to the playlist array
            }
            return allPlaylists; // Returns the playlists
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    /*
    Counts all combined time of all songs in the playlist and outputs it in seconds.
     */
    private int countTotalTime(List<Song> allSongs) {
        int totalTime = 0;
        for (Song allSong : allSongs) {
            totalTime += allSong.getPlaytime();
        }
        return totalTime; //returns total count in seconds
    }

    /*
    Creates playlist with given name
     */
    public Playlist createPlaylist(String name) {
        String sql = "INSERT INTO Playlist(name) VALUES (?)";
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
        Playlist playlist = new Playlist(0, 0, name, getNewestPlaylist()); //Creates a playlist object and specifies that there are no songs present.
        return playlist;
    }

    /*
    Gets the newest inserted playlists ID in order to create a playlist object.
     */
    private int getNewestPlaylist() {
        int newestID = -1;
        try (Connection con = ds.getConnection()) {
            String query = "SELECT TOP(1) * FROM Playlist ORDER by id desc";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                newestID = rs.getInt("id");
            }
            System.out.println(newestID);
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
    Updates specified playlist with user given name
     */
    public void updatePlaylist(Playlist selectedItem, String name) {
        try (Connection con = ds.getConnection()) {
            String query = "UPDATE Playlist set name = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setInt(2, selectedItem.getID());
            preparedStmt.executeUpdate();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /*
    Deletes specified playlist from database.
     */
    public void deletePlaylist(Playlist play) {
        try (Connection con = ds.getConnection()) {
            String query = "DELETE from Playlist WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, play.getID());
            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
