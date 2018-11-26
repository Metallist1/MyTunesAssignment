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

    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>();

        try (Connection con = ds.getConnection()) {
            String sqlStatement = "SELECT * FROM Song";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                Song son = new Song(rs.getString("name"), rs.getString("artist"), rs.getString("category"), rs.getInt("time"), rs.getString("url"), rs.getInt("id"));
                allSongs.add(son);
            }
            return allSongs;

        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

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
        Song son = new Song(title, artist, category, playtime, location, getNewestSongID());

        return son;
    }

    private int getNewestSongID() {
        int newestID = -1;
        try (Connection con = ds.getConnection()) {
            String query = "SELECT TOP(1) * FROM Song ORDER by id desc";

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

    public void deleteSong(Song songToDelete) {
        try (Connection con = ds.getConnection()) {
            deleteFromPlaylistSongsEverything(songToDelete);
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

    private void deleteFromPlaylistSongsEverything(Song songToDelete) {
        try (Connection con = ds.getConnection()) {

            String query = "DELETE from PlaylistSong WHERE SongID = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, songToDelete.getID());

            preparedStmt.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);

        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }

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
            Song son = new Song(title, artist, category, playtime, location, song.getID());
            return son;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

}
